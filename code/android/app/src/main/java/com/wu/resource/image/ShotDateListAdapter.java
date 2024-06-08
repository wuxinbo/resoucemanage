package com.wu.resource.image;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wu.resource.R;
import com.wu.resource.databinding.ListSearchShotLayoutBinding;
import com.wu.resource.databinding.PhotoImageViewBinding;
import com.wu.resource.ui.search.SearchViewModel;

public class ShotDateListAdapter extends  RecyclerView.Adapter<ShotDateListAdapter.ViewHolder> {

  private SearchViewModel.ShotDateCount shotDateCount;
  private static final String TAG ="ShotDateListAdapter";
  private Context context;
  private GridLayoutManager gridLayoutManager;
  class ViewHolder extends RecyclerView.ViewHolder{
    private TextView textView ;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.textView = ListSearchShotLayoutBinding.bind(itemView).shotDateText;
    }
  }
  public ShotDateListAdapter(SearchViewModel.ShotDateCount shotDateCount,
                             Context context,
                             GridLayoutManager gridLayoutManager) {
    this.shotDateCount = shotDateCount;
    this.context =context;
    this.gridLayoutManager =gridLayoutManager;
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(context)
      .inflate(R.layout.list_search_shot_layout, viewGroup, false);
    ViewHolder viewHolder = new ViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull ShotDateListAdapter.ViewHolder viewHolder, int i) {
    String text =shotDateCount.getCategory().get(i);
    viewHolder.textView.setText(text);
  }


  @Override
  public int getItemCount() {
    return shotDateCount.getData().size();
  }
}
