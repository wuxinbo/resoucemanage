package com.wu.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Constant {

  /**
   * 访问地址
   */
  public static final String URL = "http://192.168.2.3:8081/";
  /**
   * 分页查询照片
   */
  public static final String PHOTO_LIST_BY_PAGE = "photo/listByPage";
  /**
   * 根据拍摄时间查询
   */
  public static final String PHOTO_LIST_BY_SHOTDATE = "photo/listByShotDate";
  /**
   * 按照拍摄时间排序
   */
  public static final String PHOTO_GROUP_BY_SHOT_TIME = "photo/queryPhotoGroupby";
  public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT = "yyyy年M月dd日";
  public static final String FILE_PROVIDER = "com.wu.resource.file_provider";
  public static final String PHOTO_KEY = "photo";
  public static final String URL_KEY = "url";
  public static final String SEARCH_DATE = "searchDate";
  public static final String SHOT_DATE = "shotDate";
  public static Gson gson = new GsonBuilder().setDateFormat(TIME_FORMAT).create();


}
