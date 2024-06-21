package com.example.midterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.Adapter.CouponAdapter;
import com.example.midterm.Model.Promotion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CouponManagement extends AppCompatActivity {

    private ImageButton backBtn,addPromoBtn;
    private RecyclerView promoRv;
    private ArrayList<Promotion>promoList;
    private CouponAdapter couponAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_management);

        // init ui view
        backBtn=findViewById(R.id.backBtn);
        addPromoBtn=findViewById(R.id.addPromoBtn);
        promoRv=findViewById(R.id.promoRv);
        // Khởi tạo Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Promotion");
        // Initialize promoList
        promoList = new ArrayList<>();
        // Initialize and set adapter to RecyclerView
        couponAdapter = new CouponAdapter(this, promoList);
        promoRv.setLayoutManager(new LinearLayoutManager(this));
        promoRv.setAdapter(couponAdapter);
        // Read data from Firebase Realtime Database
        readDataFromFirebase();

        eventButton();

    }

    private void readDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear previous data
                promoList.clear();
                // Read data from each child node
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Promotion promotion = snapshot.getValue(Promotion.class);
                    promoList.add(promotion);
                }
                // Notify adapter about data changes
                couponAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void eventButton() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addPromoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CouponManagement.this,AddPromoActivity.class);
                startActivity(i);
            }
        });
    }
}