package com.wu.resource.ui.detail;

import static android.widget.LinearLayout.HORIZONTAL;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.wu.common.download.DownloadTask;
import com.wu.common.http.HttpUtil;
import com.wu.resource.Constant;
import com.wu.resource.databinding.FragmentPhotoDetailBinding;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.ui.home.HomeViewModel;

public class PhotoDetailFragment extends Fragment {

  private PhotoViewModel mViewModel;
  private FragmentPhotoDetailBinding binding;
  private PhotoInfo photoInfo;

  private HomeViewModel homeViewModel;
  public static PhotoDetailFragment newInstance() {
    return new PhotoDetailFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = FragmentPhotoDetailBinding.inflate(getLayoutInflater());
    photoInfo = Constant.gson.fromJson((String) getArguments().get(Constant.PHOTO_KEY), PhotoInfo.class);
    homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
    homeViewModel.getShowBottomNavView().postValue(false); //隐藏底部导航栏
    homeViewModel.getShowTopToolBar().postValue(true); //显示toolbar
    //显示拍摄时间
    String url =(String) getArguments().get(Constant.URL_KEY);
    Glide.with(this).load(url).into(binding.largeImage);
    assmbleShotInfo();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      downloadFile(url);
    }
    return binding.getRoot();

  }
  @RequiresApi(api = Build.VERSION_CODES.Q)
  private void downloadFile(String url) {
    if (url != null && !url.equals("")) {
      HttpUtil.executorService.execute(() -> {
        DownloadTask.download(url, getActivity(), photoInfo.getSysFileStoreItem().getFileName());
      });
    }
  }

  /**
   * 设置并显示拍摄信息
   */
  private void assmbleShotInfo() {
    //初始化线性布局
    LinearLayout shotInfoTextLayout = new LinearLayout(getActivity());
    shotInfoTextLayout.setOrientation(HORIZONTAL);
    binding.shotInfo.addView(shotInfoTextLayout);
    getTextView(shotInfoTextLayout, photoInfo.getSpeed()); //快门速度
    if (photoInfo.getIso() != null) { //ISO
      getTextView(shotInfoTextLayout, "ISO" + photoInfo.getIso().toString());
    }
    getTextView(shotInfoTextLayout, photoInfo.getAperture());//光圈 信息
    getTextView(shotInfoTextLayout, photoInfo.getLens()); //镜头
    getTextView(shotInfoTextLayout, photoInfo.getFocusLength()); //焦距
    //放入图标
//    ImageView cameraLogo =new ImageView(this);
//    cameraLogo.setImageResource(R.drawable.nikon);
//    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(cameraLogo.getWidth(),cameraLogo.getHeight());
//    layoutParams.rightMargin=10;
//    cameraLogo.setLayoutParams(layoutParams);
//    binding.shotInfo.addView(cameraLogo);
  }

  private void getTextView(ViewGroup viewGroup, String text) {
    if (text != null) {
      TextView view = new TextView(getActivity());
      view.setText(text);
      view.setTextSize(12);
      view.setPadding(10, 0, 0, 0);
      viewGroup.addView(view);
    }
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
    // TODO: Use the ViewModel
  }

}
