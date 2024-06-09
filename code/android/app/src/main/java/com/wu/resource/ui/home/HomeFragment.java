package com.wu.resource.ui.home;

import static com.wu.resource.Constant.DATE_FORMAT;
import static com.wu.resource.Constant.gson;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wu.common.http.HttpUtil;
import com.wu.resource.Constant;
import com.wu.resource.R;
import com.wu.resource.ResourceApplication;
import com.wu.resource.databinding.ActivityHomeBinding;
import com.wu.resource.databinding.FragmentPicBinding;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.image.PhotoListAdapter;
import com.wu.resource.image.PhotoResponse;
import com.wu.resource.ui.detail.PhotoViewModel;
import com.wu.sphinxsearch.NativeLib;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {
  private static final String TAG = "HomeFragment";
  private FragmentPicBinding binding;
  private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
  private HomeViewModel homeViewModel;

  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentPicBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
    //下拉刷新
    refreshPhoto();
    homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
     //显示底部导航栏
     homeViewModel.getShowBottomNavView().postValue(true);
    //初始化相册数据
    ResourceApplication application = (ResourceApplication) getActivity().getApplication();
    Bundle arguments = getArguments();
    if (arguments!=null){ //加载查询条件
      String shotDate = (String) arguments.get(Constant.SHOT_DATE);
      homeViewModel.queryPhotoInfoByShotDate(Arrays.asList(shotDate));
    }else{
      homeViewModel.loadPhotoInfo(application);
    }
    binding.search.setOnFocusChangeListener((v,hasfocues) -> {
      if (hasfocues){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_bottom_home);
        navController.navigate(R.id.search_fragment);
      }
    });
    //更新页面
    Observer<List<PhotoInfo>> photoObs = getListObserver();
    homeViewModel.getPhotoData().observe(getViewLifecycleOwner(), photoObs);
    homeViewModel.getShowTopToolBar().postValue(false); //隐藏toolbar
    return root;
  }

  /**
   * 更新照片列表数据
   * @return
   */
  @NonNull
  private Observer<List<PhotoInfo>> getListObserver() {
    Observer<List<PhotoInfo>> photoObs = photoInfos -> {
      Map<String, List<PhotoInfo>> datamap = photoInfos.stream().
        collect(Collectors.groupingBy(it -> dateFormat.format(it.getShotTime())));
      binding.picLineLayout.removeAllViews();
      Set<String> days = datamap.keySet();
      //按照拍摄时间倒叙进行排序
      days.stream().sorted(((s, t1) -> {
        try {
          return (dateFormat.parse(t1).compareTo(dateFormat.parse(s)));
        } catch (ParseException e) {
        }
        return 0;
      })).forEach(day -> {
        //初始化标题
        TextView dateTextView = new TextView(getActivity());
//         设置内边距
        dateTextView.setPadding(20, 40, 0, 40);
        RecyclerView listView = new RecyclerView(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        PhotoListAdapter photoListAdapter = new PhotoListAdapter(datamap.get(day),getActivity(),
                 gridLayoutManager);
        listView.setAdapter(photoListAdapter);
        listView.setLayoutManager(gridLayoutManager);
        //增加数量显示
        dateTextView.setText(day+"("+photoListAdapter.getItemCount()+")");
        Log.i(TAG, dateTextView.getText().toString());

        binding.picLineLayout.addView(dateTextView);
        binding.picLineLayout.addView(listView);
      });

    };
    return photoObs;
  }

  private void refreshPhoto(){
    binding.refresh.setOnRefreshListener(()->{
      homeViewModel.loadPhotoInfo((ResourceApplication) getActivity().getApplication());
      binding.refresh.setRefreshing(false);
      Toast.makeText(getActivity(),"数据已刷新",Toast.LENGTH_LONG).show();

    });
  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}
