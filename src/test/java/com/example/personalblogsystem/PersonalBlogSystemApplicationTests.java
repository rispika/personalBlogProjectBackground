package com.example.personalblogsystem;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.personalblogsystem.Utils.MailInfo;
import com.example.personalblogsystem.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootTest
class PersonalBlogSystemApplicationTests {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void send() {
//        MailInfo.sendVerificationCode("3382868941@qq.com");
    }

    @Test
    void ttt() {
        System.out.printf("|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|\n"
                        + "|             验证码:             |\n"
                        + "|--------------------------------|\n"
                        + "|             ASDFF              |\n"
                        + "|--------------------------------|\n"
                        + "|     此验证码将在10分钟后过期。     |\n"
                        + "|--------------------------------|\n"
                        + "|在此祝您时时快乐，分分精彩，秒秒幸福. |\n"
                        + "|________________________________|\n"
        );
    }


    @Test
    void ccc() {
//        String htmlStr = MailInfo.getHtmlStr("templates/mailHtml.html");
//        htmlStr = StrUtil.replace(htmlStr, 26, "${verificaion}", "qwert", false);
//        System.out.println(htmlStr);
    }

}
