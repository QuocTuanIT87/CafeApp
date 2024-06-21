package com.example.midterm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.Model.Product;

import com.example.midterm.PurchaseConfirmationDialogFragment;
import com.example.midterm.R;
import com.example.midterm.SuaSanPham;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.HolderProductShop> {
    private Context context;
    private ArrayList<Product>productList;

    public ProductAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public HolderProductShop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product,parent,false);
        return new HolderProductShop(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductShop holder, int position) {
        //get data
        Product product = productList.get(position);
        String tenSp=product.getProductTitle();
        String giaSp= String.valueOf(product.getProductPrice());
        String motaSp=product.getProductDescription();
        String anhSp=product.getProductImagePath();

        //set data
        holder.txtTenSP.setText(tenSp);
        holder.txtGiaSp.setText(giaSp);
        holder.txtMotaSp.setText(motaSp);
        Picasso.get().load(anhSp).into(holder.imgAnhSp);

        holder.swtStatus.setChecked(product.isStatus());
        if(product.isStatus() == true) {
            holder.swtStatus.setText("Còn hàng");
        } else {
            holder.swtStatus.setText("Tạm thời hết hàng");
        }
        holder.swtStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    holder.swtStatus.setText("Còn hàng");
                } else {
                    holder.swtStatus.setText("Tạm thời hết hàng");
                }
                updateStatusProduct(product);
            }

        });

        holder.tableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToUpdate(product);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(product);
            }
        });

    }

    private void updateStatusProduct(Product product) {
        Map<String, Object> map = new HashMap<>();
        if (product.isStatus() == true) {
            map.put("status", false);
        } else {
            map.put("status", true);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Product/" + product.getProductId());

        myRef.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });
    }

    private void showDialog(Product product) {
        new PurchaseConfirmationDialogFragment(product).show(((AppCompatActivity)context).getSupportFragmentManager(), "");
    }

    public void onClickGoToUpdate(Product product) {
        Intent myIntent = new Intent(context, SuaSanPham.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_product", product);
        myIntent.putExtras(bundle);
        context.startActivity(myIntent);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class HolderProductShop extends RecyclerView.ViewHolder{

        //view of row
        private ImageView imgAnhSp;
        private TextView txtTenSP,txtGiaSp,txtMotaSp;
        private TableLayout tableLayout;
        private ImageButton btnDelete;
        private Switch swtStatus;
        public HolderProductShop(@NonNull View itemView) {
            super(itemView);
            imgAnhSp=itemView.findViewById(R.id.imgAnhSP);
            txtTenSP=itemView.findViewById(R.id.txtTenSP);
            txtGiaSp=itemView.findViewById(R.id.txtGiaSP);
            txtMotaSp=itemView.findViewById(R.id.txtMoTaSP);
            tableLayout = itemView.findViewById(R.id.tableItem);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            swtStatus = itemView.findViewById(R.id.swtStatus);

        }
    }
}
