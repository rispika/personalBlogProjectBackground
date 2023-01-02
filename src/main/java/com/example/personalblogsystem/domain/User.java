package com.example.personalblogsystem.domain;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private Long id;
    private String account;
    private String password;
    private String name;
    private String email;
    private Date createdTime;

}
