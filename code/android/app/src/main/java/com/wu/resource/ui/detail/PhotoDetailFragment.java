package com.wu.resource.ui.detail;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.wu.common.download.DownloadTask;
import com.wu.common.http.HttpUtil;
import com.wu.resource.Constant;
import com.wu.resource.R;
import com.wu.resource.databinding.FragmentPhotoDetailBinding;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.ui.home.HomeViewModel;

import java.util.Arrays;
import java.util.List;

public class PhotoDetailFragment extends Fragment {

  private PhotoViewModel mViewModel;
  private FragmentPhotoDetailBinding binding;
  private PhotoInfo photoInfo;
  private HomeViewModel homeViewModel;
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    binding = FragmentPhotoDetailBinding.inflate(getLayoutInflater());
    photoInfo = Constant.gson.fromJson((String) getArguments().get(Constant.PHOTO_KEY), PhotoInfo.class);
    homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
    homeViewModel.getShowBottomNavView().postValue(false); //隐藏底部导航栏
    homeViewModel.getShowTopToolBar().postValue(true); //显示toolbar
//    binding.largeImage.set
    homeViewModel.getTitle().postValue(photoInfo.getSysFileStoreItem().getFileName()); //使用文件名作为标题
    //显示拍摄时间
    String url =(String) getArguments().get(Constant.URL_KEY);
    Glide.with(this).load(url)
      .into(binding.largeImage);
//    BitmapFactory.
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
    List<String> textList = Arrays.asList(photoInfo.getSpeed(), "ISO" + photoInfo.getIso(), photoInfo.getAperture(),
      photoInfo.getLens(), photoInfo.getFocusLength());
    View pre =null;
    for (int i = 0; i < textList.size(); i++) {
      pre =getTextView(binding.shotInfo, textList.get(i), pre, i);
    }
    addCameraLogo();
  }

  /**
   * 放入相机厂商logo
   */
  private void addCameraLogo() {
    //放入图标
    ImageView cameraLogo =new ImageView(getActivity());
    cameraLogo.setImageResource(R.drawable.nikon);
    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams
      (ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.rightMargin=10;
    layoutParams.rightToRight=R.id.shotInfo;
    layoutParams.topToTop =R.id.shotInfo;  //垂直居中
    layoutParams.bottomToBottom=R.id.shotInfo;
    cameraLogo.setLayoutParams(layoutParams);
    binding.shotInfo.addView(cameraLogo);
  }

  /**
   * 展示照片信息
   * @param viewGroup
   * @param text
   * @param preView
   * @param id
   * @return
   */
  private TextView getTextView(ViewGroup viewGroup, String text,View preView,int id) {
    if (text != null) {
      TextView view = new TextView(getActivity());
      view.setId(id);
      view.setText(text);
      ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams
        (ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
      if (preView!=null){
        layoutParams.leftToRight =preView.getId();
        layoutParams.leftMargin =10; //设置左边距
      }else{ //设置第一个元素的左边距
        layoutParams.leftMargin =25;
        layoutParams.leftToLeft =R.id.shotInfo;
      }
      layoutParams.topToTop =R.id.shotInfo;
      layoutParams.bottomToBottom=R.id.shotInfo;
      view.setLayoutParams(layoutParams);
      view.setTextSize(12);
      viewGroup.addView(view);
      return view;
    }
    return null;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
    // TODO: Use the ViewModel
  }

}
