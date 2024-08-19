package com.example.shoesapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesapp.CartActivity;
import com.example.shoesapp.R;
import com.example.shoesapp.adapters.userProductAdapter;
import com.example.shoesapp.models.ProductModel;
import com.example.shoesapp.profileactivities.EditProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
public class HomeFragment extends Fragment {


    RecyclerView userHome_rv;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    boolean flag = false;
    ArrayList<ProductModel> datalist = new ArrayList<>();
    userProductAdapter adapter;
    SearchView searchView;
    TextView txtTopUsername;
    ImageView imageViewTop;
    String uid;
    ImageView cart;
//    TextView cartItemCount;
    ProgressBar progressBar;  // Declare ProgressBar

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!flag) {
            getdata();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        flag = true;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        userHome_rv = view.findViewById(R.id.userHomeRecyclerView);
        searchView = view.findViewById(R.id.searchViewUserHome);
        progressBar = view.findViewById(R.id.progressBar);  // Initialize ProgressBar

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        txtTopUsername = view.findViewById(R.id.txtTopUsername);
        cart = view.findViewById(R.id.cart);
        imageViewTop = view.findViewById(R.id.imageTop);

        uid = mAuth.getCurrentUser().getUid();

        userHome_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        getTopData();

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CartActivity.class);
                startActivity(i);
            }
        });
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

        imageViewTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });


        return view;
    }

    private void getdata() {
        progressBar.setVisibility(View.VISIBLE);  // Show ProgressBar
        datalist.clear();
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);  // Hide ProgressBar
                        if (task.isSuccessful()) {
                            List<ProductModel> data = task.getResult().toObjects(ProductModel.class);
                            datalist.addAll(data);

                            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                            userHome_rv.setLayoutManager(layoutManager);
                            adapter = new userProductAdapter(getContext(), datalist);
                            userHome_rv.setHasFixedSize(true);
                            userHome_rv.setAdapter(adapter);
                        }
                    }
                });
    }

    private void search_product(String query) {
        progressBar.setVisibility(View.VISIBLE);  // Show ProgressBar

        if (query.isEmpty()) {
            getdata();
        } else {
            datalist.clear();
            db.collection("Products")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            progressBar.setVisibility(View.GONE);  // Hide ProgressBar
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String name = document.getString("name");
                                if (name.contains(query)) {
                                    ProductModel data = document.toObject(ProductModel.class);
                                    datalist.add(data);
                                }
                            }
                            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                            userHome_rv.setLayoutManager(layoutManager);
                            adapter = new userProductAdapter(getContext(), datalist);
                            adapter.notifyDataSetChanged();
                            userHome_rv.setHasFixedSize(true);
                            userHome_rv.setAdapter(adapter);
                        }
                    });
            datalist.clear();
        }
    }

    public void getTopData() {
        DocumentReference reference = db.collection("Users").document(uid);

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    txtTopUsername.setText("Hey, " + value.getString("FirstName"));
                    Picasso.get().load(value.getString("ProfileImage")).into(imageViewTop);
                }
            }
        });
    }
}
