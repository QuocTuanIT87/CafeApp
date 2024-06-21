package com.example.urcafe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urcafe.Adapter.HistoryOrderAdapter;
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

public class HistoryOrderActivity extends AppCompatActivity implements HistoryOrderAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private HistoryOrderAdapter adapter;
    private List<Order> historyOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);

        historyOrderList = new ArrayList<>();
        recyclerView = findViewById(R.id.orderView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HistoryOrderAdapter(historyOrderList, this);
        recyclerView.setAdapter(adapter);

        ImageView backBtnOrder = findViewById(R.id.backBtnOrder);
        backBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadHistoryOrder();
    }

    private void loadHistoryOrder() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyOrderList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null && (order.getStatus().equals("-1") || order.getStatus().equals("3"))) {
                        order.setOrderId(snapshot.getKey());
                        historyOrderList.add(order);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    @Override
    public void onItemClick(Order order) {

    }

    @Override
    public void onDetailButtonClick(Order order) {
        // Chuyển hướng đến trang chi tiết sản phẩm activity_detail_order.xml
        Intent intent = new Intent(HistoryOrderActivity.this, DetailOrderActivity.class);
        intent.putExtra("ORDER_DETAIL", (Serializable) order);
        startActivity(intent);
    }


}
