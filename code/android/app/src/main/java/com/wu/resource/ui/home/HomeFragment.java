package com.wu.resource.ui.home;

import static com.wu.resource.Constant.DATE_FORMAT;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wu.resource.R;
import com.wu.resource.ResourceApplication;
import com.wu.resource.SearchActivity;
import com.wu.resource.databinding.FragmentPicBinding;
import com.wu.resource.databinding.PhotoImageViewBinding;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.image.PhotoListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FragmentPicBinding binding;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private HomeViewModel homeViewModel;
    private List<PhotoListAdapter> photoListAdapters = new ArrayList<>();

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
        homeViewModel.loadPhotoInfo(application);
        binding.search.setFocusable(false);
        //绑定监听，进入搜索页面
        binding.search.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), SearchActivity.class);
                    startActivity(intent);
                }
        );

        //更新页面
        Observer<List<PhotoInfo>> photoObs = getListObserver();
        homeViewModel.getPhotoData().observe(getViewLifecycleOwner(), photoObs);
        homeViewModel.getShowTopToolBar().postValue(false); //隐藏toolbar
        //监听是否启用选择全部
        homeViewModel.getEnableSelect().observe(getViewLifecycleOwner(), observeEnableSelect());
        homeViewModel.getSelectPhotoInfos().observe(getViewLifecycleOwner(), (list) -> {
            binding.selectCount.setText("已选择" + list.size()+"项");

        });
        //标记最爱的图片

        binding.like.setClickable(true);
        binding.like.setOnClickListener(v->{
            homeViewModel.like(getActivity());
        });
        //取消选中
        binding.cancle.setOnClickListener((v) -> {
            homeViewModel.getEnableSelect().postValue(false);
        });
        return root;
    }


    /**
     * 选择全部监听
     *
     * @return
     */
    private Observer<Boolean> observeEnableSelect() {
        return (enable) -> {
            if (enable) { //进入选中模式
//                binding.selectCount.setVisibility(View.VISIBLE);
                binding.search.setVisibility(View.INVISIBLE);
                binding.selectLayout.setVisibility(View.VISIBLE);
                //显示全部的checkbox
                for (PhotoListAdapter photoListAdapter : photoListAdapters) {
                    for (PhotoImageViewBinding bind : photoListAdapter.getBinds()) {
                        bind.check.setVisibility(View.VISIBLE);
                    }
                }
            } else { //取消选中
                binding.search.setVisibility(View.VISIBLE);
                binding.selectLayout.setVisibility(View.GONE);
                //隐藏cheeckbox
                for (PhotoListAdapter photoListAdapter : photoListAdapters) {
                    for (PhotoImageViewBinding bind : photoListAdapter.getBinds()) {
                        if (bind.check.isChecked()){
                            bind.check.toggle();
                        }
                        bind.check.setVisibility(View.GONE);
                    }
                }
                homeViewModel.getSelectPhotoInfos().postValue(new ArrayList<>());
            }

        };
    }

    private void initPopupWindow() {
        PopupWindow popupWindow = new PopupWindow(this.getActivity());
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.share_background, null));
        popupWindow.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.share_layout, null, false));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setEnterTransition(new Slide());
        popupWindow.showAtLocation(binding.getRoot(), Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(() -> {
            homeViewModel.getShowBottomNavView().postValue(true);
        });
    }

    /**
     * 更新照片列表数据
     *
     * @return
     */
    @NonNull
    private Observer<List<PhotoInfo>> getListObserver() {
        Observer<List<PhotoInfo>> photoObs = photoInfos -> {
            Map<String, List<PhotoInfo>> datamap = photoInfos.stream().
                    collect(Collectors.groupingBy(it -> dateFormat.format(it.getShotTime())));
            binding.picLineLayout.removeAllViews();
            photoListAdapters.clear();
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
                PhotoListAdapter photoListAdapter = new PhotoListAdapter(datamap.get(day), getActivity(),
                        homeViewModel,
                        gridLayoutManager);
                listView.setAdapter(photoListAdapter);
                listView.setLayoutManager(gridLayoutManager);
                //增加数量显示
                dateTextView.setText(day + "(" + photoListAdapter.getItemCount() + ")");
                Log.i(TAG, dateTextView.getText().toString());
                photoListAdapters.add(photoListAdapter);
                binding.picLineLayout.addView(dateTextView);
                binding.picLineLayout.addView(listView);
            });

        };
        return photoObs;
    }

    private void refreshPhoto() {
        binding.refresh.setOnRefreshListener(() -> {
            homeViewModel.loadPhotoInfo((ResourceApplication) getActivity().getApplication());
            binding.refresh.setRefreshing(false);
            Toast.makeText(getActivity(), "数据已刷新", Toast.LENGTH_LONG).show();

        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
