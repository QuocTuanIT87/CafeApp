package com.example.midterm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.midterm.Model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SuaSanPham extends AppCompatActivity {
    Button btnUpdate;
    EditText edtTenMon;
    EditText edtGiaTien;
    EditText edtMoTa;
    Button btnUpload;
    ImageView imgSanPham;
    Uri filePath;
    Uri downloadUrl;
    String idProduct;
    int categoryProduct;
    private Spinner categoryId;
    private CheckBox isPopular;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

//        btnBack = findViewById(R.id.btnBackShow);
        edtTenMon = findViewById(R.id.edtTenMon);
        edtGiaTien = findViewById(R.id.edtGiaTien);
        edtMoTa = findViewById(R.id.edtMoTa);
        btnUpload = findViewById(R.id.btnUpload);
        imgSanPham = findViewById(R.id.imgSanPham);
        btnUpdate = findViewById(R.id.btnUpdate);
        categoryId=findViewById(R.id.spinnerCategoryId);
        isPopular=findViewById(R.id.checkBoxPopularProduct);

        String[] categories = {"Espresso", "Cappuccino", "Latte", "Mocha", "Americano", "Macchiato"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryId.setAdapter(adapter);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProduct();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                someActivityResultLauncher.launch(intent);
            }
        });
        loadDataObject();
    }

    public void loadDataObject() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        Product product = (Product) bundle.get("object_product");


        edtTenMon.setText(product.getProductTitle());
        edtGiaTien.setText(String.valueOf(product.getProductPrice()));
        edtMoTa.setText(product.getProductDescription());
        idProduct = product.getProductId();
        Picasso.get().load(product.getProductImagePath()).into(imgSanPham);
        isPopular.setChecked(product.isPopularProduct());

        categoryId.setSelection(product.getCategoryId() - 1);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // There are no request codes
                    Intent data = result.getData();
                    imgSanPham.setImageURI(data.getData());
                    filePath = data.getData();
                }
            });

    public void updateProduct() {
        Product newProduct = new Product();

        //Upload image
        FirebaseStorage storage;
        StorageReference storageReference;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

        if (filePath != null) {
            UploadTask uploadTask = ref.putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri;
                            //Do what you want with the url

                            Map<String, Object> map = new HashMap<>();
                            map.put("productTitle", edtTenMon.getText().toString());
                            map.put("productPrice", Double.parseDouble(String.valueOf(edtGiaTien.getText())));
                            map.put("productDescription", edtMoTa.getText().toString());
                            map.put("productImagePath", uri.toString());
                            map.put("popularProduct", isPopular.isChecked());
                            map.put("categoryId", categoryId.getSelectedItemPosition() + 1);
                            map.put("status", true);

                            // Write a object to the database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference().child("Product/" + idProduct);;

                            myRef.updateChildren(map, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(SuaSanPham.this, "Update product successfully!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    });
                }

                ;
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SuaSanPham.this, "Failed update", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("productTitle", edtTenMon.getText().toString());
            map.put("productPrice", Double.parseDouble(String.valueOf(edtGiaTien.getText())));
            map.put("productDescription", edtMoTa.getText().toString());
            map.put("popularProduct", isPopular.isChecked());
            map.put("categoryId", categoryId.getSelectedItemPosition() + 1);
            map.put("status", true);


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("Product/" + idProduct);

            myRef.updateChildren(map, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(SuaSanPham.this, "Update product successfully!", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }
}