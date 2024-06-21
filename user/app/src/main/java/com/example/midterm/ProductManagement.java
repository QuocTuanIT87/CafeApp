package com.example.midterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.Adapter.ProductAdapter;
import com.example.midterm.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProductManagement extends AppCompatActivity {
    private ImageButton addProductBtn;
    private ImageButton backBtn;
    private DatabaseReference databaseReference;
    private RecyclerView productRv;
    private ProductAdapter productAdapter;
    private ArrayList<Product>productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_product_management);

        // init ui view
        backBtn=findViewById(R.id.backButton);
        addProductBtn=findViewById(R.id.addProductBtn);
        productRv=findViewById(R.id.productRv);

        databaseReference=FirebaseDatabase.getInstance().getReference().child("Product");
        productList=new ArrayList<>();
        productAdapter=new ProductAdapter(this,productList);
        productRv.setLayoutManager(new LinearLayoutManager(this));
        productRv.setAdapter(productAdapter);


        // Read data from Firebase Realtime Database
        readDataFromFirebase();
        eventButton();
    }

    private void eventButton() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductManagement.this, AddProductActivity.class);
                startActivity(i);
            }
        });
    }

    private void readDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear previous data
                productList.clear();
                // Read data from each child node
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    productList.add(product);
                }
                // Notify adapter about data changes
                productAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}