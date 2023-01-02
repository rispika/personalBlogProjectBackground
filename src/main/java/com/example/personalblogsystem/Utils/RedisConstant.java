package com.example.personalblogsystem.Utils;

public class RedisConstant {

    //验证码
    public static String REGISTER_CODE = "email:code:";
    public static String LOGIN_CODE = "login:code:";
    public static long LOGIN_CODE_TTL = 1L; //分钟
    public static long REG_CODE_TTL = 10L; //分钟
    public static long LOGIN_TOKEN_TTL = 30L; //分钟
    public static String LOGIN_TOKEN = "user:token:";

}
