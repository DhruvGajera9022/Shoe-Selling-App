package com.example.shoesapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoesapp.adapters.AdminProductCategoryAdapter;
import com.example.shoesapp.adapters.ProductAdapter;
import com.example.shoesapp.R;
import com.example.shoesapp.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminCategoriesFragment extends Fragment {
    RecyclerView category_rv;
    SwipeRefreshLayout refreshLayout;
    FirebaseFirestore db;
    boolean flag = false;
    ArrayList<ProductModel> datalist = new ArrayList<>();
    AdminProductCategoryAdapter adapter;
    Chip chMale, chFemale, chKids;


    public AdminCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!flag)
        {
            getdata();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_category, container, false);

        category_rv = view.findViewById(R.id.categoryRecyclerView);
        refreshLayout = view.findViewById(R.id.refreshCategory);
        db = FirebaseFirestore.getInstance();
        chMale = view.findViewById(R.id.chipMale);
        chFemale = view.findViewById(R.id.chipFemale);
        chKids = view.findViewById(R.id.chipKids);

        category_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata();
                refreshLayout.setRefreshing(false);
            }
        });
        return view;

    }

    private void getdata() {
        datalist.clear();
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<ProductModel> data = task.getResult().toObjects(ProductModel.class);
                            datalist.addAll(data);

                            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                            category_rv.setLayoutManager(layoutManager);
                            adapter = new AdminProductCategoryAdapter(getContext(),datalist);
                            category_rv.setHasFixedSize(true);
                            category_rv.setAdapter(adapter);

                        }
                    }
                });
    }

    public void getChipData(){
        if (chMale.isSelected()){
            datalist.clear();
            db.collection("Products")
                    .whereEqualTo("category", "Male")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                            {
                                List<ProductModel> data = task.getResult().toObjects(ProductModel.class);
                                datalist.addAll(data);

                                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                                category_rv.setLayoutManager(layoutManager);
                                adapter = new AdminProductCategoryAdapter(getContext(),datalist);
                                category_rv.setHasFixedSize(true);
                                category_rv.setAdapter(adapter);

                            }
                        }
                    });
        } else if (chFemale.isSelected()) {
            datalist.clear();
            db.collection("Products")
                    .whereEqualTo("category", "Female")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                            {
                                List<ProductModel> data = task.getResult().toObjects(ProductModel.class);
                                datalist.addAll(data);

                                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                                category_rv.setLayoutManager(layoutManager);
                                adapter = new AdminProductCategoryAdapter(getContext(),datalist);
                                category_rv.setHasFixedSize(true);
                                category_rv.setAdapter(adapter);

                            }
                        }
                    });
        } else if (chKids.isSelected()) {
            datalist.clear();
            db.collection("Products")
                    .whereEqualTo("category", "Kids")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                            {
                                List<ProductModel> data = task.getResult().toObjects(ProductModel.class);
                                datalist.addAll(data);

                                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                                category_rv.setLayoutManager(layoutManager);
                                adapter = new AdminProductCategoryAdapter(getContext(),datalist);
                                category_rv.setHasFixedSize(true);
                                category_rv.setAdapter(adapter);

                            }
                        }
                    });
        } else {
            getdata();
        }
    }

}