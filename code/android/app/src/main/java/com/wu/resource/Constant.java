package com.wu.resource;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Constant {

    /**
     * 访问地址
     */
    public static final String URL="http://192.168.2.3:8080/";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();


}
