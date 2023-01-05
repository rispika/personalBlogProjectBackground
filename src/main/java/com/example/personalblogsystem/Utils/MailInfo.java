package com.example.personalblogsystem.Utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class MailInfo {
    //接收邮箱集合
    private List<String> mails;
    //标题
    private String title;
    //内容
    private String message;
    //Html
    private String htmlContent;

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlUrl) {
        this.htmlContent = getHtmlStr(htmlUrl);
    }
    public void setHtmlContent(String htmlUrl,String verificationCode) {
        String htmlStr = getHtmlStr(htmlUrl);
        this.htmlContent = StrUtil.replace(htmlStr, 26, "${verificaion}", verificationCode, false);
    }

    public static void sendMail(MailInfo mailInfo){
        try {
            log.info("=======进入QQ邮件发送方法=======");
            MailAccount account = new MailAccount();
            account.setHost("smtp.qq.com");
            account.setPort(587);
            account.setAuth(true);
            account.setFrom("unkonwn_website@foxmail.com");
            account.setUser("unkonwn_website@foxmail.com");
//            account.setPass("wljcobynlcpdcgjh"); //邮箱设置获取到的授权码
            account.setPass("vxulywcsrccxddji"); //邮箱设置获取到的授权码
            MailUtil.send(account, CollUtil.newArrayList(mailInfo.getMails()),
                    mailInfo.getTitle(), mailInfo.getHtmlContent(), true);
            log.info("=======QQ邮件发送成功======="+ JSONUtil.toJsonStr(mailInfo));
        } catch (Exception e) {
            log.error("=======QQ邮件发送失败======="+e.getMessage(),e);
        }

    }

    public static String getHtmlStr(String htmlUrl) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(htmlUrl);
            InputStream inputStream = classPathResource.getInputStream();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String str = result.toString(StandardCharsets.UTF_8.name());
            return str;
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    public static String sendVerificationCode(String email) {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setTitle("一个不知名小网站传来的验证码");
        String verificationCode = RandomUtil.randomString(5);
//        mailInfo.setMessage("|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|\n"
//                + "|             验证码:             |\n"
//                + "|--------------------------------|\n"
//                + "|             ASDFF              |\n"
//                + "|--------------------------------|\n"
//                + "|     此验证码将在10分钟后过期。     |\n"
//                + "|--------------------------------|\n"
//                + "|在此祝您时时快乐，分分精彩，秒秒幸福. |\n"
//                + "|________________________________|\n");
        mailInfo.setHtmlContent("templates/mailHtml.html",verificationCode);
        List<String> mails = new ArrayList<>();
        mails.add(email);
        mailInfo.setMails(mails);
        MailInfo.sendMail(mailInfo);
        return verificationCode;
    }

}