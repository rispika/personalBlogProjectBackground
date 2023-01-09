package com.example.personalblogsystem.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.personalblogsystem.Utils.MailInfo;
import com.example.personalblogsystem.Utils.PatternUtils;
import com.example.personalblogsystem.controller.result.Result;
import com.example.personalblogsystem.domain.DTO.User.UserDTO;
import com.example.personalblogsystem.domain.DTO.User.UserLoginDTO;
import com.example.personalblogsystem.domain.DTO.User.UserRegisterDTO;
import com.example.personalblogsystem.domain.DTO.User.VerificationCodeDTO;
import com.example.personalblogsystem.domain.User;
import com.example.personalblogsystem.mapper.UserMapper;
import com.example.personalblogsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.personalblogsystem.Utils.RedisConstant.*;
import static com.example.personalblogsystem.Utils.RedisConstant.LOGIN_CODE_TTL;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 发送邮箱验证码
     * @param email
     * @return
     */
    @Override
    public Result sendEmailVerificationCode(String email) {
        //1.验证email格式
        if (!PatternUtils.checkEmail(email))
            return Result.fail("邮箱格式错误!");
        //2.判断该用户1分钟内有无发送过
        //2.1 如果发送过,则直接返回
        if (stringRedisTemplate.opsForValue().getOperations().getExpire(REGISTER_CODE + email) >= 540)
            return Result.fail("请求时间间隔小于60s...");
        //-> 校验该邮箱是否注册过
        User one = query().eq("email", email).one();
        if (one != null) {
            return Result.fail("该邮箱已被注册...");
        }
        //3.发送邮件
        String verificationCode = MailInfo.sendVerificationCode(email);
        //4.将验证码存入redis
        stringRedisTemplate.opsForValue().set(REGISTER_CODE + email, verificationCode, REG_CODE_TTL, TimeUnit.MINUTES);
        return Result.success();
    }

    /**
     * 请求登录验证码
     * @return
     */
    @Override
    public Result sendVerificationCode(String key) {
        // 随机生成 4 位验证码
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
        // 定义图片的显示大小
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(100, 30);
        //判断是否已有key
        if (StrUtil.isBlank(key))
           //生成对应的key
            key = RandomUtil.randomString(16);
        // 调用父类的 setGenerator() 方法，设置验证码的类型
        lineCaptcha.setGenerator(randomGenerator);
        // 输出到页面
        String imageBase64 = lineCaptcha.getImageBase64();
        // 封装
        VerificationCodeDTO code = new VerificationCodeDTO();
        code.setKey(key);
        code.setImageBase64(imageBase64);
        // 打印日志
        log.info("发送验证码中.,key为:{}",key);
        log.info("生成的验证码:{}", lineCaptcha.getCode());
        // 写入redis
        stringRedisTemplate.opsForValue().set(LOGIN_CODE + key, lineCaptcha.getCode(), LOGIN_CODE_TTL, TimeUnit.MINUTES);
        return Result.success(code);
    }

    /**
     * 注册账号
     * @param dto
     * @return
     */
    @Override
    public Result registerAccount(UserRegisterDTO dto) {
        //1.验证-验证码
        String code = dto.getCode();
        String redisCode = stringRedisTemplate.opsForValue().get(REGISTER_CODE + dto.getEmail());
        if (!code.equals(redisCode)) {
            //如果验证码不一致
            return Result.fail("验证码不一致...");
        }
        //2.判断该账号是否有人注册过
        User account = query().eq("account", dto.getAccount()).one();
        if (account != null) {
            return Result.fail("该账号已被注册过...");
        }
        //3.封装User
        User user = new User();
        user.setAccount(dto.getAccount());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setCreatedTime(new Date());
        user.setName("用户" + RandomUtil.randomString(5));
        //4.存入数据库
        save(user);
        //5.1.拷贝并生成token
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> objectMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        String token = IdUtil.randomUUID();
        //5.2存入redis
        stringRedisTemplate.opsForHash().putAll(LOGIN_TOKEN+token,objectMap);
        stringRedisTemplate.opsForHash().getOperations().expire(LOGIN_TOKEN+token,LOGIN_TOKEN_TTL,TimeUnit.MINUTES);
        //6.返回token
        return Result.success(token);
    }

    /**
     * 登录
     * @param dto
     * @return
     */
    @Override
    public Result loginAccount(UserLoginDTO dto) {
        //0.解析json
        //1.判断account
        if (dto.getAccount().length() <= 3)
            return Result.fail("账号格式输入错误...");
        //2.判断password
        if (dto.getPassword().length() <= 5) {
            return Result.fail("密码格式输入错误...");
        }
        //3.判断code
        //3.1取验证码
        String code = stringRedisTemplate.opsForValue().get(LOGIN_CODE + dto.getKey());
        //3.2判断
        if (!dto.getCode().equals(code)) {
            return Result.fail("验证码不一致...");
        }
        //4.判断有没有这个人
        User user = query().eq("account", dto.getAccount()).eq("password", dto.getPassword()).one();
        if ( user == null)
            return Result.fail("账号或密码错误...");
        //5.登录成功,生成token
        //5.1封装
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> objectMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        String token = IdUtil.randomUUID();
        //5.2存入redis
        stringRedisTemplate.opsForHash().putAll(LOGIN_TOKEN+token,objectMap);
        stringRedisTemplate.opsForHash().getOperations().expire(LOGIN_TOKEN+token,LOGIN_TOKEN_TTL,TimeUnit.MINUTES);
        //5.3返回token
        return Result.success(token);
    }

    /**
     * 由token获取信息
     * @param token
     * @return
     */
    @Override
    public Result getUserByToken(String token) {
        //检查token是否为空
        if (StrUtil.isBlank(token) || token.equals("null"))
            return Result.fail("token为空...");
        //不为空,查找
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(LOGIN_TOKEN + token);
        if (MapUtil.isEmpty(entries)) {
            return Result.fail("token已过期...");
        }
        UserDTO userDTO = BeanUtil.mapToBean(entries, UserDTO.class, false);
        return Result.success(userDTO);
    }


}
