package com.wuxinbo.resourcemanage.ui.detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuxinbo.resourcemanage.image.PhotoInfo;

public class PhotoViewModel  extends ViewModel {

  private MutableLiveData<PhotoInfo> photoInfo =new MutableLiveData<>();


  public MutableLiveData<PhotoInfo> getPhotoInfo() {
    return photoInfo;
  }

}
