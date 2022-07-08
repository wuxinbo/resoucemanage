package com.wu.resource.utils;

import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;

import com.wu.resource.Constant;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

    public static OkHttpClient httpClient =new OkHttpClient();
//    HandlerCompat.
//    HandlerCompat.createAsync(Looper.getMainLooper());
    /**
     * 默认线程池
     */
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 数据查询
     * @param url
     * @param callBack
     */
    public static void getJson(String url,HttpCallBack<String> callBack){
        Request request = new Request.Builder()
                .url(Constant.URL+url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String result =response.body().string();
            Log.i("http json", result);
            callBack.onResponse(result);
        } catch (IOException e) {
            Log.e("IOException",e.getMessage());
        }
    }

}
