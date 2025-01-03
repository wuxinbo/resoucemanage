package com.wu.resource.ui.search;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wu.resource.R;
import com.wu.resource.databinding.FragmentSearchBinding;
import com.wu.resource.image.ShotDateListAdapter;
import com.wu.resource.ui.home.HomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

  private SearchViewModel searchViewModel;

  private FragmentSearchBinding binding;
  private final String TAG="SearchFragment";
  private int defaultSpanCount =4;
  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment SearchFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static SearchFragment newInstance() {
    SearchFragment fragment = new SearchFragment();

    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }
  private void initShotDateInfo(SearchViewModel.ShotDateCount shotDateCount){
    binding.shotDateInfo.removeAllViews();
    RecyclerView recyclerView =new RecyclerView(getActivity());
    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), defaultSpanCount);
    ShotDateListAdapter shotDateListAdapter = new ShotDateListAdapter(shotDateCount,getActivity(),this,
      gridLayoutManager);
    recyclerView.setAdapter(shotDateListAdapter);
    recyclerView.setLayoutManager(gridLayoutManager);
    gridLayoutManager.setSmoothScrollbarEnabled(true);
    ConstraintLayout.LayoutParams layoutParams =new ConstraintLayout.LayoutParams(MATCH_PARENT,
      WRAP_CONTENT);
    recyclerView.setLayoutParams(layoutParams);
    recyclerView.setClipToPadding(false);
    //绑定搜索框监听
    binding.shotDateInfo.addView(recyclerView);
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding =FragmentSearchBinding.inflate(inflater,container,false);
    searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      searchViewModel.getShotDateCountInfoFromServer();
    }
    Log.i(TAG,"oncreateView");
    searchViewModel.getShotDateCount().observe(getViewLifecycleOwner(), (shotDateCount) -> {
      initShotDateInfo(shotDateCount);
    });
    return binding.getRoot();
  }
}
