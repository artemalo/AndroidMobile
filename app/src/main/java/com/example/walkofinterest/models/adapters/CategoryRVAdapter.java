package com.example.walkofinterest.models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkofinterest.R;

import java.util.ArrayList;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.MyViewHolder> {
    Context context;
    ArrayList<CategoryModel> categoryModels;

    public CategoryRVAdapter(Context context, ArrayList<CategoryModel> categoryModels){
        this.context = context;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public CategoryRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_view_category, parent, false);

        return new CategoryRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRVAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(categoryModels.get(position).getName());
        holder.tvDescription.setText(categoryModels.get(position).getDescription());
        holder.imageView.setImageResource(categoryModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tvName, tvDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageCategory);
            tvName = itemView.findViewById(R.id.nameCategory);
            tvDescription = itemView.findViewById(R.id.descriptionCategory);

        }
    }
}
