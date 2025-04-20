package com.wuxinbo.resourcemanage.ui.search;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuxinbo.common.http.HttpUtil;
import com.wuxinbo.resourcemanage.Constant;

import java.util.List;

/**
 * 搜索页面
 */
public class SearchViewModel extends ViewModel {

  /**
   * 每天拍摄数量
   */
  private MutableLiveData<ShotDateCount> shotDateCount =new MutableLiveData<>();

  public MutableLiveData<ShotDateCount> getShotDateCount() {
    return shotDateCount;
  }

  public class ShotDateCount {
    /**
     * 类别
     */
    private List<String> category;
    /**
     * 数据
     */
    private List<Integer> data;

    public List<String> getCategory() {
      return category;
    }

    public void setCategory(List<String> category) {
      this.category = category;
    }

    public List<Integer> getData() {
      return data;
    }

    public void setData(List<Integer> data) {
      this.data = data;
    }
  }

  /**
   * 从后端获取数据
   */
  @RequiresApi(api = Build.VERSION_CODES.P)
  public void getShotDateCountInfoFromServer(){
    HttpUtil.getJson(Constant.URL +Constant.PHOTO_GROUP_BY_SHOT_TIME+"?group=4",(result)->{
      ShotDateCount shotDateCount = Constant.gson.fromJson(result, ShotDateCount.class);
      if (shotDateCount!=null){
        getShotDateCount().postValue(shotDateCount);
      }
    });
  }

}
