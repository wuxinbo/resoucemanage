package com.wu.resource;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.AutoTransition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.wu.common.download.DownloadTask;
import com.wu.common.http.HttpUtil;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.image.ShareListAdapter;

import java.text.SimpleDateFormat;
import java.util.Set;

public class ResourceDetailActivity extends AppCompatActivity {

    private PopupWindow popupWindow;
    private ImageView imageView ;
    private Toolbar toolbar;
    private PhotoInfo photoInfo;
    private String url;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_detail);
        imageView =findViewById(R.id.largeImage);
        popupWindow =new PopupWindow(this);
        initPopupWindow(popupWindow);
        String photoJson = getIntent().getStringExtra("photo");
        photoInfo = Constant.gson.fromJson(photoJson, PhotoInfo.class);
        toolbar =findViewById(R.id.materialToolbar);
        //显示拍摄时间
        toolbar.setTitle(photoInfo.getSysFileStoreItem().getFileName());
        setSupportActionBar(toolbar);
        url = getIntent().getStringExtra("url");
        Glide.with(this).load(url).into(imageView);
        RecyclerView recyclerView = (RecyclerView) popupWindow.getContentView().findViewById(R.id.shareLayout);
        recyclerView.setAdapter(new ShareListAdapter());
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        imageView.setOnClickListener(v->{
            if (!popupWindow.isShowing()) {
                popupWindow.showAtLocation(imageView, Gravity.BOTTOM,0,0);
            }
        });
        downloadFile();
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void downloadFile(){
        if (url!=null&&!url.equals("")){
            HttpUtil.executorService.execute(()->{
                DownloadTask.download(url,this,photoInfo.getSysFileStoreItem().getFileName());
            });
        }
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