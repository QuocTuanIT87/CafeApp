package com.example.midterm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.midterm.Model.Order;
import com.example.midterm.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class HomeActivity extends AppCompatActivity {
    private Button btnOrderManagement, btnHistoryOrder, btnAddUser, btnProductManagement, btnStatical, couponBtn;
    ImageView homeBtn, logoutBtn;
    TextView txtTotalOrder, txtTotalOrderComplete, txtPriceOrderComplete;
    private int sumOrder;
    private int sumOrderComplete;
    private int sumPriceOrderComplete;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnOrderManagement = findViewById(R.id.btnOrderManagement);
        btnHistoryOrder = findViewById(R.id.btnHistoryOrder);
        btnAddUser = findViewById(R.id.btnAddUser);
        btnProductManagement = findViewById(R.id.btnProductManagement);
        homeBtn = findViewById(R.id.btnHome);
        couponBtn = findViewById(R.id.btnCouponManagement);
        logoutBtn = findViewById(R.id.btnLogOut);
        btnStatical = findViewById(R.id.btnStatical);
        txtTotalOrder = findViewById(R.id.order_total);
        txtTotalOrderComplete = findViewById(R.id.order_complete);
        setVariable();
        txtPriceOrderComplete = findViewById(R.id.total_money_order_complete);
        loadDataHomeActivity();
    }

    public void loadDataHomeActivity() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Orders");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sumOrder = sumOrderComplete = sumPriceOrderComplete = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Order order = postSnapshot.getValue(Order.class);
                    sumOrder++;
                    if(order.getStatus().equals("3")) {
                        sumOrderComplete++;
                        String priceWithoutCurrency = order.getTotal().replace("đ", "");
                        float priceFloat = Float.parseFloat(priceWithoutCurrency);
                        sumPriceOrderComplete += priceFloat;
                    }
                }
                DecimalFormat formatter = new DecimalFormat("#,### đ");
                String formattedAmount = formatter.format(sumPriceOrderComplete);
                txtTotalOrder.setText(String.valueOf(sumOrder));
                txtTotalOrderComplete.setText(String.valueOf(sumOrderComplete));
                txtPriceOrderComplete.setText(formattedAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setVariable() {
        //Khuyến mãi
        couponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,CouponManagement.class);
                startActivity(i);
            }
        });

        //Đơn hàng
        btnOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order = new Intent(HomeActivity.this, OrderManagement.class);
                startActivity(order);

            }
        });

        //Lịch sử đơn hàng
        btnHistoryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent history = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(history);

            }
        });

        //Quản lý User
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user = new Intent(HomeActivity.this, UserManagement.class);
                startActivity(user);

            }
        });

        //Quản lý sản phẩm
        btnProductManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user = new Intent(HomeActivity.this, ProductManagement.class);
                startActivity(user);

            }
        });

        //Trang chủ
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,HomeActivity.class));
            }
        });

        // Đăng xuất
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(HomeActivity.this, LoginActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
            }
        });

        // Thống kê
        btnStatical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ChartPieActivity.class));
            }
        });
    }
}



