package com.wu.resource.ui.home;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;

import com.wu.common.http.HttpUtil;
import com.wu.resource.ResourceApplication;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.image.PhotoListAdapter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeViewModel extends ViewModel {

  /**
   * 照片数据
   */
  private final MutableLiveData<List<PhotoInfo>> photoData;

  public HomeViewModel() {
    this.photoData = new MutableLiveData<>();
  }

  public void loadPhotoInfoFromDb(ResourceApplication application) {
    HttpUtil.executorService.execute(() -> {
      List<PhotoInfo> list = application.getDb().photoDao().getAll();
      //从后台线程中发起消息通知
      getPhotoData().postValue(list);
    });
  }

  /**
   * 加载照片数据。
   * @param application
   */
  public void loadPhotoInfo(ResourceApplication application) {
    loadPhotoInfoFromDb(application);
  }


  public MutableLiveData<List<PhotoInfo>> getPhotoData() {
    return photoData;
  }

}
