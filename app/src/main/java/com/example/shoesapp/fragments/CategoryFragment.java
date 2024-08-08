package com.example.shoesapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoesapp.R;
import com.example.shoesapp.adapters.AdminProductCategoryAdapter;
import com.example.shoesapp.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private RecyclerView rv;
    private CardView cardnike, cardadidas, cardpuma, cardrebook, cardcrocs, cardbata, cardredtape;
    private ArrayList<ProductModel> datalistcate = new ArrayList<>();
    private FirebaseFirestore dbcate;
    private AdminProductCategoryAdapter adapter;
    private String data = "";
    private ImageView btnFilter;
    private Chip chipMale, chipFemale, chipKids, chipNike, chipAdidas, chipPuma, chipRebook, chipCrocs, chipBata, chipRedTape, chipLess1000, chip1000To1500, chip1500To3000, chip3000To5000, chip5000To7000, chipAbove7000;
    private BottomSheetDialog dialog;
    private MaterialButton btnApply;
    private ArrayList<String> selectedGenderData;
    private ArrayList<String> selectedCompanyData;
    private ArrayList<String> selectedPriceData;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        selectedGenderData = new ArrayList<>();
        selectedCompanyData = new ArrayList<>();
        selectedPriceData = new ArrayList<>();

        initViews(view);
        setupRecyclerView(view);
        setupFirestore();
        setupFilterDialog();

        return view;
    }

    private void initViews(View view) {
        cardnike = view.findViewById(R.id.nike);
        cardadidas = view.findViewById(R.id.adidas);
        cardpuma = view.findViewById(R.id.puma);
        cardrebook = view.findViewById(R.id.rebook);
        cardcrocs = view.findViewById(R.id.crocs);
        cardbata = view.findViewById(R.id.bata);
        cardredtape = view.findViewById(R.id.redtape);
        btnFilter = view.findViewById(R.id.btnFilter);

        cardnike.setOnClickListener(v -> getcatedata("Nike"));
        cardadidas.setOnClickListener(v -> getcatedata("Adidas"));
        cardpuma.setOnClickListener(v -> getcatedata("Puma"));
        cardrebook.setOnClickListener(v -> getcatedata("Reebok"));
        cardcrocs.setOnClickListener(v -> getcatedata("Crocs"));
        cardbata.setOnClickListener(v -> getcatedata("Bata"));
        cardredtape.setOnClickListener(v -> getcatedata("Red Tape"));

        btnFilter.setOnClickListener(v -> dialog.show());
    }

    private void setupRecyclerView(View view) {
        rv = view.findViewById(R.id.cate_rv);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void setupFirestore() {
        dbcate = FirebaseFirestore.getInstance();
        getcatedata(data);
    }

    private void getcatedata(String category) {
        Query query = dbcate.collection("Products");

        if (!category.isEmpty()) {
            query = query.whereEqualTo("categoryCompany", category);
        }

        // Only add gender filter if selected
        if (!selectedGenderData.isEmpty()) {
            query = query.whereIn("categoryGender", selectedGenderData);
        }

        // Only add company filter if selected
        if (!selectedCompanyData.isEmpty()) {
            query = query.whereIn("categoryCompany", selectedCompanyData);
        }

        if (!selectedPriceData.isEmpty()) {
            List<Task<QuerySnapshot>> priceRangeTasks = new ArrayList<>();

            for (String priceRange : selectedPriceData) {
                Query priceQuery = applyPriceFilter(query, priceRange);
                priceRangeTasks.add(priceQuery.get());
            }

            Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(priceRangeTasks);

            allTasks.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    datalistcate.clear();
                    for (QuerySnapshot snapshot : task.getResult()) {
                        if (snapshot != null && !snapshot.isEmpty()) {
                            List<ProductModel> products = snapshot.toObjects(ProductModel.class);
                            datalistcate.addAll(products);
                        }
                    }
                    updateRecyclerView();
                } else {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // No price filter selected, just execute the base query
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    datalistcate.clear();
                    if (task.getResult() != null) {
                        List<ProductModel> catedata = task.getResult().toObjects(ProductModel.class);
                        datalistcate.addAll(catedata);
                    }
                    updateRecyclerView();
                } else {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Query applyPriceFilter(Query query, String priceRange) {
        switch (priceRange) {
            case "Less than 1000":
                return query.whereLessThan("price", 1000);
            case "1000 - 1500":
                return query.whereGreaterThanOrEqualTo("price", 1000)
                        .whereLessThan("price", 1500);
            case "1500 - 3000":
                return query.whereGreaterThanOrEqualTo("price", 1500)
                        .whereLessThan("price", 3000);
            case "3000 - 5000":
                return query.whereGreaterThanOrEqualTo("price", 3000)
                        .whereLessThan("price", 5000);
            case "5000 - 7000":
                return query.whereGreaterThanOrEqualTo("price", 5000)
                        .whereLessThan("price", 7000);
            case "Above 7000":
                return query.whereGreaterThanOrEqualTo("price", 7000);
            default:
                return query;
        }
    }



    private void updateRecyclerView() {
        adapter = new AdminProductCategoryAdapter(getContext(), datalistcate);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(adapter);
    }

    private void setupFilterDialog() {
        dialog = new BottomSheetDialog(getContext());
        View view = getLayoutInflater().inflate(R.layout.category_filter_list, null);

        chipMale = view.findViewById(R.id.chipMale);
        chipFemale = view.findViewById(R.id.chipFemale);
        chipKids = view.findViewById(R.id.chipKids);
        chipNike = view.findViewById(R.id.chipNike);
        chipAdidas = view.findViewById(R.id.chipAdidas);
        chipPuma = view.findViewById(R.id.chipPuma);
        chipRebook = view.findViewById(R.id.chipRebook);
        chipCrocs = view.findViewById(R.id.chipCrocs);
        chipBata = view.findViewById(R.id.chipBata);
        chipRedTape = view.findViewById(R.id.chipRedTape);
        btnApply = view.findViewById(R.id.applyBtn);
        chipLess1000 = view.findViewById(R.id.chipLess1000);
        chip1000To1500 = view.findViewById(R.id.chip1000To1500);
        chip1500To3000 = view.findViewById(R.id.chip1500To3000);
        chip3000To5000 = view.findViewById(R.id.chip3000To5000);
        chip5000To7000 = view.findViewById(R.id.chip5000To7000);
        chipAbove7000 = view.findViewById(R.id.chipAbove7000);

        selectedGenderData = new ArrayList<>();
        selectedCompanyData = new ArrayList<>();
        selectedPriceData = new ArrayList<>();

        CompoundButton.OnCheckedChangeListener genderCheckedChangeListener = (buttonView, isChecked) -> {
            if (isChecked) {
                selectedGenderData.add(buttonView.getText().toString());
            } else {
                selectedGenderData.remove(buttonView.getText().toString());
            }
        };

        CompoundButton.OnCheckedChangeListener companyCheckedChangeListener = (buttonView, isChecked) -> {
            if (isChecked) {
                selectedCompanyData.add(buttonView.getText().toString());
            } else {
                selectedCompanyData.remove(buttonView.getText().toString());
            }
        };

        CompoundButton.OnCheckedChangeListener priceCheckedChangeListener = (buttonView, isChecked) -> {
            if (isChecked) {
                selectedPriceData.add(buttonView.getText().toString());
            } else {
                selectedPriceData.remove(buttonView.getText().toString());
            }
        };

        chipMale.setOnCheckedChangeListener(genderCheckedChangeListener);
        chipFemale.setOnCheckedChangeListener(genderCheckedChangeListener);
        chipKids.setOnCheckedChangeListener(genderCheckedChangeListener);

        chipNike.setOnCheckedChangeListener(companyCheckedChangeListener);
        chipAdidas.setOnCheckedChangeListener(companyCheckedChangeListener);
        chipPuma.setOnCheckedChangeListener(companyCheckedChangeListener);
        chipRebook.setOnCheckedChangeListener(companyCheckedChangeListener);
        chipCrocs.setOnCheckedChangeListener(companyCheckedChangeListener);
        chipBata.setOnCheckedChangeListener(companyCheckedChangeListener);
        chipRedTape.setOnCheckedChangeListener(companyCheckedChangeListener);

        chipLess1000.setOnCheckedChangeListener(priceCheckedChangeListener);
        chip1000To1500.setOnCheckedChangeListener(priceCheckedChangeListener);
        chip1500To3000.setOnCheckedChangeListener(priceCheckedChangeListener);
        chip3000To5000.setOnCheckedChangeListener(priceCheckedChangeListener);
        chip5000To7000.setOnCheckedChangeListener(priceCheckedChangeListener);
        chipAbove7000.setOnCheckedChangeListener(priceCheckedChangeListener);

        btnApply.setOnClickListener(v -> {
            dialog.dismiss();
            getcatedata(data); // Refresh data with selected filters
            Toast.makeText(getContext(), "Applied", Toast.LENGTH_SHORT).show();
        });

        dialog.setContentView(view);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}
