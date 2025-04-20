package com.wuxinbo.resourcemanage.image;

import static com.wuxinbo.resourcemanage.image.ShareListAdapter.shareInfo.Type.WX;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuxinbo.resourcemanage.R;
import com.wuxinbo.resourcemanage.databinding.ShareImageViewBinding;

import java.util.ArrayList;
import java.util.List;

public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.ViewHolder> {
    private static final List<shareInfo> items=new ArrayList<>();
    static {
        items.add(new shareInfo(R.drawable.icon64_appwx_logo,R.string.share_wx_freind, WX.value));
        items.add(new shareInfo(R.drawable.icon_res_download_moments,R.string.share_wx_freind, WX.value));
        items.add(new shareInfo(R.drawable.icon_res_download_collect,R.string.share_wx_collect, WX.value));
    }

    static class shareInfo{
        int resourceId;
        int  labelId ;
        int type;

        public enum Type{
            /**
             * 微信
             */
            WX(1),
            /**
             * 抖音
             */
            DOUYING(2),
            ;
            int value ;

            Type(int value) {
                this.value = value;
            }
        }

        public shareInfo(int resourceId, int label, int type) {
            this.resourceId = resourceId;
            this.labelId = label;
            this.type = type;
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView ;
        private TextView textView ;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ShareImageViewBinding bind = ShareImageViewBinding.bind(itemView);
            imageView = bind.imageView2;
            textView = bind.lable;
            imageView.setOnClickListener((view )->{
                Toast.makeText(imageView.getContext(), "hello",Toast.LENGTH_SHORT).show();

            });
            imageView.setBackgroundResource(R.drawable.share_item_selector);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.share_image_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView .setImageResource(items.get(position).resourceId);
        holder.textView.setText(items.get(position).labelId);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
