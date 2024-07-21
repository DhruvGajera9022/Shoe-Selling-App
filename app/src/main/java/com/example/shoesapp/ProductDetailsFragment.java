package com.example.shoesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.shoesapp.databinding.FragmentProductDetailsBinding;

public class ProductDetailsFragment extends Fragment {
//    FragmentProductDetailsBinding binding;
//    String imgUrl, name, price, description;

    public ProductDetailsFragment() {
    }

//    public ProductDetailsFragment(String imgUrl, String name, String price, String description) {
//        this.imgUrl = imgUrl;
//        this.name = name;
//        this.price = price;
//        this.description = description;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_product_details, container, false);
//        View view =  inflater.inflate(R.layout.fragment_product_details, container, false);

//        Glide.with(getContext()).load(imgUrl).into(binding.ProductDImage);
//        binding.ProductDName.setText(name);
//        binding.ProductDPrice.setText(price);
//        binding.ProductDDescription.setText(description);

    }
}