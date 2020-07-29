package com.mooc.ppjoke.utils;

import java.util.Calendar;

public class TimeUtils {
    public static String calculate(long time) {
        long timeInMillis = Calendar.getInstance().getTimeInMillis();

        //兼容脏数据。抓取的数据有些帖子的时间戳不是标准的十三位
        String valueOf = String.valueOf(time);
        if (valueOf.length() < 13) {
            time = time * 1000;
        }
        long diff = (timeInMillis - time) / 1000;
        if (diff <= 5) {
            return "刚刚";
        } else if (diff < 60) {
            return diff + "秒前";
        } else if (diff < 3600) {
            return diff / 60 + "分钟前";
        } else if (diff < 3600 * 24) {
            return diff / (3600) + "小时前";
        } else {
            return diff / (3600 * 24) + "天前";
        }
    }
}
