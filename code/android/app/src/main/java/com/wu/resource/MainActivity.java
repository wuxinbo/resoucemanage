package com.wu.resource;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView ;
    private GridView gridView;

    private Button button;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        getActionBar().hide();
//        setActionBar();
        setContentView(R.layout.activity_main);
        imageView =findViewById(R.id.imageView);
        gridView =findViewById(R.id.imageGridView);

        popupWindow =new PopupWindow(MainActivity.this);
        popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.share_image_view,null));
//        gridView.addView(imageView);
        button =findViewById(R.id.share);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        button.setOnClickListener((v)->{
            popupWindow.showAtLocation(gridView, Gravity.BOTTOM,0,0);

        });
        Glide.with(this).load("http://192.168.1.112:8080/photo/get?mid=2570").into(imageView);
    }


}