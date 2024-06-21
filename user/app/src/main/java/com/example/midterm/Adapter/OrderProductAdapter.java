package com.example.midterm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.Model.Product;
import com.example.midterm.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public OrderProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView txtProductTitle, txtProductQuantity, txtProductTotalPrice;
        ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductTitle = itemView.findViewById(R.id.product_title);
            txtProductQuantity = itemView.findViewById(R.id.product_quantity);
            txtProductTotalPrice = itemView.findViewById(R.id.product_total_price);
            imgProduct = itemView.findViewById(R.id.product_image);

        }

        public void bind(Product product) {
            txtProductQuantity.setText(product.getNumberInCart() + "x");
            txtProductTitle.setText(product.getProductTitle());
            txtProductTotalPrice.setText(product.getProductTotal() + "đ");
            // Sử dụng Picasso để tải hình ảnh và hiển thị nó trong ImageView
            Picasso.get().load(product.getProductImagePath()).into(imgProduct);
        }
    }
}
