package com.wu.resource.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wu.resource.Constant;
import com.wu.resource.databinding.FragmentSearchResultBinding;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.image.PhotoListAdapter;
import com.wu.resource.ui.home.HomeViewModel;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class SearchResultFragment extends Fragment {

  private FragmentSearchResultBinding binding;
  private HomeViewModel homeViewModel;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding =  FragmentSearchResultBinding.inflate(inflater,container,false);
    SearchResultViewModel searchResultViewModel = new ViewModelProvider(this).get(SearchResultViewModel.class);
    homeViewModel =new ViewModelProvider(this).get(HomeViewModel.class);
    String shotDate = (String) getArguments().get(Constant.SHOT_DATE);
    searchResultViewModel.queryPhotoInfoByShotDate(Arrays.asList(shotDate));
    //数据页面绑定
    searchResultViewModel.getPhotoData().observe(getViewLifecycleOwner(),getListObserver());
    return binding.getRoot();
  }


  /**
   * 监听照片信息
   * @return
   */

  private Observer<List<PhotoInfo>> getListObserver() {
    Observer<List<PhotoInfo>> photoObs = photoInfos -> {
      binding.shotDateInfo.removeAllViews();
      RecyclerView listView = new RecyclerView(getActivity());
      GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
      PhotoListAdapter photoListAdapter = new PhotoListAdapter(photoInfos,getActivity(),
        homeViewModel,
        gridLayoutManager);
      listView.setAdapter(photoListAdapter);
      listView.setLayoutManager(gridLayoutManager);
      binding.shotDateInfo.addView(listView);

    };
    return photoObs;
  }
}
