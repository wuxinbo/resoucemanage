package com.wu.resource;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.wu.resource.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

  private ActivityHomeBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityHomeBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    //系统栏后布置您的应用
    WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    View decorView = getWindow().getDecorView();
    NavController navController = Navigation.findNavController(this, R.id.nav_bottom_home);
    NavigationUI.setupWithNavController(binding.navView, navController);
  }

}
