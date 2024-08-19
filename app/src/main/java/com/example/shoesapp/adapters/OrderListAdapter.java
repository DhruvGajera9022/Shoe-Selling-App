package com.example.shoesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoesapp.OrderProductDetailActivity;
import com.example.shoesapp.R;
import com.example.shoesapp.models.OrderModel;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    Context context;
    List<OrderModel> list;

    public OrderListAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(holder.img.getContext())
                .load(list.get(position).getProductImage())
                .error(R.drawable.image_icon)
                .into(holder.img);
        holder.name.setText(list.get(position).getProductName());
        holder.price.setText(list.get(position).getProductPrice());
        holder.date.setText(list.get(position).getdate());

        holder.orderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderProductDetailActivity.class);
                intent.putExtra("key", list.get(position).getOid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, date;
        ImageView img;
        CardView orderCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.orderProductItemName);
            price = itemView.findViewById(R.id.orderProductItemPrice);
            date = itemView.findViewById(R.id.orderProductItemDate);
            img = itemView.findViewById(R.id.orderProductItemImage);
            orderCard = itemView.findViewById(R.id.orderCard);

        }
    }

}
