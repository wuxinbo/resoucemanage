package com.wu.resource.ui.home;

import static com.wu.resource.Constant.PHOTO_LIST_BY_PAGE;
import static com.wu.resource.Constant.gson;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;

import com.wu.common.http.HttpUtil;
import com.wu.resource.Constant;
import com.wu.resource.ResourceApplication;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.image.PhotoListAdapter;
import com.wu.resource.image.PhotoResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeViewModel extends ViewModel {

  /**
   * 照片数据
   */
  private final MutableLiveData<List<PhotoInfo>> photoData;
  /**
   * 底部导航栏隐藏显示
   */
  private  MutableLiveData<Boolean> showBottomNavView;
  /**
   * 顶部导航栏隐藏
   */
  private MutableLiveData<Boolean> showTopToolBar;
  public HomeViewModel() {
    this.photoData = new MutableLiveData<>();
    this.showBottomNavView =new MutableLiveData<>();
    this.showTopToolBar =new MutableLiveData<>(false);
  }

  public void loadPhotoInfoFromDb(ResourceApplication application) {
    HttpUtil.executorService.execute(() -> {
      List<PhotoInfo> list = application.getDb().photoDao().getAll();
      //从后台线程中发起消息通知
      getPhotoData().postValue(list);
      queryPhotoInfo(application);
    });
  }

  private void queryPhotoInfo(ResourceApplication application) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      HttpUtil.getJson(Constant.URL + PHOTO_LIST_BY_PAGE, (result) -> {
        PhotoResponse photoResponse = gson.fromJson(result, PhotoResponse.class);
        List<PhotoInfo> content = photoResponse.getContent();
        application.getDb().photoDao().insertAll(content);
        getPhotoData().postValue(content);
      });
    }
  }

  /**
   * 加载照片数据。
   *
   * @param application
   */
  public void loadPhotoInfo(ResourceApplication application) {
    loadPhotoInfoFromDb(application);
  }


  public MutableLiveData<List<PhotoInfo>> getPhotoData() {
    return photoData;
  }

  public MutableLiveData<Boolean> getShowBottomNavView(){
    return showBottomNavView;
  }
  public MutableLiveData<Boolean> getShowTopToolBar(){
    return showTopToolBar;
  }
}
