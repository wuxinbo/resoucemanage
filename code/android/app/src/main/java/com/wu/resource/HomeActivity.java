package com.wu.resource;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.wu.resource.databinding.ActivityHomeBinding;
import com.wu.resource.ui.home.HomeViewModel;


public class HomeActivity extends AppCompatActivity {

  private static final String TAG = "HomeActivity";
  private ActivityHomeBinding binding;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    binding = ActivityHomeBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    //系统栏后布置您的应用
    binding.navView.setVisibility(View.VISIBLE);
    NavController navController = Navigation.findNavController(this, R.id.nav_bottom_home);
    AppBarConfiguration appBarConfiguration =
      new AppBarConfiguration.Builder(navController.getGraph()).build();
    //头部toolbar
    NavigationUI.setupWithNavController(binding.materialToolbar, navController,appBarConfiguration);
    //底部导航栏
    NavigationUI.setupWithNavController(binding.navView, navController);
    homeViewModel.getShowBottomNavView().observe(this,show ->{
      if (show){
        binding.navView.setVisibility(View.VISIBLE);
      }else{
        binding.navView.setVisibility(View.GONE);
      }
    });
    //顶部工具栏
    homeViewModel.getShowTopToolBar().observe(this,show->{
      if (show){
        binding.materialToolbar.setVisibility(View.VISIBLE);
      }else{
        binding.materialToolbar.setVisibility(View.GONE);
      }
    });
    //设置标题
    homeViewModel.getTitle().observe(this,title-> binding.materialToolbar.setTitle(title));
  }

}
