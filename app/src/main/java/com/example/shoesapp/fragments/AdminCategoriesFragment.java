package com.example.shoesapp.fragments;

import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shoesapp.adapters.AdminProductCategoryAdapter;
import com.example.shoesapp.R;
import com.example.shoesapp.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminCategoriesFragment extends Fragment {
    RecyclerView category_rv;
    SwipeRefreshLayout refreshLayout;
    FirebaseFirestore db;
    boolean flag = false;
    ArrayList<ProductModel> dataList = new ArrayList<>();
    AdminProductCategoryAdapter adapter;
    ImageButton imageButtonFilter;
    BottomSheetDialog dialog;


    public AdminCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!flag)
        {
            getData();
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
        imageButtonFilter = view.findViewById(R.id.filterImageButton);

        category_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                refreshLayout.setRefreshing(false);
            }
        });

        dialog = new BottomSheetDialog(getContext());
        createDialog();

        imageButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return view;

    }

    private void getData() {
        dataList.clear();
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<ProductModel> data = task.getResult().toObjects(ProductModel.class);
                            dataList.addAll(data);

                            GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
                            category_rv.setLayoutManager(layoutManager);
                            adapter = new AdminProductCategoryAdapter(getContext(),dataList);

                            category_rv.setHasFixedSize(true);
                            category_rv.setAdapter(adapter);

                        }
                    }
                });
    }

    private void getDataCategoryGender(String category) {
        dataList.clear();
        db.collection("Products")
                .whereEqualTo("categoryGender", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<ProductModel> data = task.getResult().toObjects(ProductModel.class);
                            dataList.addAll(data);

                            GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
                            category_rv.setLayoutManager(layoutManager);
                            adapter = new AdminProductCategoryAdapter(getContext(),dataList);

                            category_rv.setHasFixedSize(true);
                            category_rv.setAdapter(adapter);

                            dialog.dismiss();

                        }
                    }
                });
    }

    public void createDialog(){
        View view = getLayoutInflater().inflate(R.layout.category_filter_list, null, false);

        TextView maleCategoryFilter = view.findViewById(R.id.maleCategoryFilter);
        TextView femaleCategoryFilter = view.findViewById(R.id.femaleCategoryFilter);
        TextView kidsCategoryFilter = view.findViewById(R.id.kidsCategoryFilter);

        maleCategoryFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataCategoryGender("Male");
            }
        });

        femaleCategoryFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataCategoryGender("Female");
            }
        });

        kidsCategoryFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataCategoryGender("Kids");
            }
        });

        dialog.setContentView(view);
    }


}