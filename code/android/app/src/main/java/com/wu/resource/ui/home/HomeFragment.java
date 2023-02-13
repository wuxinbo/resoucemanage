package com.wu.resource.ui.home;

import static com.wu.resource.Constant.gson;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wu.common.http.HttpUtil;
import com.wu.resource.Constant;
import com.wu.resource.R;
import com.wu.resource.ResourceApplication;
import com.wu.resource.databinding.FragmentHomeBinding;
import com.wu.resource.image.PhotoInfo;
import com.wu.resource.image.PhotoListAdapter;
import com.wu.resource.image.PhotoResponse;
import com.wu.sphinxsearch.NativeLib;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView gridView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        NativeLib nativeLib =new NativeLib();
        Log.i("native test",nativeLib.query("50mm","photo"));
        //setContentView(R.layout.activity_main);
        gridView = root.findViewById(R.id.photoGrid);
        //初始化相册数据
        ResourceApplication application = (ResourceApplication) getActivity().getApplication();
        HttpUtil.executorService.execute(()->{
            Handler handler = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                handler = Handler.createAsync(Looper.getMainLooper());
            }
            List<PhotoInfo> list = application.getDb().photoDao().getAll();
            handler.post(() -> {
                gridView.setAdapter(new PhotoListAdapter(list, getContext()));
                gridView.setLayoutManager(new GridLayoutManager(getContext(), 4));

            });
        });
        //查询数据
        HttpUtil.executorService.execute(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                HttpUtil.getJson(Constant.URL + "photo/listByPage", (result) -> {
                    PhotoResponse photoResponse = gson.fromJson(result, PhotoResponse.class);
                    /**
                     *
                     */
                    List<PhotoInfo> content = photoResponse.getContent();
                    Handler handler = Handler.createAsync(Looper.getMainLooper());
                    application.getDb().photoDao().insertAll(content);
                    handler.post(() -> {
                        gridView.setAdapter(new PhotoListAdapter(content, getContext()));
                        gridView.setLayoutManager(new GridLayoutManager(getContext(), 4));

                    });
                });
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}