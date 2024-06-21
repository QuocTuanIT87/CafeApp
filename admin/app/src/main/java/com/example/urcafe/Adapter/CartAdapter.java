package com.example.urcafe.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urcafe.Helper.ChangeNumberItemsListener;
import com.example.urcafe.Helper.ManagmentCart;
import com.example.urcafe.Model.Product;
import com.example.urcafe.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private ArrayList<Product> list;
    private ManagmentCart managmentCart;
    private ChangeNumberItemsListener changeNumberItemsListener;
    private Context context;

    public CartAdapter(ArrayList<Product> list, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        this.context = context;
        this.changeNumberItemsListener = changeNumberItemsListener;
        managmentCart = new ManagmentCart(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = list.get(position);
        holder.title.setText(product.getProductTitle());
        holder.feeEach.setText("Giá: " + product.getProductPrice() + "đ");
        int totalPrice = product.getNumberInCart() * product.getProductPrice();
        holder.totalPrice.setText("Tổng: " + totalPrice + "đ");
        holder.amount.setText(String.valueOf(product.getNumberInCart()));
        Picasso.get().load(product.getProductImagePath()).into(holder.imageProduct);

        holder.addBtn.setOnClickListener(v -> {
            managmentCart.plusItem(list, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.change();
            });
        });

        holder.minusBtn.setOnClickListener(v -> {
            managmentCart.minusItem(list, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.change();
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView title, feeEach, totalPrice, amount;
        ImageView imageProduct, addBtn, minusBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.nameinCart);
            feeEach = itemView.findViewById(R.id.price);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            amount = itemView.findViewById(R.id.txtamount);
            imageProduct = itemView.findViewById(R.id.image_cart);
            addBtn = itemView.findViewById(R.id.img_add);
            minusBtn = itemView.findViewById(R.id.img_minus);
        }
    }
}
