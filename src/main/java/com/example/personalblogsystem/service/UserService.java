package com.example.personalblogsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.personalblogsystem.controller.result.Result;
import com.example.personalblogsystem.domain.DTO.User.UserLoginDTO;
import com.example.personalblogsystem.domain.DTO.User.UserRegisterDTO;
import com.example.personalblogsystem.domain.User;

public interface UserService extends IService<User> {
    Result sendEmailVerificationCode(String email);

    Result sendVerificationCode(String key);

    Result registerAccount(UserRegisterDTO dto);

    Result loginAccount(UserLoginDTO dto);

    Result getUserByToken(String token);
}
