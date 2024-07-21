package com.example.shoesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>{

    Context context;
    ArrayList<ProductModel> datalist;
    FirebaseFirestore db;
    ProductModel model;

    public ProductAdapter(Context context, ArrayList<ProductModel> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_design,parent,false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(datalist.get(position).getName());
        holder.price.setText(datalist.get(position).getPrice());
        holder.description.setText(datalist.get(position).getDescription());
        Glide.with(holder.img.getContext())
                .load(datalist.get(position).getImgurl())
                .error(R.drawable.image_icon)
                .into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.wrapper, new ProductDetailsFragment())
//                        .replace(R.id.wrapper, new ProductDetailsFragment(model.getImgurl(), model.getName(), model.getPrice(), model.getDescription()))
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,price,description;
        ImageView img;
        Button edit,delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txtname);
            price = itemView.findViewById(R.id.txtprice);
            img = itemView.findViewById(R.id.img);
            description = itemView.findViewById(R.id.txtdec);

        }
    }






























//    List<ProductModel> list;
//    Context context;
//    DatabaseReference reference;
//
//    public ProductAdapter(List<ProductModel> list, Context context) {
//        this.list = list;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.product_design,parent,false);
//        return new myViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
//        ProductModel productModel = list.get(position);
//        holder.name.setText(productModel.getName());
//        holder.price.setText(productModel.getPrice());
//        holder.description.setText(productModel.getDescription());
//        Picasso.get().load(productModel.getImgurl()).into(holder.imageView);
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public class myViewHolder extends RecyclerView.ViewHolder {
//        TextView name, price, description;
//        ImageView imageView;
//
//        public myViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            name = itemView.findViewById(R.id.txtname);
//            price = itemView.findViewById(R.id.txtprice);
//            description = itemView.findViewById(R.id.txtdec);
//            imageView = itemView.findViewById(R.id.img);
//
//        }
//    }

}
