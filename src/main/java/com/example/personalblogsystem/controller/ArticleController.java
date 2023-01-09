package com.example.personalblogsystem.controller;

import com.example.personalblogsystem.controller.result.Result;
import com.example.personalblogsystem.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/article")
@CrossOrigin(origins = "*")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 博客文章添加功能
     * @param name
     * @param label
     * @param content
     * @param img
     * @param time
     * @return
     */
    @PostMapping("/add")
    public Result addArticle(String name, String label, String content, MultipartFile img, String  time) throws IOException {
        return articleService.addArticle(name, label, content, img, time);
    }

    /**
     * @author ris
     * @createTime 2023/1/8 16:30
     * @desc 获取全部文章
     * @param
     * @return
     */
    @GetMapping("/getAll")
    public Result getAllArticle() {
        return Result.success(articleService.query().list());
    }

}
