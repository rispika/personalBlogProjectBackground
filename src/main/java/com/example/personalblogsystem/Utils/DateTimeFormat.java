package com.example.personalblogsystem.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeFormat {

    /**
     * @author ris
     * @createTime 2023/1/8 17:36
     * @desc String转时间
     * @param str
     * @return 时间
     */
    public static Date strToDateTime(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = simpleDateFormat.parse(str);
        return datetime;
    }

}
