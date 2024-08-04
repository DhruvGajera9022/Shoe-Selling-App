package com.example.shoesapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoesapp.R;
import com.example.shoesapp.adapters.AdminProductCategoryAdapter;
import com.example.shoesapp.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    RecyclerView rv;
    CardView cardnike,cardadidas,cardpuma,cardrebook,cardcrocs,cardbata,cardredtape;
    ArrayList<ProductModel> datalistcate = new ArrayList<>();
    FirebaseFirestore dbcate;
    AdminProductCategoryAdapter adapter;
    String data = "";

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        cardnike = view.findViewById(R.id.nike);
        cardadidas = view.findViewById(R.id.adidas);
        cardpuma = view.findViewById(R.id.puma);
        cardrebook = view.findViewById(R.id.rebook);
        cardcrocs = view.findViewById(R.id.crocs);
        cardbata = view.findViewById(R.id.bata);
        cardredtape = view.findViewById(R.id.redtape);

        rv = view.findViewById(R.id.cate_rv);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        dbcate = FirebaseFirestore.getInstance();

        getcatedata(data);

        cardnike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcatedata("Nike");
            }
        });
        cardadidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcatedata("Adidas");
            }
        });
        cardpuma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcatedata("Puma");
            }
        });
        cardrebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcatedata("Rebook");
            }
        });
        cardcrocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcatedata("Crocs");
            }
        });
        cardbata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcatedata("Bata");
            }
        });
        cardredtape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcatedata("Red Tape");
            }
        });

        return view;
    }
    private void getcatedata(String data) {

        if (data.isEmpty())
        {
            datalistcate.clear();
            dbcate.collection("Products")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                            {
                                List<ProductModel> catedata = task.getResult().toObjects(ProductModel.class);
                                datalistcate.addAll(catedata);
                            }
                            adapter = new AdminProductCategoryAdapter(getContext(),datalistcate);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                            rv.setLayoutManager(gridLayoutManager);
                            rv.setAdapter(adapter);
                        }
                    });
            return;
        }

        datalistcate.clear();
        dbcate.collection("Products")
                .whereEqualTo("categoryCompany",data)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<ProductModel> catedata = task.getResult().toObjects(ProductModel.class);
                            datalistcate.addAll(catedata);
                        }
                        adapter = new AdminProductCategoryAdapter(getContext(),datalistcate);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                        rv.setLayoutManager(gridLayoutManager);
                        rv.setAdapter(adapter);
                    }
                });
    }
}