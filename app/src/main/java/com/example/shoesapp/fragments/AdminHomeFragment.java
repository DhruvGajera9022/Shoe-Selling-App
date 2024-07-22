package com.example.shoesapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.shoesapp.adapters.ProductAdapter;
import com.example.shoesapp.R;
import com.example.shoesapp.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminHomeFragment extends Fragment {
    RecyclerView home_rv;
    SwipeRefreshLayout refreshLayout;
    FirebaseFirestore db;
    boolean flag = false;
    ArrayList<ProductModel> datalist = new ArrayList<>();
    ProductAdapter productAdapter;
    FloatingActionButton floatingActionButton;
    Uri imguri;
    ImageView temp_img;
    String sel_com;
    StorageReference storageReference,imageName;
    SearchView searchView;


    public AdminHomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        home_rv = view.findViewById(R.id.home_rv);
        db = FirebaseFirestore.getInstance();
        refreshLayout = view.findViewById(R.id.refreshhome);
        searchView = view.findViewById(R.id.searchView2);

        home_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
                .setContentHolder(new ViewHolder(R.layout.add_product))
                .setExpanded(true,1500)
                .setCancelable(false)
                .create();
        View dailogview = dialogPlus.getHolderView();


        ImageView img = dailogview.findViewById(R.id.addProductImage);
        EditText name = dailogview.findViewById(R.id.addNewProductName);
        EditText price = dailogview.findViewById(R.id.addNewProductPrice);
        EditText desc = dailogview.findViewById(R.id.addNewProductDescription);
        Button save = dailogview.findViewById(R.id.productSaveButton);
        Button cancel = dailogview.findViewById(R.id.productCancelButton);
        Spinner spin = dailogview.findViewById(R.id.addProductCategory);



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

        temp_img = img;

        floatingActionButton = view.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] category = {"Male", "Female", "Kids"};
                ArrayAdapter<String> arrayAdapter;
                arrayAdapter = new ArrayAdapter<String>(dialogPlus.getHolderView().getContext(), android.R.layout.simple_spinner_dropdown_item, category);
                spin.setAdapter(arrayAdapter);

                dialogPlus.show();

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 100);
                    }
                });

                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sel_com = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                    });

                    if (save == null) {
                        Log.e("AdminHomeFragment", "Save button is null");
                    } else {
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("Uploading...");
                                progressDialog.show();

                                SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.UK);
                                Date now = new Date();
                                String filename;
                                filename = format.format(now);

                                storageReference = FirebaseStorage.getInstance().getReference(filename);
                                imageName = storageReference;

                                storageReference.putFile(imguri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String fiuri = uri.toString();
                                                        String mname = name.getText().toString();
                                                        String mprice = price.getText().toString();
                                                        String mdesc = desc.getText().toString();
                                                        String mcategory = sel_com;
                                                        String uid = db.collection("Products").document().getId();

                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("name", mname);
                                                        map.put("price", mprice);
                                                        map.put("description", mdesc);
                                                        map.put("category", mcategory);
                                                        map.put("imgurl", fiuri);
                                                        map.put("pid", uid);

                                                    db.collection("Products")
                                                            .document(uid)
                                                            .set(map)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    progressDialog.dismiss();
                                                                    dialogPlus.dismiss();

                                                                    name.setText("");
                                                                    price.setText("");
                                                                    desc.setText("");
                                                                    img.setImageResource(R.drawable.image_icon);

                                                                    getdata();
                                                                }
                                                            });

                                                }
                                            });
                                        }
                                    });
                        }
                    });
                }


                if (cancel == null) {
                    Log.e("AdminHomeFragment", "Cancel button is null");
                } else {
                    // Set an OnClickListener on the cancel button
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            // Clear text fields
//                            name.setText("");
//                            price.setText("");
//                            desc.setText("");
//                            // Reset image to default icon
//                            img.setImageResource(R.drawable.image_icon);
                            // Dismiss the dialog
                            dialogPlus.dismiss();
                        }
                    });
                }
            }
            });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String s = searchView.getQuery().toString();
                if (s.isEmpty())
                {
                    getdata();
                }
                else
                {
                    search_product(s);
                }

                refreshLayout.setRefreshing(false);
            }
        });

        return view;
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
                                String name = document.getString("name");
                                if (name.contains(query))
                                {
                                    ProductModel data = document.toObject(ProductModel.class);
                                    datalist.add(data);
                                }
                            }
                            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                            home_rv.setLayoutManager(layoutManager);
                            productAdapter = new ProductAdapter(getContext(),datalist);
                            home_rv.setHasFixedSize(true);
                            home_rv.setAdapter(productAdapter);

                        }
                    });
            datalist.clear();
        }
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

//                            productAdapter = new ProductAdapter(getContext(),datalist);
//                            home_rv.setAdapter(productAdapter);

                            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                            home_rv.setLayoutManager(layoutManager);
                            productAdapter = new ProductAdapter(getContext(),datalist);
                            home_rv.setHasFixedSize(true);
                            home_rv.setAdapter(productAdapter);

                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null)
        {
            imguri = data.getData();
            temp_img.setImageURI(imguri);
        }
    }


}