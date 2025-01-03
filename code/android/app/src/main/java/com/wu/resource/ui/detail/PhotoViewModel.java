package com.wu.resource.ui.detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wu.resource.image.PhotoInfo;

public class PhotoViewModel  extends ViewModel {

  private MutableLiveData<PhotoInfo> photoInfo =new MutableLiveData<>();


  public MutableLiveData<PhotoInfo> getPhotoInfo() {
    return photoInfo;
  }

}
