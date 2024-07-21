package com.example.shoesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;

import com.example.shoesapp.databinding.ActivityAdminDashboardBinding;

public class AdminDashboardActivity extends AppCompatActivity {
    ActivityAdminDashboardBinding binding;
    ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new AdminHomeFragment());
        binding.adminBottomNavigationView.setBackground(null);

        binding.adminBottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.admin_home){
                replaceFragment(new AdminHomeFragment());
            } else if (itemId == R.id.admin_order){
                replaceFragment(new AdminOrderFragment());
            } else if (itemId == R.id.admin_profile){
                replaceFragment(new AdminHomeFragment());
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

public void replaceFragment(Fragment fragment){
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.adminFrameLayout, fragment);
    fragmentTransaction.commit();
}
}