package com.wu.resource;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.wu.resource.main.ShareListAdapter;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView ;
    private GridView gridView;

    private Button button;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView =findViewById(R.id.imageView);
        gridView =findViewById(R.id.imageGridView);
        popupWindow =new PopupWindow(MainActivity.this);
        initPopupWindow(popupWindow);
        button =findViewById(R.id.share);
        RecyclerView recyclerView = (RecyclerView) popupWindow.getContentView().findViewById(R.id.shareLayout);
        recyclerView.setAdapter(new ShareListAdapter());
        recyclerView.setLayoutManager(new GridLayoutManager(this,6));
        button.setOnClickListener((v)->{
            popupWindow.showAtLocation(gridView, Gravity.BOTTOM,0,0);
        });
        Glide.with(this).load("http://192.168.1.112:8080/photo/get?mid=2570").into(imageView);
    }

    /**
     *
      */
   private void initPopupWindow(PopupWindow popupWindow){
       popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.share_layout,null));
       popupWindow.setOutsideTouchable(true);
       popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.share_background,null));
       popupWindow.setEnterTransition(new AutoTransition());
       popupWindow.setExitTransition(new AutoTransition());
       popupWindow.setElevation(3);
   }

}