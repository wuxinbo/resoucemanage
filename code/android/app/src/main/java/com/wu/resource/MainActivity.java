package com.wu.resource;

import static com.wu.resource.Constant.gson;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.image.PhotoListAdapter;
import com.wu.resource.image.PhotoResponse;
import com.wu.common.http.HttpUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView gridView;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.photoGrid);
        //初始化相册数据
        ResourceApplication application = (ResourceApplication) getApplication();
        HttpUtil.executorService.execute(()->{
            Handler handler = Handler.createAsync(Looper.getMainLooper());
            List<PhotoInfo> list = application.getDb().photoDao().getAll();
            handler.post(() -> {
                gridView.setAdapter(new PhotoListAdapter(list, MainActivity.this));
                gridView.setLayoutManager(new GridLayoutManager(this, 4));

            });
        });
        //查询数据
        HttpUtil.executorService.execute(() -> {
            HttpUtil.getJson(Constant.URL + "photo/listByPage", (result) -> {
                PhotoResponse photoResponse = gson.fromJson(result, PhotoResponse.class);
                /**
                 *
                 */
                List<PhotoInfo> content = photoResponse.getContent();
                Handler handler = Handler.createAsync(Looper.getMainLooper());
                application.getDb().photoDao().insertAll(content);
                handler.post(() -> {
                    gridView.setAdapter(new PhotoListAdapter(content, MainActivity.this));
                    gridView.setLayoutManager(new GridLayoutManager(this, 4));

                });
            });
        });

    }


}