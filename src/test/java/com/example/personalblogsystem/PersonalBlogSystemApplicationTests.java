package com.example.personalblogsystem;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.example.personalblogsystem.Utils.MailInfo;
import com.example.personalblogsystem.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootTest
class PersonalBlogSystemApplicationTests {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void test() {
//        User user = new User();
//        user.setId(1L);
//        user.setAccount("123321");
//        user.setPassword("123321");
//        user.setName("虎哥");
//        user.setCreatedTime(new Date());
//        String json = JSONUtil.toJsonStr(user);
//        stringRedisTemplate.opsForValue().set("user",json);
        MailInfo mailInfo = new MailInfo();
        mailInfo.setTitle("验证码");
        String verificationCode = RandomUtil.randomStringUpper(5);
        mailInfo.setMessage("验证码为: " + verificationCode + ";验证码将在30分钟后过期");
        List<String> mails = new ArrayList<>();
        mails.add("qq3382868941@163.com");
        mailInfo.setMails(mails);
        MailInfo.sendMailToQQ(mailInfo);
    }

}
