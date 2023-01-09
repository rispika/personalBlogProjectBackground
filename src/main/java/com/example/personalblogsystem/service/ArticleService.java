package com.example.personalblogsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.personalblogsystem.controller.result.Result;
import com.example.personalblogsystem.domain.Article;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

public interface ArticleService extends IService<Article> {
    Result addArticle(String name, String label, String content, MultipartFile img, String time) throws IOException;
}
