package com.wu.resource;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView ;
    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setActionBar();
        setContentView(R.layout.activity_main);
        imageView =findViewById(R.id.imageView);
        gridView =findViewById(R.id.imageGridView);
        gridView.addView(imageView);


        Glide.with(this).load("http://192.168.1.112:8080/photo/get?mid=2570").into(imageView);
    }
}