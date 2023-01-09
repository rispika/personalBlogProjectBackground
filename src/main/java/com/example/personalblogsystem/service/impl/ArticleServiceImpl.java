package com.example.personalblogsystem.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.personalblogsystem.Utils.DateTimeFormat;
import com.example.personalblogsystem.Utils.GiteeUtil;
import com.example.personalblogsystem.controller.result.Result;
import com.example.personalblogsystem.domain.Article;
import com.example.personalblogsystem.domain.ImgMd5;
import com.example.personalblogsystem.mapper.ArticleMapper;
import com.example.personalblogsystem.mapper.ImgMd5Mapper;
import com.example.personalblogsystem.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ImgMd5Mapper imgMd5Mapper;
    @Autowired
    private RestTemplate restTemplate;
    /**
     * 博客文章添加功能
     * @param name
     * @param label
     * @param content
     * @param img
     * @param time
     * @return
     */
    @Override
    @Transactional
    public Result addArticle(String name, String label, String content, MultipartFile img, String time) throws IOException {
        Date datetime = null;
        try {
            datetime = DateTimeFormat.strToDateTime(time);
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("时间格式转换失败...");
            return Result.fail("时间格式转换失败...");
        }
        //第一次封装
        Article article = new Article();
        article.setName(name);
        article.setLabel(label);
        article.setContent(content);
        article.setTime(datetime);
        //md5校验判断
        byte[] imgBytes = img.getBytes();
        String imgMD5 = DigestUtil.md5Hex(imgBytes);
        //数据库查询
        Article md5 = query().eq("md5", imgMD5).one();
        if (md5 != null) {
            //当存在相同md5时,秒传
            //1.查找当时存储的地址
            String imgPath = md5.getImgPath();
            //2.设置路径和md5序号
            article.setImgPath(imgPath);
            article.setMd5(imgMD5);
        } else {
            //当不存在相同md5时,上传
            String img64 = Base64.encode(imgBytes);
            String path = "/article/" + name + System.currentTimeMillis();
            GiteeUtil giteeUtil = new GiteeUtil(path,img64);
            //请求url
            String uploadUrl = giteeUtil.getUploadUrl();
            //LinkedMultiValueMap一个键对应多个值，对应format-data的传入类型
            LinkedMultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
            //入参
            request.set("access_token",giteeUtil.getAccess_token());
            request.set("owner", giteeUtil.getOwner());
            request.set("repo", giteeUtil.getRepo());
            request.set("path", giteeUtil.getPath());
            request.set("content", giteeUtil.getContent());
            request.set("message", giteeUtil.getMessage());
            //头部类型
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            //构造实体对象
            HttpEntity<MultiValueMap<String, Object>> param = new HttpEntity<>(request, headers);
            //发起请求,服务地址，请求参数，返回消息体的数据类型
            ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, param, String.class);
            //返回response code
            HttpStatus statusCode = response.getStatusCode();
            log.info("向gitee发出请求,响应码为{}",statusCode);
            //返回body
            String body = response.getBody();
            JSONObject entries = JSONUtil.parseObj(body);
            String content1 = entries.get("content").toString();
            JSONObject entries1 = JSONUtil.parseObj(content1);
            String imgUrl = entries1.get("download_url").toString();
            //封装
            article.setImgPath(imgUrl);
            article.setMd5(imgMD5);
        }

        //保存
//        article.setImgPath("https://gitee.com/ris-two-thousand-and-twenty/personal-blog-map-bed/raw/master" + path);
        save(article);
        return Result.success();
    }
}
