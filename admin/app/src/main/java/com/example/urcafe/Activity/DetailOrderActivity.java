package com.example.urcafe.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.urcafe.Adapter.OrderProductAdapter;
import com.example.urcafe.Model.Order;
import com.example.urcafe.Model.Product;
import com.example.urcafe.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailOrderActivity extends AppCompatActivity {
    ArrayList<Product> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order); // Gắn layout activity_detail_order.xml vào activity

        // Lấy dữ liệu đơn hàng từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            Order order = (Order) intent.getSerializableExtra("ORDER_DETAIL");
            if (order != null) {
                // Hiển thị thông tin đơn hàng trên giao diện
                displayOrderDetails(order);
            }
        }

        ImageView backBtnOrder = findViewById(R.id.backBtnOrder);
        backBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void displayOrderDetails(Order order) {
        // Lấy các View từ layout activity_detail_order.xml
        TextView txtOrderId = findViewById(R.id.orderId);
        TextView txtOrderName = findViewById(R.id.order_name);
        TextView txtOrderPhone = findViewById(R.id.order_phone);
        TextView txtOrderAddress = findViewById(R.id.order_address);
        TextView txtOrderStatus = findViewById(R.id.order_status);
        TextView txtOrderTotal = findViewById(R.id.order_total);
        TextView txtOrderTime = findViewById(R.id.order_time);

        // Hiển thị thông tin đơn hàng lên giao diện
        txtOrderId.setText("ID: " + order.getOrderId());
        txtOrderName.setText("Tên khách hàng: " + order.getName());
        txtOrderPhone.setText("Số điện thoại: " + order.getPhone());
        txtOrderAddress.setText("Địa chỉ: " + order.getAddress());
        txtOrderStatus.setText("Trạng thái: " + convertStatus(order.getStatus()));
        txtOrderTotal.setText("Tổng tiền: " + order.getTotal());
        txtOrderTime.setText("Thời gian đặt hàng: " + order.getOrderDateTime());

        // Bổ sung hiển thị sản phẩm đơn hàng trong RecyclerView (sử dụng Adapter)

        // Bắt đầu thêm phần hiển thị sản phẩm đơn hàng
        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Tạo adapter và thiết lập dữ liệu
        OrderProductAdapter adapter = new OrderProductAdapter(order.getProduct());
        recyclerView.setAdapter(adapter);

    }

    private String convertStatus(String status) {
        switch (status) {
            case "0":
                return "Đang xử lý";
            case "1":
                return "Đang chuẩn bị";
            case "2":
                return "Đang giao hàng";
            case "3":
                return "Đã giao hàng";
            case "-1":
                return "Đã hủy";
            default:
                return "";
        }
    }
}
