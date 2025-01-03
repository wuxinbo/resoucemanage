package com.wu.common.http;


import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtil {

    public static OkHttpClient httpClient = new OkHttpClient();

    /**
     * 默认线程池
     */
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static Gson gson = new GsonBuilder().setDateFormat(TIME_FORMAT).create();
    /**
     * 数据查询
     *
     * @param url
     * @param callBack
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void getJson(String url, HttpCallBack<String> callBack) {
        executorService.execute(() -> {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try (Response response = httpClient.newCall(request).execute()) {
                String result = response.body().string();
                Log.i("http request Url", url);
                Log.i("http response json", result);
                callBack.onResponse(result);
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }
        });

    }

    /**
     * 发送post请求
     * @param url
     * @param data
     * @param callBack
     */
    public static <T> void  postJSON(String url, Object data, HttpCallBack<Result<T>> callBack) {
        executorService.execute(() -> {
            Request request = new Request.Builder()
                    .url(url).post(RequestBody.create(MediaType.parse("application/json"),
                            gson.toJson(data)))
                    .build();
            Log.i("http request Url", url);
            Log.i("http request body", gson.toJson(data));
            try (Response response = httpClient.newCall(request).execute()) {
                String result = response.body().string();

                Log.i("http response json", result);
                callBack.onResponse(gson.fromJson(result, Result.class));
            } catch (Exception e) {
                Log.e("IOException", e.getMessage());
                callBack.onResponse(Result.err("请求失败"));
            }
        });

    }

    /**
     * 使用http 文件下载
     *
     * @param url
     * @param callBack
     */
    public static void downloadFile(String url, HttpCallBack<InputStream> callBack) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            ResponseBody body = response.body();
            InputStream inputStream = body.byteStream();
            callBack.onResponse(inputStream);
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }
    }

}
