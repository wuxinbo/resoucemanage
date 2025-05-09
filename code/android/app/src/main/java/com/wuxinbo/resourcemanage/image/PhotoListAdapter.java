package com.wuxinbo.resourcemanage.image;


import static com.wuxinbo.resourcemanage.Constant.gson;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wuxinbo.resourcemanage.Constant;
import com.wuxinbo.resourcemanage.R;
import com.wuxinbo.resourcemanage.databinding.PhotoImageViewBinding;
import com.wuxinbo.resourcemanage.image.PhotoInfo;
import com.wuxinbo.resourcemanage.ui.home.HomeViewModel;

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
            MutableLiveData<List<PhotoInfo>> selectPhotoInfos = homeViewModel.getSelectPhotoInfos();
            imageView.setOnClickListener((view) -> {
                //判断是进入详情还是进入多选模式
                if (homeViewModel.getEnableSelect().getValue()){
                    List<PhotoInfo> selectPhotos = selectPhotoInfos.getValue();
                    if (!bind.check.isChecked()){ //如果是选中则取消选中
                        selectPhotos.add(photoInfo);
                    }else{
                        selectPhotos.remove(photoInfo);
                    }
                    selectPhotoInfos.postValue(selectPhotos);
                    bind.check.toggle();
                    Log.i(TAG, "select photo size : "+ selectPhotos.size());
                }else{
                    Bundle data = new Bundle();
                    data.putString(Constant.URL_KEY, Constant.URL + "photo/get?mid=" + photoInfo.getMid());
                    data.putString(Constant.PHOTO_KEY, gson.toJson(photoInfo));
                    NavController navController = Navigation.findNavController((Activity) context, R.id.nav_bottom_home);
                    navController.navigate(R.id.photo_detail, data);
                }

            });
            //添加长按事件，长按弹出
            imageView.setLongClickable(true);
            imageView.setOnLongClickListener((v) -> {
                Log.i(TAG, "imageClick: ");
                bind.check.toggle();
                //默认选中当前的图片
                selectPhotoInfos.getValue().add(photoInfo);
                selectPhotoInfos.postValue(selectPhotoInfos.getValue());
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
        // 标记是否喜欢
        if (holder.photoInfo.getLike()==null){ //为空表示不喜欢
            binds.get(position).likeButton.setImageResource(R.drawable.like);
        }
        layoutParams.width = gridLayoutManager.getWidth() / gridLayoutManager.getSpanCount();
        layoutParams.height = layoutParams.width;
        Glide.with(context).load(Constant.URL + "photo/get?mid=" + holder.photoInfo.getMid()).
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
