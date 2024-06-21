package com.example.midterm;

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
import com.example.midterm.Adapter.OrderAdapter;
import com.example.midterm.Model.Order;
import com.example.midterm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderManagement extends AppCompatActivity implements OrderAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        orderList = new ArrayList<>();
        recyclerView = findViewById(R.id.orderView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OrderAdapter(orderList, this);
        recyclerView.setAdapter(adapter);

        ImageView backBtnOrder = findViewById(R.id.backBtnOrder);

        loadOrder();
        backBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadOrder() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            .addOnFailureListener(e -> Toast.makeText(OrderManagement.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
        // Thực hiện hành động cập nhật trạng thái của đơn hàng
        showUpdateDialog(order.getOrderId(), order.getStatus());
    }

    @Override
    public void onDetailButtonClick(Order order) {
        // Chuyển hướng đến trang chi tiết đơn hàng
        Intent intent = new Intent(OrderManagement.this, DetailOrderActivity.class);
        intent.putExtra("ORDER_DETAIL", (Serializable) order);
        startActivity(intent);
    }

    private void showUpdateDialog(String orderId, String status) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Cập nhật trạng thái đơn hàng");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout, null);

        MaterialSpinner spinner = view.findViewById(R.id.statusSpinner);
        spinner.setItems("Xác nhận", "Giao hàng", "Huỷ", "Giao thành công");

        alertDialog.setView(view);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int selectedIndex = spinner.getSelectedIndex();
                String selectedStatus = convertIndexToStatus(selectedIndex);
                updateOrderStatus(orderId, selectedStatus);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private String convertIndexToStatus(int index) {
        switch (index) {
            case 0:
                return "1";
            case 1:
                return "2";
            case 2:
                return "-1";
            case 3:
                return "3";
            default:
                return "";
        }
    }

    private void updateOrderStatus(String orderId, String status) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
        orderRef.child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    if (status.equals("-1") || status.equals("3")) {
                        // Nếu đơn hàng đã bị huỷ hoặc đã giao thành công, chuyển đơn hàng vào lịch sử
                        moveOrderToHistory(orderId);
                    } else {
                        // Nếu đơn hàng đang được xử lý, cập nhật lại trạng thái và giao diện
                        for (Order order : orderList) {
                            if (order.getOrderId().equals(orderId)) {
                                order.setStatus(status);
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(OrderManagement.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
