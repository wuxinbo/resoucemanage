package com.wuxinbo.resourcemanage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.wuxinbo.resourcemanage.databinding.ActivitySearchBinding;


/**
 * 搜索页面
 */
public class SearchActivity extends AppCompatActivity {
  ActivitySearchBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivitySearchBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    //绑定返回按钮
    binding.searchBack.setOnClickListener(v -> finish());
    //不获取焦点
    binding.search.setFocusable(false);
    binding.search.setOnClickListener(v->{
      NavController navController = Navigation.findNavController(SearchActivity.this, R.id.search_activity_fragment);
      navController.navigate(R.id.searchFragment);
    });
  }
}
