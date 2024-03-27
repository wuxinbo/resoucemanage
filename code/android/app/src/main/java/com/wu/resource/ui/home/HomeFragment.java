package com.wu.resource.ui.home;

import static com.wu.resource.Constant.DATE_FORMAT;
import static com.wu.resource.Constant.gson;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wu.common.http.HttpUtil;
import com.wu.resource.Constant;
import com.wu.resource.R;
import com.wu.resource.ResourceApplication;
import com.wu.resource.databinding.FragmentPicBinding;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.image.PhotoListAdapter;
import com.wu.resource.image.PhotoResponse;
import com.wu.sphinxsearch.NativeLib;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

  private FragmentPicBinding binding;
  private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
  private HomeViewModel homeViewModel;

  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentPicBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
    homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
//    NativeLib nativeLib = new NativeLib();
//    Log.i("native test", nativeLib.query("50mm", "photo"));

    //初始化相册数据
    ResourceApplication application = (ResourceApplication) getActivity().getApplication();
    homeViewModel.loadPhotoInfo(application);
    //更新页面
    Observer<List<PhotoInfo>> photoObs = photoInfos -> {
        Map<String, List<PhotoInfo>> collect = photoInfos.stream().
          collect(Collectors.groupingBy(it -> dateFormat.format(it.getShotTime())));
        binding.picLineLayout.removeAllViews();
        collect.forEach((key, value) -> {
          //初始化标题
          TextView dateTextView = new TextView(getActivity());
          dateTextView.setText(key);
//         设置内边距
          dateTextView.setPadding(20,40,0,40);
          binding.picLineLayout.addView(dateTextView);
          RecyclerView listView = new RecyclerView(getActivity());
          GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
          PhotoListAdapter photoListAdapter = new PhotoListAdapter(value, getContext(),gridLayoutManager);
          listView.setAdapter(photoListAdapter);
          listView.setLayoutManager(gridLayoutManager);
          binding.picLineLayout.addView(listView);
        });
    };
    homeViewModel.getPhotoData().observe(getViewLifecycleOwner(), photoObs);
    return root;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
