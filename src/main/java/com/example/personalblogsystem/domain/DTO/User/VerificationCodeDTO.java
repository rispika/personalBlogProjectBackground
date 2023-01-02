package com.example.personalblogsystem.domain.DTO.User;

import lombok.Data;

@Data
public class VerificationCodeDTO {

    private String key;
    private String imageBase64;

}
