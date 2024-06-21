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
public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {
    private List<Product> productList;
    Context context;
    public PopularAdapter(List<Product> productList) {
        this.productList = productList;
    }
    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular,parent,false);
        return new PopularViewHolder(inflate);
    }
    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
       holder.namePopular.setText(productList.get(position).getProductTitle());
       holder.feePopular.setText(productList.get(position).getProductPrice()+"đ");
       Picasso.get().load(productList.get(position).getProductImagePath()).into(holder.imagePopular);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("object", productList.get(clickedPosition));
                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
    public class PopularViewHolder extends RecyclerView.ViewHolder{
        ImageView imagePopular;
        TextView namePopular;
        TextView feePopular;
        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePopular=itemView.findViewById(R.id.image_pop);
            namePopular=itemView.findViewById(R.id.name_pop);
            feePopular=itemView.findViewById(R.id.fee_pop);
        }
    }
}
