package com.example.shoesapp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoesapp.R;
import com.example.shoesapp.adapters.userProductAdapter;
import com.example.shoesapp.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView userHome_rv;
    FirebaseFirestore db;
    boolean flag = false;
    ArrayList<ProductModel> datalist = new ArrayList<>();
    userProductAdapter adapter;
    SearchView searchView;


    public HomeFragment() {
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
    public void onPause() {
        super.onPause();
        flag = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        userHome_rv = view.findViewById(R.id.userHomeRecyclerView);
        searchView = view.findViewById(R.id.searchViewUserHome);
        db = FirebaseFirestore.getInstance();

        userHome_rv.setLayoutManager(new LinearLayoutManager(getContext()));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search_product(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search_product(newText);
                return false;
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
                            userHome_rv.setLayoutManager(layoutManager);
                            adapter = new userProductAdapter(getContext(),datalist);
                            userHome_rv.setHasFixedSize(true);
                            userHome_rv.setAdapter(adapter);

                        }
                    }
                });
    }

    private void search_product(String query) {

        if (query.isEmpty())
        {
            getdata();
        }
        else
        {
            datalist.clear();
            db.collection("Products")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                            {
                                String name = document.getString("categoryCompany");
                                if (name.contains(query))
                                {
                                    ProductModel data = document.toObject(ProductModel.class);
                                    datalist.add(data);
                                }
                            }
                            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                            userHome_rv.setLayoutManager(layoutManager);
                            adapter = new userProductAdapter(getContext(),datalist);
                            userHome_rv.setHasFixedSize(true);
                            userHome_rv.setAdapter(adapter);

                        }
                    });
            datalist.clear();
        }
    }

}