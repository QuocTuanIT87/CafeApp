package com.example.urcafe.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.urcafe.Helper.ManagmentCart;
import com.example.urcafe.Model.Product;
import com.example.urcafe.R;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {
    private Product object;
    private int num=1;
    private TextView nameProduct ;
    private TextView feeProduct;
    private ImageView imageProduct;
    private TextView desProduct;
    private ImageView addBtn,subBtn;
    private TextView amount;
    private Button addToCartBtn;
    private ManagmentCart managmentCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        nameProduct = findViewById(R.id.nameProduct);
        feeProduct= findViewById(R.id.feeProduct);
        imageProduct=findViewById(R.id.imageProduct);
        desProduct=findViewById(R.id.desProduct);
        addBtn=findViewById(R.id.addbtn);
        subBtn=findViewById(R.id.minusbtn);
        amount=findViewById(R.id.amount);
        addToCartBtn=findViewById(R.id.btnaddToCart);

        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        managmentCart=new ManagmentCart(this);
        feeProduct.setText("$"+object.getProductPrice());
        nameProduct.setText(object.getProductTitle());
        desProduct.setText(object.getProductDescription());
        Picasso.get().load(object.getProductImagePath()).into(imageProduct);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num =num+1;
                amount.setText(num+"");
            }
        });
        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num>1){
                    num=num-1;
                    amount.setText(num+"");
                }
            }
        });
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumberInCart(num);
                managmentCart.insertFood(object);

            }
        });
    }

    private void getIntentExtra() {
        object= (Product) getIntent().getSerializableExtra("object");
    }


}
