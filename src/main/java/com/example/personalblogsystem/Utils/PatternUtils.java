package com.example.personalblogsystem.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {

    /**
     * 电子邮箱正则
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        //电子邮件
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

}
