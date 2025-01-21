package com.example.shoesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesapp.adapters.MyCartAdapter;
import com.example.shoesapp.models.MyCartModel;
import com.example.shoesapp.profileactivities.EditProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private MyCartAdapter adapter;
    private ArrayList<MyCartModel> list;

    private MyCartModel deletedItem = null;

    private TextView txtProductTotal, txtProductDeliveryTotal, txtProduceVoucherTotal, txtProductFinalTotal,cartProductFinalTotal1, txtCartViewProductDetails;
    private Button conntinue;

    private String strTotalAmount, uid, paymentMethod;
    private int totalAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initialize();
        loadCartItems();
    }

    private void initialize() {
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        txtProductTotal = findViewById(R.id.cartProductTotal);
        txtProductDeliveryTotal = findViewById(R.id.cartProductDeliveryTotal);
        cartProductFinalTotal1 = findViewById(R.id.cartProductFinalTotal1);
        txtProduceVoucherTotal = findViewById(R.id.cartProductVoucherTotal);
        txtProductFinalTotal = findViewById(R.id.cartProductFinalTotal);
        txtCartViewProductDetails = findViewById(R.id.cartViewProductDetails);

        conntinue = findViewById(R.id.conntinue);

        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new MyCartAdapter(this, list);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        conntinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toOrder();
            }
        });
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
                            cartProductFinalTotal1.setText("Rs. " + strTotalAmount);
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
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(CartActivity.this, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void showDeleteDialog(final int position) {
        new AlertDialog.Builder(this)
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
                            cartProductFinalTotal1.setText("Rs. " + totalAmount);

                            list.remove(position);
                            adapter.notifyItemRemoved(position);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void toOrder() {
        if (list.isEmpty()) {
            Toast.makeText(this, "Please, Add some products to cart", Toast.LENGTH_SHORT).show();
        } else {
            DialogPlus dialogPlus = DialogPlus.newDialog(this)
                    .setContentHolder(new ViewHolder(R.layout.buy_details))
                    .setExpanded(true, 2080)
                    .setCancelable(true)
                    .create();

            View dailogview = dialogPlus.getHolderView();

            TextView txtEdit = dailogview.findViewById(R.id.editProfile);
            TextView txtName = dailogview.findViewById(R.id.buyPersonName);
            TextView txtEmail = dailogview.findViewById(R.id.buyPersonEmail);
            TextView txtNumber = dailogview.findViewById(R.id.buyPersonNumber);
            RadioButton rbAddress = dailogview.findViewById(R.id.buyPersonAddress);
            MaterialButton btnSave = dailogview.findViewById(R.id.buySaveBtn);
            MaterialButton btnCancel = dailogview.findViewById(R.id.buyCancelBtn);

            RadioButton rbOptionCreditDebitAtm = dailogview.findViewById(R.id.rbOptionCreditDebitAtm);
            RadioButton rbOptionGPay = dailogview.findViewById(R.id.rbOptionGPay);
            RadioButton rbOptionPhonePay = dailogview.findViewById(R.id.rbOptionPhonePay);
            RadioButton rbOptionNetBanking = dailogview.findViewById(R.id.rbOptionNetBanking);
            RadioButton rbOptionWallets = dailogview.findViewById(R.id.rbOptionWallets);
            RadioButton rbOptionCashOnDelivery = dailogview.findViewById(R.id.rbOptionCashOnDelivery);

            uid = mAuth.getCurrentUser().getUid();

            DocumentReference documentReference = firestore.collection("Users").document(uid);

            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null && value.exists()) {
                        txtName.setText(value.getString("FirstName"));
                        txtName.append(" ");
                        txtName.append(value.getString("LastName"));
                        txtEmail.setText(value.getString("Email"));
                        txtNumber.setText(value.getString("Mobile"));
                        rbAddress.setText(value.getString("Address"));
                    }
                }
            });

            dialogPlus.show();

            txtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CartActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String strPaymentMethod;

                    if(rbOptionCreditDebitAtm.isChecked()){
                        strPaymentMethod = rbOptionCreditDebitAtm.getText().toString();
                    }else if(rbOptionGPay.isChecked()){
                        strPaymentMethod = rbOptionGPay.getText().toString();
                    }else if(rbOptionPhonePay.isChecked()){
                        strPaymentMethod = rbOptionPhonePay.getText().toString();
                    }else if(rbOptionNetBanking.isChecked()){
                        strPaymentMethod = rbOptionNetBanking.getText().toString();
                    }else if(rbOptionWallets.isChecked()){
                        strPaymentMethod = rbOptionWallets.getText().toString();
                    }else{
                        strPaymentMethod = rbOptionCashOnDelivery.getText().toString();
                    }

                    for (MyCartModel list : list) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("productName", list.getProductName());
                        map.put("productCompanyName", list.getCategoryCompany());
                        map.put("productPrice", list.getProductPrice());
                        map.put("productSize", list.getProductSize());
                        map.put("productImage", list.getProductImage());
                        map.put("oid", list.getOid());
                        map.put("productDescription", list.getProductDescription());
                        map.put("uid", list.getUid());
                        map.put("userName", txtName.getText().toString());
                        map.put("email", txtEmail.getText().toString());
                        map.put("number", txtNumber.getText().toString());
                        map.put("address", rbAddress.getText().toString());
                        map.put("paymentMethod", strPaymentMethod);

                        // Set the current date
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                        LocalDateTime now = LocalDateTime.now();
                        String date = dtf.format(now);
                        map.put("date", date);

                        // Add the order to Firestore
                        firestore.collection("Orders")
                                .document(list.getOid())
                                .set(map);

                        // Remove the item from the cart
                        firestore.collection("AddToCart")
                                .document(list.getOid())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        loadCartItems();  // Refresh the cart after deletion
                                    }
                                });
                    }
                    Intent intent = new Intent(CartActivity.this, OrderConfirmActivity.class);
                    startActivity(intent);
                    dialogPlus.dismiss();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogPlus.dismiss();
                }
            });
        }
    }
}
