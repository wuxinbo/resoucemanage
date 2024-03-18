package com.wu.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Constant {

    /**
     * 访问地址
     */
    public static final String URL="http://192.168.2.3:8080/";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT= "yyyy年M月dd日";
    public static final String FILE_PROVIDER="com.wu.resource.file_provider";
    public static Gson gson = new GsonBuilder().setDateFormat(TIME_FORMAT).create();


}
