package com.wu.resource.image;


import static com.wu.resource.Constant.gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wu.resource.Constant;
import com.wu.resource.R;
import com.wu.resource.databinding.PhotoImageViewBinding;
import com.wu.resource.ui.detail.PhotoViewModel;
import com.wu.resource.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片适配器
 */
public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {
    private List<PhotoInfo> items;
    private Context context;
    private GridLayoutManager gridLayoutManager;
    private final String TAG = "PhotoListAdapter";
    private HomeViewModel homeViewModel;
    private List<PhotoImageViewBinding> binds =new ArrayList<>();

    public List<PhotoImageViewBinding> getBinds() {
        return binds;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private PhotoInfo photoInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PhotoImageViewBinding bind =PhotoImageViewBinding.bind(itemView);
            imageView = bind.photoImage;
            //不显示checkbox
            bind.check.setVisibility(View.GONE);
            //禁用点击事件，使用图片的点击
            bind.check.setClickable(false);
            binds.add(bind);
            imageView.setOnClickListener((view) -> {
                if (homeViewModel.getEnableSelect().getValue()){
                    List<PhotoInfo> selectPhotos = homeViewModel.getSelectPhotoInfos().getValue();
                    if (!bind.check.isChecked()){ //如果是选中则取消选中
                        selectPhotos.add(photoInfo);
                    }else{
                        selectPhotos.remove(photoInfo);
                    }
                    homeViewModel.getSelectPhotoInfos().postValue(selectPhotos);
                    bind.check.toggle();
                    Log.i(TAG, "select photo size : "+ selectPhotos.size());
                }else{
                    Bundle data = new Bundle();
                    data.putString(Constant.URL_KEY, Constant.URL + "/photo/get?mid=" + photoInfo.getMid());
                    data.putString(Constant.PHOTO_KEY, gson.toJson(photoInfo));
                    NavController navController = Navigation.findNavController((Activity) context, R.id.nav_bottom_home);
                    navController.navigate(R.id.photo_detail, data);
                }
//                Intent intent = new Intent();

            });
            imageView.setLongClickable(true);
            imageView.setOnLongClickListener((v) -> {
                Log.i(TAG, "imageClick: ");
                homeViewModel.getEnableSelect().postValue(true);
                return true;
            });
        }
    }

    public PhotoListAdapter(List<PhotoInfo> items,
                            Context context,
                            HomeViewModel homeViewModel,
                            GridLayoutManager gridLayoutManager) {
        this.items = items;
        this.context = context;
        this.homeViewModel = homeViewModel;
        this.gridLayoutManager = gridLayoutManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_image_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.photoInfo = items.get(position);
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();

        layoutParams.width = gridLayoutManager.getWidth() / gridLayoutManager.getSpanCount();
        layoutParams.height = layoutParams.width;
        Glide.with(context).load(Constant.URL + "/photo/get?mid=" + holder.photoInfo.getMid()).
                //图片裁切
                        centerCrop().
                override(holder.imageView.getWidth(), holder.imageView.getHeight()).
                into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
