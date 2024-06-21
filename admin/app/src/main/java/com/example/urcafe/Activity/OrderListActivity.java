package com.example.urcafe.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.urcafe.Adapter.OrderAdapter;
import com.example.urcafe.Common.Common;
import com.example.urcafe.Model.Order;
import com.example.urcafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity implements OrderAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> orderList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView historyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        orderList = new ArrayList<>();
        recyclerView = findViewById(R.id.orderView);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        historyBtn=findViewById(R.id.HistoryOrder);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OrderAdapter(orderList, this);
        recyclerView.setAdapter(adapter);

        ImageView backBtnOrder = findViewById(R.id.backBtnOrder);

        loadOrder(Common.currentUser.getPhone());
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderListActivity.this,HistoryOrderActivity.class);
                startActivity(intent);
            }
        });
        backBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                loadOrder(Common.currentUser.getPhone());
            }
        });


    }

    private void loadOrder(String phone) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        ordersRef.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    order.setOrderId(snapshot.getKey());

                    // Kiểm tra trạng thái đơn hàng và thêm vào danh sách tương ứng
                    if ("-1".equals(order.getStatus()) || "3".equals(order.getStatus())) {
                        // Nếu đơn hàng đã bị huỷ hoặc đã giao thành công, thêm vào danh sách canceledOrderList
                        moveOrderToHistory(order.getOrderId());
                    } else {
                        // Nếu đơn hàng đang được xử lý, thêm vào danh sách inProgressOrderList
                        orderList.add(order);
                    }
                }

                // Cập nhật giao diện
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }

    private void moveOrderToHistory(String orderId) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.getValue(Order.class);
                if (order != null) {
                    DatabaseReference historyOrdersRef = FirebaseDatabase.getInstance().getReference("HistoryOrders").child(orderId);
                    historyOrdersRef.setValue(order)
                            .addOnSuccessListener(aVoid -> {
                                // Xóa đơn hàng khỏi danh sách đang diễn ra
                                orderList.remove(order);
                                // Cập nhật giao diện
                                adapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> Toast.makeText(OrderListActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }

    @Override
    public void onItemClick(Order order) {
        String address = order.getAddress();

        // Tạo Intent để chuyển sang trang khác
        Intent intent = new Intent(OrderListActivity.this, ShipperTrackingActivity.class);

        // Đặt giá trị của biến address vào Intent
        intent.putExtra("address", address);

        // Chuyển sang trang khác với Intent đã được cài đặt
        startActivity(intent);

    }

    @Override
    public void onDetailButtonClick(Order order) {
        // Chuyển hướng đến trang chi tiết đơn hàng
        Intent intent = new Intent(OrderListActivity.this, DetailOrderActivity.class);
        intent.putExtra("ORDER_DETAIL", (Serializable) order);
        startActivity(intent);
    }
}