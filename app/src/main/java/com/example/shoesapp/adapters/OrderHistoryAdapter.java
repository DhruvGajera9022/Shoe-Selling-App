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
import com.example.shoesapp.ConfirmOrderDetailsActivity;
import com.example.shoesapp.OrderProductDetailActivity;
import com.example.shoesapp.R;
import com.example.shoesapp.models.OrderModel;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>{

    Context context;
    List<OrderModel> list;

    public OrderHistoryAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_confirmed, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {

        Glide.with(holder.img.getContext())
                .load(list.get(position).getProductImage())
                .error(R.drawable.image_icon)
                .into(holder.img);
        holder.name.setText(list.get(position).getProductName());
        holder.price.setText(list.get(position).getProductPrice());
        holder.date.append(list.get(position).getdate());

        holder.orderConfirmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConfirmOrderDetailsActivity.class);
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
        CardView orderConfirmCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.orderConfirmProductItemName);
            price = itemView.findViewById(R.id.orderConfirmProductItemPrice);
            date = itemView.findViewById(R.id.orderConfirmProductItemDate);
            img = itemView.findViewById(R.id.orderConfirmProductItemImage);
            orderConfirmCard = itemView.findViewById(R.id.orderConfirmCard);

        }
    }

}
