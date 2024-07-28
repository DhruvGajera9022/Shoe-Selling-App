package com.example.shoesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesapp.R;
import com.example.shoesapp.models.AdminProfileDataModel;

import java.util.List;

public class AdminProfileDataAdapter extends RecyclerView.Adapter<AdminProfileDataAdapter.ViewHolder> {
    Context context;
    List<AdminProfileDataModel> list;

    public AdminProfileDataAdapter(Context context, List<AdminProfileDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_profile_user_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtUsername.setText(list.get(position).getUserName());
        holder.txtEmail.setText(list.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtUsername = itemView.findViewById(R.id.txtAdminProfileUserName);
            txtEmail = itemView.findViewById(R.id.txtAdminProfileEmail);

        }
    }
}
