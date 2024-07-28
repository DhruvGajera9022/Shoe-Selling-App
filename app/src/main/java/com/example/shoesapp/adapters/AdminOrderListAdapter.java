package com.example.shoesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesapp.R;
import com.example.shoesapp.models.OrderModel;

import java.util.List;

public class AdminOrderListAdapter extends RecyclerView.Adapter<AdminOrderListAdapter.ViewHolder> {

    Context context;
    List<OrderModel> list;

    public AdminOrderListAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminOrderListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_all_orders_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(list.get(position).getOrderProductName());
        holder.price.setText(list.get(position).getOrderProductPrice());
        holder.size.setText(list.get(position).getOrderProductSize());
        holder.date.setText(list.get(position).getCurrentOrderDate());
        holder.userId.setText(list.get(position).getCurrentUserId());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, size, date, userId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.adminOrderProductItemName);
            price = itemView.findViewById(R.id.adminOrderProductItemPrice);
            size = itemView.findViewById(R.id.adminOrderProductItemSize);
            date = itemView.findViewById(R.id.adminOrderProductItemDate);
            userId = itemView.findViewById(R.id.adminOrderProductItemUserId);

        }
    }

}
