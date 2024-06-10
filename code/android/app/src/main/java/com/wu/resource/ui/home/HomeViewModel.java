package com.wu.resource.ui.home;

import static com.wu.resource.Constant.PHOTO_LIST_BY_PAGE;
import static com.wu.resource.Constant.PHOTO_LIST_BY_SHOTDATE;
import static com.wu.resource.Constant.gson;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.wu.common.http.HttpUtil;
import com.wu.resource.Constant;
import com.wu.resource.ResourceApplication;
import com.wu.resource.image.PhotoDao;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.image.PhotoListAdapter;
import com.wu.resource.image.PhotoResponse;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeViewModel extends ViewModel {

  private final String TAG="HomeViewModel";
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
  /**
   * 标题
   */
  private MutableLiveData<String> title;

  public HomeViewModel() {
    this.photoData = new MutableLiveData<>();
    this.showBottomNavView =new MutableLiveData<>();
    this.showTopToolBar =new MutableLiveData<>(false);
    this.title =new MutableLiveData<>();
  }

  public void loadPhotoInfoFromDb(ResourceApplication application) {
    HttpUtil.executorService.execute(() -> {
      List<PhotoInfo> list = application.getDb().photoDao().getAll();
      //从后台线程中发起消息通知
      getPhotoData().postValue(list);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      queryPhotoInfo(application);
    });
  }

  /**
   * 查询首页照片信息
   * @param application
   */
  private void queryPhotoInfo(ResourceApplication application) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      HttpUtil.getJson(Constant.URL + PHOTO_LIST_BY_PAGE, (result) -> {
        PhotoResponse photoResponse = gson.fromJson(result, PhotoResponse.class);
        List<PhotoInfo> content = photoResponse.getContent();
        //删除数据
        PhotoDao photoDao = application.getDb().photoDao();
        List<PhotoInfo> all = photoDao.getAll();
        if (all!=null&& !all.isEmpty()){
          //清除缓存重新加载
          all.stream().forEach(it->photoDao.delete(it));
        }
        photoDao.insertAll(content);
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
  public MutableLiveData<String> getTitle(){
    return title;
  }
}
