package com.example.shoesapp.fragments;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.shoesapp.MainActivity;
import com.example.shoesapp.ProductDetailsActivity;
import com.example.shoesapp.R;
import com.example.shoesapp.adapters.MyCartAdapter;
import com.example.shoesapp.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartFragment extends Fragment {

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    RecyclerView recyclerView;
    MyCartAdapter adapter;
    ArrayList<MyCartModel> list;

    MyCartModel deletedItem = null;

    public CartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        adapter = new MyCartAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);

        String uid = mAuth.getCurrentUser().getUid();

        firestore.collection("AddToCart")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<MyCartModel> cartModelList = task.getResult().toObjects(MyCartModel.class);
                            list.addAll(cartModelList);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    deletedItem = list.get(position);
                    showDeleteDialog(position);
                    break;
                case ItemTouchHelper.RIGHT:
//                    editCartItem(position);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.sky))
                    .addSwipeRightActionIcon(R.drawable.edit_icon)
                    .create()
                    .decorate();


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void editCartItem(int position) {
        MyCartModel cartItem = list.get(position);
        if (cartItem != null && cartItem.getOid() != null) {
            Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
            intent.putExtra("key", cartItem.getUid());
            intent.putExtra("key", cartItem.getProductName()); // Pass additional item data if needed
            intent.putExtra("key", cartItem.getProductPrice()); // Pass additional item data if needed
            intent.putExtra("key", cartItem.getProductDescription()); // Pass additional item data if needed
            intent.putExtra("key", cartItem.getProductSize()); // Pass additional item data if needed
            intent.putExtra("key", cartItem.getProductImage()); // Pass additional item data if needed
            startActivity(intent);
        } else {
            Snackbar.make(recyclerView, "Error: Item details are invalid", Snackbar.LENGTH_LONG).show();
        }
    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setCancelable(false)
                .setTitle("Remove?")
                .setMessage("Are you sure to remove this item?")
                .setIcon(R.drawable.remove_icon)
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCartItem(position);
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void deleteCartItem(int position) {
        firestore.collection("AddToCart")
                .document(list.get(position).getOid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        list.remove(position);
                        adapter.notifyItemRemoved(position);

                        Snackbar.make(recyclerView, "Item removed from cart", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        list.add(position, deletedItem);
                                        adapter.notifyItemInserted(position);
                                    }
                                }).show();
                    }
                });
    }
}
