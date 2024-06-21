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
import com.example.urcafe.Activity.ProductDetailActivity;
import com.example.urcafe.Model.Product;
import com.example.urcafe.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {
    List<Product>items;
    Context context;

    public ProductListAdapter(List<Product> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ProductListAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_product,parent,false);

        return new ProductViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ProductViewHolder holder, int position) {
        holder.txtTitle.setText(items.get(holder.getAdapterPosition()).getProductTitle());
        holder.txtPrice.setText(items.get(holder.getAdapterPosition()).getProductPrice()+"đ");
        holder.txtMota.setText(items.get(holder.getAdapterPosition()).getProductDescription());
        Picasso.get().load(items.get(holder.getAdapterPosition()).getProductImagePath()).into(holder.imgProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("object", items.get(clickedPosition));
                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txtPrice,txtMota;
        ImageView imgProduct;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle=itemView.findViewById(R.id.txtTenSP);
            txtPrice=itemView.findViewById(R.id.txtGiaSP);
            imgProduct=itemView.findViewById(R.id.imgAnhSP);
            txtMota=itemView.findViewById(R.id.txtMoTaSP);

        }
    }
}
