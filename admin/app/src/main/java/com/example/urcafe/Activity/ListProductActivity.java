package com.example.urcafe.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urcafe.Adapter.ProductListAdapter;
import com.example.urcafe.Model.Product;
import com.example.urcafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListProductActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterListProduct;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        getIntentExtra();
        initListProduct();
    }

    private void initListProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Product");
        List<Product>productList=new ArrayList<>();
        Query query;
        if(isSearch){
            query=myRef.orderByChild("productTitle").startAt(searchText).endAt(searchText+'\uf8ff');
        }else{
            query=myRef.orderByChild("categoryId").equalTo(categoryId);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue : snapshot.getChildren()){
                        productList.add(issue.getValue(Product.class));
                    }
                    if(productList.size()>0){
                        RecyclerView recyclerView = findViewById(R.id.productView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ListProductActivity.this,LinearLayoutManager.VERTICAL,false));;
                        adapterListProduct=new ProductListAdapter(productList);
                        recyclerView.setAdapter(adapterListProduct);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getIntentExtra() {
        TextView txtNameCategory = findViewById(R.id.txtTitle);
        ImageView backBtn = findViewById(R.id.backBtn);
        categoryId=getIntent().getIntExtra("categoryId",0);
        categoryName=getIntent().getStringExtra("CategoryName");
        searchText=getIntent().getStringExtra("text");
        isSearch=getIntent().getBooleanExtra("isSearch",false);
        txtNameCategory.setText(categoryName);
        backBtn.setOnClickListener(v -> finish());
    }
}