package com.wuxinbo.resourcemanage.ui.home;

import static com.wuxinbo.resourcemanage.Constant.PHOTO_LIST_BY_PAGE;
import static com.wuxinbo.resourcemanage.Constant.UPDATE;
import static com.wuxinbo.resourcemanage.Constant.gson;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuxinbo.common.http.HttpUtil;
import com.wuxinbo.resourcemanage.Constant;
import com.wuxinbo.resourcemanage.ResourceApplication;
import com.wuxinbo.resourcemanage.image.PhotoDao;
import com.wuxinbo.resourcemanage.image.PhotoInfo;
import com.wuxinbo.resourcemanage.image.PhotoResponse;

import java.util.ArrayList;
import java.util.List;
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
  private MutableLiveData<String> title ;

  private MutableLiveData<List<PhotoInfo>> selectPhotoInfos =new MutableLiveData(new ArrayList<>());
  /**
   * 启用选择模式，该模式下会出现选择框
   */
  private MutableLiveData<Boolean> enableSelect=new MutableLiveData<>(false);
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

  public MutableLiveData<List<PhotoInfo>> getSelectPhotoInfos() {
    return selectPhotoInfos;
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

  public MutableLiveData<Boolean> getEnableSelect(){
    return enableSelect;
  }

  /**
   * 更新最爱
   */
  public void like(Context context) {
    List<PhotoInfo> list = getSelectPhotoInfos().getValue();
    if (!list.isEmpty()){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        list =list.stream().map(it->{
          PhotoInfo photoInfo =new PhotoInfo();
          photoInfo.setMid(it.getMid());
          photoInfo.updateLike();
          return photoInfo;
        }).collect(Collectors.toList());
        HttpUtil.postJSON(Constant.URL + UPDATE,list, (result) -> {
          if (!result.isSuccess()){
            ((Activity)context).runOnUiThread(()->{
              Toast.makeText(context,"更新失败",Toast.LENGTH_SHORT).show();
            });
          }else{
            ((Activity)context).runOnUiThread(()->{
              Toast.makeText(context,"更新成功",Toast.LENGTH_SHORT).show();
//              get
            });
          }
//          List<PhotoInfo> all = photoDao.getAll();
//          if (all!=null&& !all.isEmpty()){
//            //清除缓存重新加载
//            all.stream().forEach(it->photoDao.delete(it));
//          }
//          photoDao.insertAll(content);
//          getPhotoData().postValue(content);
        });
      }
    }

  }
}
