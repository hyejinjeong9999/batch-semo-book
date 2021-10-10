package com.semobook.tools;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TimeFormat {

    public static String simpleDateFormat(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String resultTime = simpleDateFormat.format(new Date(time));
        return resultTime;
    }
}
