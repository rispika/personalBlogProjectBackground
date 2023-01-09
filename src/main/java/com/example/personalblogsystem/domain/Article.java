package com.example.personalblogsystem.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Article {

    private Long id;
    private String name;
    private String label;
    private String content;
    private String imgPath;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    private String md5;

}
