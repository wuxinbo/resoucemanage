package com.wu.common.http;


import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtil {

    public static OkHttpClient httpClient =new OkHttpClient();

    /**
     * 默认线程池
     */
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 数据查询
     * @param url
     * @param callBack
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void getJson(String url, HttpCallBack<String> callBack){
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            String result =response.body().string();
            Log.i("http request Url", url);
            Log.i("http response json", result);
            callBack.onResponse(result);
        } catch (IOException e) {
            Log.e("IOException",e.getMessage());
        }
    }

    /**
     * 使用http 文件下载
     * @param url
     * @param callBack
     */
    public static void  downloadFile(String url,HttpCallBack<InputStream> callBack){
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            ResponseBody body = response.body();
            InputStream inputStream = body.byteStream();
            callBack.onResponse(inputStream);
        } catch (IOException e) {
            Log.e("IOException",e.getMessage());
        }
    }

}
