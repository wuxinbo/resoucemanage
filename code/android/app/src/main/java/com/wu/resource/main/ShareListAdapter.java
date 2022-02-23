package com.wu.resource.main;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wu.resource.R;

import java.util.ArrayList;
import java.util.List;

public class ShareListAdapter extends RecyclerView.Adapter<ShareListAdapter.ViewHolder> {
    private static final List<Integer> items=new ArrayList<>();
    static {
        items.add(R.drawable.icon64_appwx_logo);
        items.add(R.drawable.icon_res_download_moments);
        items.add(R.drawable.icon_res_download_collect);
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView ;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView =itemView.findViewById(R.id.imageView2);
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
        holder.imageView .setImageResource(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
