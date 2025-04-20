package com.wuxinbo.resourcemanage.db;

import android.util.Log;

import androidx.room.TypeConverter;


import com.wuxinbo.resourcemanage.Constant;
import com.wuxinbo.resourcemanage.image.PhotoInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTypeConverter {

    @TypeConverter
    public static String dataToString(Date date){
        if(date==null){
            return "";
        }
        return new SimpleDateFormat(Constant.TIME_FORMAT).format(date);
    }
    @TypeConverter
    public static Date StringtoDate(String value){
        try {
            return new SimpleDateFormat(Constant.TIME_FORMAT).parse(value);
        } catch (ParseException e) {
            Log.e("TypeConvertor","parseException",e);
        }
        return null;
    }
    @TypeConverter
    public static PhotoInfo.SysFileStoreItem jsonToItem(String value){
        return Constant.gson.fromJson(value,PhotoInfo.SysFileStoreItem.class);
    }
    @TypeConverter
    public static String fileStoreItemToJson(PhotoInfo.SysFileStoreItem fileStoreItem){
        return Constant.gson.toJson(fileStoreItem);
    }
}
