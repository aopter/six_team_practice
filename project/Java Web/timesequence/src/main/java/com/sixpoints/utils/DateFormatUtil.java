package com.sixpoints.utils;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ASUS
 * @createTime 2021/5/6 9:07
 * @projectName demo
 * @className DateFormatUtil.java
 * @description 时间转换
 */
@Service
public class DateFormatUtil {

    public String formatDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date(time);
        return format.format(date);
    }
}
