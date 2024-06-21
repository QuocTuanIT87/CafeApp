package com.example.urcafe.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urcafe.Activity.ListProductActivity;
import com.example.urcafe.Model.Category;
import com.example.urcafe.R;
import com.squareup.picasso.Picasso;

import java.util.List;
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categoryList;
    Context context;
    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new CategoryViewHolder(inflate);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.nameCategory.setText(categoryList.get(holder.getAdapterPosition()).getName());
        Picasso.get().load(categoryList.get(holder.getAdapterPosition()).getImagePath()).into(holder.imageCategory);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, ListProductActivity.class);
                    intent.putExtra("categoryId", categoryList.get(adapterPosition).getId());
                    intent.putExtra("CategoryName", categoryList.get(adapterPosition).getName());
                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView imageCategory;
        TextView nameCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCategory=itemView.findViewById(R.id.image_cate);
            nameCategory=itemView.findViewById(R.id.cate_name);

        }
    }
}
