package com.wu.resource;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wu.resource.image.PhotoListAdapter;
import com.wu.resource.image.PhotoResponse;
import com.wu.resource.utils.HttpUtil;

public class MainActivity extends AppCompatActivity {

    private RecyclerView gridView;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.photoGrid);
        //查询数据
        HttpUtil.executorService.execute(() -> {
            HttpUtil.getJson("photo/listByPage", (result) -> {
                Message resMsg = new Message();
                resMsg.obj = result;
                Handler handler = Handler.createAsync(Looper.getMainLooper());
                handler.post(()-> {
                    //Snackbar.make(MainActivity.this, gridView,result, Snackbar.LENGTH_SHORT).show();
                    Gson gson = new GsonBuilder().create();
                    PhotoResponse photoResponse = gson.fromJson(result, PhotoResponse.class);
                    gridView.setAdapter(new PhotoListAdapter(photoResponse.getContent(),MainActivity.this));
                    gridView.setLayoutManager(new GridLayoutManager(this,4));

                });
            });
        });

    }


}