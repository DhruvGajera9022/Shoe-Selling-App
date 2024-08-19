package com.example.shoesapp;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.shoesapp.databinding.ActivityMainBinding;
import com.example.shoesapp.fragments.CategoryFragment;
import com.example.shoesapp.fragments.HomeFragment;
import com.example.shoesapp.fragments.OrderFragment;
import com.example.shoesapp.fragments.ProfileFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up data binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Load the selected language from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        String languageCode = preferences.getString("language", "en"); // Default to English

        // Apply the selected language
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Set up the bottom navigation view
        binding.bottomNavigationView.setBackground(null);

        // Handle navigation item selection
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.category) {
                replaceFragment(new CategoryFragment());
            } else if (itemId == R.id.order) {
                replaceFragment(new OrderFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });

        // Load the initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new HomeFragment())
                    .commit();
            binding.bottomNavigationView.setSelectedItemId(R.id.home);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.frameLayout);

        // Go back to HomeFragment from any other fragment
        if (currentFragment instanceof HomeFragment) {
            super.onBackPressed();  // If already on HomeFragment, handle normally
        } else {
            replaceFragment(new HomeFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.home); // Ensure the Home icon is selected
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
