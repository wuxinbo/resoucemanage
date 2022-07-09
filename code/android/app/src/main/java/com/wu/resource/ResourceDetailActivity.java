package com.wu.resource;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.wu.resource.image.ShareListAdapter;

public class ResourceDetailActivity extends AppCompatActivity {

    private PopupWindow popupWindow;
    private ImageView imageView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_detail);
        imageView =findViewById(R.id.largeImage);
        popupWindow =new PopupWindow(this);
        initPopupWindow(popupWindow);
        String url = getIntent().getStringExtra("url");
        Glide.with(this).load(url).into(imageView);
        RecyclerView recyclerView = (RecyclerView) popupWindow.getContentView().findViewById(R.id.shareLayout);
        recyclerView.setAdapter(new ShareListAdapter());
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        imageView.setOnClickListener(v->{
            if (!popupWindow.isShowing()) {
                popupWindow.showAtLocation(imageView, Gravity.BOTTOM,0,0);

            }
        });
    }
    /**
     *
     */
    private void initPopupWindow(PopupWindow popupWindow){
        popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.share_layout,null));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.share_background,null));
        AutoTransition autoTransition =new AutoTransition();
        autoTransition.setDuration(400);
        popupWindow.setEnterTransition(autoTransition);
        popupWindow.setExitTransition(autoTransition);
        popupWindow.setElevation(20);
    }
}