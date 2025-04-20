package com.wuxinbo.resourcemanage.ui.search;

import static com.wuxinbo.resourcemanage.Constant.PHOTO_LIST_BY_SHOTDATE;
import static com.wuxinbo.resourcemanage.Constant.gson;

import android.os.Build;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.reflect.TypeToken;
import com.wuxinbo.common.http.HttpUtil;
import com.wuxinbo.resourcemanage.Constant;
import com.wuxinbo.resourcemanage.image.PhotoInfo;

import java.util.List;


public class SearchResultViewModel extends ViewModel {

  private final String TAG="SearchResultViewModel";
  /**
   * 照片数据
   */
  private final MutableLiveData<List<PhotoInfo>> photoData;

  public SearchResultViewModel() {
    this.photoData = new MutableLiveData<>();
  }

  public MutableLiveData<List<PhotoInfo>> getPhotoData() {
    return photoData;
  }

  /**
   * 按天查询照片
   * @param shotDates
   */
  public void queryPhotoInfoByShotDate(List<String> shotDates){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      HttpUtil.getJson(Constant.URL + PHOTO_LIST_BY_SHOTDATE+"?shotDate="+shotDates.get(0), result->{
        try {
          List<PhotoInfo> content = gson.fromJson(result, new TypeToken<List<PhotoInfo>>(){}.getType());
          this.photoData.postValue(content);
        }catch (Exception e){
          Log.e(TAG,"json parseException",e);
        }

      });
    }
  }}
