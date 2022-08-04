package com.wu.resource.db;

import android.util.Log;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.wu.resource.Constant;
import com.wu.resource.image.PhotoInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTypeConverter {

    @TypeConverter
    public static String dataToString(Date date){
        return new SimpleDateFormat(Constant.DATE_FORMAT).format(date);
    }
    @TypeConverter
    public static Date StringtoDate(String value){
        try {
            return new SimpleDateFormat(Constant.DATE_FORMAT).parse(value);
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
