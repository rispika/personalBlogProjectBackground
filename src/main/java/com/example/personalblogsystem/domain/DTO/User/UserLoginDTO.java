package com.example.personalblogsystem.domain.DTO.User;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String key;
    private String account;
    private String password;
    private String code;
}
