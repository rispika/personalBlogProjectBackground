package com.example.personalblogsystem.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class User {

    private Long id;
    private String account;
    private String password;
    private String name;
    private String email;
    @DateTimeFormat(pattern = "yy-MM-dd")
    private Date createdTime;

}
