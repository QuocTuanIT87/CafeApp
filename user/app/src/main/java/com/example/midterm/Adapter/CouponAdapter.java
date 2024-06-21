package com.example.midterm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.EditPromotion;
import com.example.midterm.Model.Product;
import com.example.midterm.Model.Promotion;
import com.example.midterm.PurchaseConfirmationDialogFragment;
import com.example.midterm.PurchaseConfirmationDialogFragmentPromotion;
import com.example.midterm.R;
import com.example.midterm.SuaSanPham;

import java.io.Serializable;
import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.HolderPromotionShop> {
    private Context context;
    private ArrayList<Promotion>promoList;

    public CouponAdapter(Context context, ArrayList<Promotion> promoList) {
        this.context = context;
        this.promoList = promoList;
    }

    @NonNull
    @Override
    public HolderPromotionShop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coupon,parent,false);
        return new HolderPromotionShop(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPromotionShop holder, int position) {
        //get data
        Promotion promotion = promoList.get(position);
        String promoCode=promotion.getPromoCode();
        String description=promotion.getDescription();
        String promoPrice=promotion.getPromoPrice();
        String expireDate=promotion.getExpireDate();
        String minimumOrderPrice=promotion.getMinimumOrderPrice();

        //set data
        holder.descriptionTv.setText(description);
        holder.promoPriceTv.setText(promoPrice +"đ");
        holder.minimumOrderPriceTv.setText(minimumOrderPrice+"đ");
        holder.promoCodeTv.setText("Mã: "+promoCode);
        holder.expireDateTv.setText(expireDate);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(promotion);
            }
        });

        holder.relativePromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToUpdate(promotion);
            }
        });
    }

    private void showDialog(Promotion promotion) {
        new PurchaseConfirmationDialogFragmentPromotion(promotion).show(((AppCompatActivity)context).getSupportFragmentManager(), "");
    }

    @Override
    public int getItemCount() {
        return promoList.size();
    }

    public void onClickGoToUpdate(Promotion promotion) {
        Intent myIntent = new Intent(context, EditPromotion.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_promotion", promotion);
        myIntent.putExtras(bundle);
        context.startActivity(myIntent);
    }
    class HolderPromotionShop extends RecyclerView.ViewHolder{

        //view of row
        private ImageView iconTv;
        private TextView promoCodeTv,promoPriceTv,minimumOrderPriceTv,expireDateTv,descriptionTv;
        private ImageButton btnDelete;
        private RelativeLayout relativePromotion;
        public HolderPromotionShop(@NonNull View itemView) {
            super(itemView);
            iconTv=itemView.findViewById(R.id.iconTv);
            promoCodeTv=itemView.findViewById(R.id.promoCodeTv);
            promoPriceTv=itemView.findViewById(R.id.promoPriceTv);
            minimumOrderPriceTv=itemView.findViewById(R.id.minimumOrderPriceTv);
            expireDateTv=itemView.findViewById(R.id.expireDateTv);
            descriptionTv=itemView.findViewById(R.id.descriptionTv);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            relativePromotion = itemView.findViewById(R.id.relativePromotion);
        }
    }
}
