package com.example.personalblogsystem.domain.DTO.User;

import lombok.Data;

import java.util.Date;
@Data
public class UserDTO {
    private Long id;
    private String account;
    private String name;
    private String email;
    private Date createdTime;

}
