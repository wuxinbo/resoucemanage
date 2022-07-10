package com.wu.resource.db;

import android.util.Log;

import androidx.room.TypeConverter;

import com.wu.resource.Constant;

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
}
