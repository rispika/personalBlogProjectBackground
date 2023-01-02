package com.example.personalblogsystem.domain.DTO.User;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String account;
    private String password;
    private String rePassword;
    private String email;
    private String code;
}
