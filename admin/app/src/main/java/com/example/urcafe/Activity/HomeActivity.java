package com.example.urcafe.Activity;


import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urcafe.Adapter.CategoryAdapter;
import com.example.urcafe.Adapter.PopularAdapter;
import com.example.urcafe.Common.Common;
import com.example.urcafe.Model.Category;
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

public class HomeActivity extends AppCompatActivity {

    private ImageView cartBtn;
    private ImageView historyBtn;
    private ImageView logoutBtn;
    private ImageView informationBtn;
    private TextView nameUser;
    private EditText searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cartBtn=findViewById(R.id.giohangHome);
        historyBtn=findViewById(R.id.historyBtn);
        logoutBtn=findViewById(R.id.logoutBtn);
        informationBtn=findViewById(R.id.inforBtn);
        nameUser=findViewById(R.id.tvhi);
        searchBtn=findViewById(R.id.editsearch);


        initPopularProduct();
        initCategory();
        setVariable();
    }


    private void setVariable() {
        nameUser.setText("Xin chào "+Common.currentUser.getName());
        //Dia chi
        informationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Information.class));
            }
        });
        //Gio hang
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,CartActivity.class));
            }
        });
        //Lich su mua hang
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,OrderListActivity.class));
            }
        });
        // Dang xuat
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(HomeActivity.this, LoginActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchBtn.getText().toString().trim(); // Lấy từ khóa tìm kiếm
                if (!searchText.isEmpty()) {
                    searchProduct(searchText); // Gọi phương thức tìm kiếm sản phẩm
                }
            }
        });
    }

    private void searchProduct(String searchText) {
        Intent intent = new Intent(HomeActivity.this, ListProductActivity.class);
        intent.putExtra("text", searchText);
        intent.putExtra("isSearch", true);
        startActivity(intent);
        searchBtn.setText("");
    }

    private void initCategory() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("Category");
        List<Category>categoryList=new ArrayList<>();
        RecyclerView recyclerView=findViewById(R.id.rc_cate);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        categoryList.add(issue.getValue(Category.class));
                    }
                    if(categoryList.size()>0){
                        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this,LinearLayoutManager.HORIZONTAL,false));
                        CategoryAdapter adapter=new CategoryAdapter(categoryList);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initPopularProduct() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("Product");
        List<Product>popularList=new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recypop);
        Query query=myRef.orderByChild("popularProduct").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue: snapshot.getChildren()){
                        popularList.add(issue.getValue(Product.class));
                    }
                    if(popularList.size()>0){
                        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this,LinearLayoutManager.VERTICAL,false));
                        PopularAdapter popularAdapter = new PopularAdapter(popularList);
                        recyclerView.setAdapter(popularAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}