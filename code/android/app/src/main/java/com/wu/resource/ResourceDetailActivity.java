package com.wu.resource;

import static android.widget.LinearLayout.HORIZONTAL;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;

import android.app.ActionBar;
import android.app.Activity;
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
import com.wu.resource.ui.detail.PhotoViewModel;

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
    PhotoViewModel photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
    setContentView(binding.getRoot());
    PhotoInfo value = photoViewModel.getPhotoInfo().getValue();
    popupWindow = new PopupWindow(this);
    initPopupWindow(popupWindow);
    NavController navController = Navigation.findNavController(this, R.id.nav_bottom_home);

    String photoJson = savedInstanceState.getString("photo");
    //显示拍摄时间
    binding.materialToolbar.setTitle(photoInfo.getSysFileStoreItem().getFileName());
    binding.materialToolbar.setNavigationIcon(R.drawable.back);
//    binding.materialToolbar.
    //绑定返回按钮
    binding.materialToolbar.setNavigationOnClickListener((v)->{
      finish();
    });

    url = getIntent().getStringExtra("url");

//    RecyclerView recyclerView = (RecyclerView) popupWindow.getContentView().findViewById(R.id.shareLayout);
//    recyclerView.setAdapter(new ShareListAdapter());
//    recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

    binding.largeImage.setOnClickListener(v -> {
//      if (!popupWindow.isShowing()) {
//        popupWindow.showAtLocation(binding.largeImage, Gravity.BOTTOM, 0, 0);
//      } else {
//        popupWindow.dismiss();
//      }
    });
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
