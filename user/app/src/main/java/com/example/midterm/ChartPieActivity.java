package com.example.midterm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.midterm.Model.Order;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChartPieActivity extends AppCompatActivity {

    Button thongKeTheoThoiGian;
    ImageView backBtnUser;

    private int sumDangXuLy, sumDangChuanBi, sumDangGiaoHang, sumDaGiaoHang, sumDaHuy;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        thongKeTheoThoiGian = findViewById(R.id.btnPie);
        backBtnUser = findViewById(R.id.backBtnOrder);
        loadData();

        thongKeTheoThoiGian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChartPieActivity.this,ChartActivity.class);
                startActivity(i);
            }
        });

        backBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    public void loadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Orders");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               sumDangXuLy = sumDangChuanBi = sumDangGiaoHang = sumDaGiaoHang = sumDaHuy = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Order order = postSnapshot.getValue(Order.class);
                      if(order != null) {
                          String status = order.getStatus();
                          if(status.equals("0")) {
                              sumDangXuLy++;
                          } else if (status.equals("1")) {
                              sumDangChuanBi++;
                          } else if (status.equals("2")) {
                              sumDangGiaoHang++;
                          } else if (status.equals("3")) {
                              sumDaGiaoHang++;
                          } else if (status.equals("-1")) {
                              sumDaHuy++;
                          }
                      }
                }

                loadDataToPieChart();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    public void loadDataToPieChart() {
        PieChart pieChart = findViewById(R.id.chart_pie);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(sumDangXuLy, "Đang xử lý"));
        entries.add(new PieEntry(sumDangChuanBi, "Đang chuẩn bị"));
        entries.add(new PieEntry(sumDangGiaoHang, "Đang giao hàng"));
        entries.add(new PieEntry(sumDaGiaoHang, "Đã giao hàng"));
        entries.add(new PieEntry(sumDaHuy, "Đã hủy"));


        PieDataSet pieDataSet = new PieDataSet(entries, "ORDERS");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        // Tùy chỉnh kích thước và độ đậm của giá trị
        pieDataSet.setValueTextSize(12f); // Kích thước văn bản
        pieDataSet.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD)); // Đậm

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}
