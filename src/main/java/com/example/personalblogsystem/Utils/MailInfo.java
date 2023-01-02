package com.example.personalblogsystem.Utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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


    public static void sendMailToQQ(MailInfo mailInfo){
        try {
            log.info("=======进入QQ邮件发送方法=======");
            MailAccount account = new MailAccount();
            account.setHost("smtp.qq.com");
            account.setPort(587);
            account.setAuth(true);
            account.setFrom("3382868941@qq.com");
            account.setUser("3382868941@qq.com");
            account.setPass("wljcobynlcpdcgjh"); //邮箱设置获取到的授权码
            MailUtil.send(account, CollUtil.newArrayList(mailInfo.getMails()),
                    mailInfo.getTitle(), mailInfo.getMessage(), false);
            log.info("=======QQ邮件发送成功======="+ JSONUtil.toJsonStr(mailInfo));
        } catch (Exception e) {
            log.error("=======QQ邮件发送失败======="+e.getMessage(),e);
        }

    }

    public static String sendVerificationCode(String email) {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setTitle("验证码");
        String verificationCode = RandomUtil.randomString(5);
        mailInfo.setMessage("验证码为: " + verificationCode + ";验证码将在30分钟后过期");
        List<String> mails = new ArrayList<>();
        mails.add(email);
        mailInfo.setMails(mails);
        MailInfo.sendMailToQQ(mailInfo);
        return verificationCode;
    }

}