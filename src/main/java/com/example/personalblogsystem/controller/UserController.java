package com.example.personalblogsystem.controller;

import com.example.personalblogsystem.controller.result.Result;
import com.example.personalblogsystem.domain.DTO.User.UserLoginDTO;
import com.example.personalblogsystem.domain.DTO.User.UserRegisterDTO;
import com.example.personalblogsystem.service.UserService;
import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {


    @Autowired
    UserService userService;

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @return
     */
    @GetMapping("/code/{email}")
    public Result sendEmailVerificationCode(@PathVariable("email") String email) {
        return userService.sendEmailVerificationCode(email);
    }

    /**
     * 请求登录验证码
     * @return
     */
    @GetMapping({"/getCode/{key}","/getCode"})
    public Result sendVerificationCode(@PathVariable(value = "key",required = false) String key) {
        return userService.sendVerificationCode(key);
    }

    /**
     * 注册
     * @param dto
     * @return
     */
    @PostMapping("/register")
    public Result registerAccount(@RequestBody UserRegisterDTO dto) {
        return userService.registerAccount(dto);
    }

    /**
     * 登录功能实现
     * @param dto
     * @return
     */
    @PostMapping("/login")
    public Result loginAccount(@RequestBody UserLoginDTO dto) {
        return userService.loginAccount(dto);
    }


    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    @GetMapping("/token/{token}")
    public Result getUserByToken(@PathVariable("token") String token) {
        return userService.getUserByToken(token);
    }

}
