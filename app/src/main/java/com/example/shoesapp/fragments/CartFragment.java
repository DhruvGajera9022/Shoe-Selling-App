package com.example.shoesapp.fragments;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shoesapp.R;
import com.example.shoesapp.adapters.MyCartAdapter;
import com.example.shoesapp.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartFragment extends Fragment {

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private MyCartAdapter adapter;
    private ArrayList<MyCartModel> list;

    private MyCartModel deletedItem = null;

    private TextView txtProductTotal, txtProductDeliveryTotal, txtProduceVoucherTotal, txtProductFinalTotal;
    private MaterialButton btnCheckout;

    private String strTotalAmount;
    private int totalAmount;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        initialize(view);
        loadCartItems();

        return view;
    }

    private void initialize(View view) {
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        txtProductTotal = view.findViewById(R.id.cartProductTotal);
        txtProductDeliveryTotal = view.findViewById(R.id.cartProductDeliveryTotal);
        txtProduceVoucherTotal = view.findViewById(R.id.cartProductVoucherTotal);
        txtProductFinalTotal = view.findViewById(R.id.cartProductFinalTotal);

        btnCheckout = view.findViewById(R.id.cartCheckoutButton);

        recyclerView = view.findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        adapter = new MyCartAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void loadCartItems() {
        String uid = mAuth.getCurrentUser().getUid();

        firestore.collection("AddToCart")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<MyCartModel> cartModelList = task.getResult().toObjects(MyCartModel.class);
                            totalAmount = 0;
                            for (MyCartModel cartModel : cartModelList) {
                                String priceString = cartModel.getProductPrice();
                                if (priceString != null) {
                                    priceString = priceString.replace("Rs. ", "");
                                    try {
                                        int price = Integer.parseInt(priceString);
                                        totalAmount += price;
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            list.clear();
                            list.addAll(cartModelList);
                            adapter.notifyDataSetChanged();

                            strTotalAmount = String.valueOf(totalAmount);
                            txtProductTotal.setText("Rs. " + strTotalAmount);
                            txtProductFinalTotal.setText("Rs. " + strTotalAmount);
                        } else {
                            // Handle the error
                        }
                    }
                });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT) {
                deletedItem = list.get(position);
                showDeleteDialog(position);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void showDeleteDialog(final int position) {
        new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setTitle("Remove?")
                .setMessage("Are you sure to remove this item?")
                .setIcon(R.drawable.remove_icon)
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCartItem(position);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyItemChanged(position);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteCartItem(final int position) {
        firestore.collection("AddToCart")
                .document(list.get(position).getOid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        try {
                            int itemPrice = Integer.parseInt(list.get(position).getProductPrice().replace("Rs. ", ""));
                            totalAmount -= itemPrice;
                            txtProductTotal.setText("Rs. " + totalAmount);
                            txtProductFinalTotal.setText("Rs. " + totalAmount);

                            list.remove(position);
                            adapter.notifyItemRemoved(position);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
