package com.wuxinbo.resourcemanage.image;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wuxinbo.resourcemanage.Constant;
import com.wuxinbo.resourcemanage.R;
import com.wuxinbo.resourcemanage.databinding.ListSearchShotLayoutBinding;
import com.wuxinbo.resourcemanage.ui.search.SearchViewModel;

public class ShotDateListAdapter extends  RecyclerView.Adapter<ShotDateListAdapter.ViewHolder> {

  private SearchViewModel.ShotDateCount shotDateCount;
  private static final String TAG ="ShotDateListAdapter";
  private Activity activity;
  private Fragment fragment;
  private GridLayoutManager gridLayoutManager;
  class ViewHolder extends RecyclerView.ViewHolder{
    private TextView textView ;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.textView = ListSearchShotLayoutBinding.bind(itemView).shotDateText;
    }
  }
  public ShotDateListAdapter(SearchViewModel.ShotDateCount shotDateCount,
                             Activity activity,
                             Fragment searchFragment,
                             GridLayoutManager gridLayoutManager) {
    this.shotDateCount = shotDateCount;
    this.fragment =searchFragment;
    this.activity =activity;
    this.gridLayoutManager =gridLayoutManager;
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(activity)
      .inflate(R.layout.list_search_shot_layout, viewGroup, false);
    ViewHolder viewHolder = new ViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull ShotDateListAdapter.ViewHolder viewHolder, int i) {
    String text =shotDateCount.getCategory().get(i);
    viewHolder.textView.setText(text);
    viewHolder.textView.setOnClickListener(v->{
      //回到首页
      Bundle arguments = new Bundle();
      arguments.putString(Constant.SHOT_DATE,text);
      arguments.putString(Constant.SEARCH_DATE,"true");
      NavController navController = Navigation.findNavController(activity, R.id.search_activity_fragment);
      navController.navigate(R.id.searchResultFragment,arguments);
    });
  }


  @Override
  public int getItemCount() {
    return shotDateCount.getData().size();
  }
}
