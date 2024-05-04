package com.wu.resource;

import static android.widget.LinearLayout.HORIZONTAL;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.AutoTransition;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wu.common.download.DownloadTask;
import com.wu.common.http.HttpUtil;
import com.wu.resource.databinding.ActivityHomeBinding;
import com.wu.resource.databinding.ActivityResourceDetailBinding;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.image.ShareListAdapter;

import java.text.SimpleDateFormat;
import java.util.Set;

public class ResourceDetailActivity extends AppCompatActivity {

  private PopupWindow popupWindow;
  private static final String TAG="resourceDetail";
  private PhotoInfo photoInfo;
  private String url;
  private ActivityResourceDetailBinding binding;

  @RequiresApi(api = Build.VERSION_CODES.Q)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityResourceDetailBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    popupWindow = new PopupWindow(this);
    initPopupWindow(popupWindow);
    String photoJson = getIntent().getStringExtra("photo");
    photoInfo = Constant.gson.fromJson(photoJson, PhotoInfo.class);
    //显示拍摄时间
    binding.materialToolbar.setTitle(photoInfo.getSysFileStoreItem().getFileName());
    binding.materialToolbar.setNavigationIcon(R.drawable.back);
    binding.materialToolbar.setNavigationOnClickListener((v)->{

      Log.i(TAG,"click");
    });
//    binding.materialToolbar.set
    setSupportActionBar(binding.materialToolbar);

    url = getIntent().getStringExtra("url");
    Glide.with(this).load(url).into(binding.largeImage);
    assmbleShotInfo();
//    RecyclerView recyclerView = (RecyclerView) popupWindow.getContentView().findViewById(R.id.shareLayout);
//    recyclerView.setAdapter(new ShareListAdapter());
//    recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

//    binding.largeImage.setOnClickListener(v -> {
//      if (!popupWindow.isShowing()) {
//        popupWindow.showAtLocation(binding.largeImage, Gravity.BOTTOM, 0, 0);
//      } else {
//        popupWindow.dismiss();
//      }
//    });
    downloadFile();
  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  private void downloadFile() {
    if (url != null && !url.equals("")) {
      HttpUtil.executorService.execute(() -> {
        DownloadTask.download(url, this, photoInfo.getSysFileStoreItem().getFileName());
      });
    }
  }

  /**
   * 设置并显示拍摄信息
   */
  private void assmbleShotInfo(){
    //初始化线性布局
    LinearLayout shotInfoTextLayout =new LinearLayout(this);
    shotInfoTextLayout.setOrientation(HORIZONTAL);
    binding.shotInfo.addView(shotInfoTextLayout);
    getTextView(shotInfoTextLayout,photoInfo.getSpeed()); //快门速度
    if (photoInfo.getIso()!=null){ //ISO
      getTextView(shotInfoTextLayout,"ISO"+photoInfo.getIso().toString());
    }
    getTextView(shotInfoTextLayout,photoInfo.getAperture());//光圈 信息
    getTextView(shotInfoTextLayout,photoInfo.getLens()); //镜头
    getTextView(shotInfoTextLayout,photoInfo.getFocusLength()); //焦距
    //放入图标
//    ImageView cameraLogo =new ImageView(this);
//    cameraLogo.setImageResource(R.drawable.nikon);
//    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(cameraLogo.getWidth(),cameraLogo.getHeight());
//    layoutParams.rightMargin=10;
//    cameraLogo.setLayoutParams(layoutParams);
//    binding.shotInfo.addView(cameraLogo);
  }
  private void getTextView(ViewGroup viewGroup,String text){
    if (text!=null) {
      TextView view =new TextView(this);
      view.setText(text);
      view.setTextSize(12);
      view.setPadding(10,0,0,0);
      viewGroup.addView(view);
    }
  }
  /**
   *
   */
  private void initPopupWindow(PopupWindow popupWindow) {
    popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.share_layout, null));
    popupWindow.setOutsideTouchable(true);
    popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.share_background, null));
    AutoTransition autoTransition = new AutoTransition();
    autoTransition.setDuration(400);
    popupWindow.setEnterTransition(autoTransition);
    popupWindow.setExitTransition(autoTransition);
    popupWindow.setElevation(20);
  }
}
