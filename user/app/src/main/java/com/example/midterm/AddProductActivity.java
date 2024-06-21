package com.example.midterm;

import static com.example.midterm.Common.Common.PICK_IMAGE_REQUEST;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {
    private EditText productTitle,productDes,productPrice;
    private Button chooseImage , addProduct;
    private ImageView imageProduct;
    private Spinner categoryId;
    private CheckBox isPopular;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //init UI view
        productTitle = findViewById(R.id.editTextProductTitle);
        productDes=findViewById(R.id.editTextProductDescription);
        productPrice=findViewById(R.id.editTextProductPrice);
        chooseImage=findViewById(R.id.buttonChooseImage);
        addProduct=findViewById(R.id.buttonAddProduct);
        imageProduct=findViewById(R.id.image_Product);
        categoryId=findViewById(R.id.spinnerCategoryId);
        isPopular=findViewById(R.id.checkBoxPopularProduct);


        String[] categories = {"Espresso", "Cappuccino", "Latte", "Mocha", "Americano", "Macchiato"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryId.setAdapter(adapter);


        eventButton();
    }

    private void eventButton() {
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }
    private String productTitleData,productDesData,imageUrl;
    private int productPriceData,categoryIdData;
    private boolean isPopularData;
    private void inputData() {
        //input
        productTitleData = productTitle.getText().toString().trim();
        productDesData = productDes.getText().toString().trim();
        productPriceData = Integer.parseInt(productPrice.getText().toString().trim());
        categoryIdData = categoryId.getSelectedItemPosition() + 1;
        isPopularData = isPopular.isChecked();

        if (productTitleData.isEmpty() || productDesData.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadImageToFirebase(imageUri);
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("images/");
        StorageReference imageRef = storageRef.child("image_" + System.currentTimeMillis() + ".jpg");
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> downloadUrlTask = imageRef.getDownloadUrl();
                    downloadUrlTask.addOnSuccessListener(uri -> {
                        // Sau khi upload thành công, lấy URL và tiếp tục quá trình thêm sản phẩm
                        imageUrl = uri.toString();
                        addProductToDatabase(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi upload hình ảnh
                    Toast.makeText(AddProductActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }

    private void addProductToDatabase(String imageUrl) {
        // Tham chiếu tới "promo" trong Firebase Realtime Database
        DatabaseReference promoRef = FirebaseDatabase.getInstance().getReference("Product");
        // Tạo một object chứa dữ liệu mới
        String mGroupId = promoRef.push().getKey();
        HashMap<String, Object> promoData = new HashMap<>();
        promoData.put("productId", mGroupId);
        promoData.put("categoryId", categoryIdData);
        promoData.put("productDescription", productDesData);
        promoData.put("popularProduct",isPopularData);
        promoData.put("productImagePath",imageUrl);
        promoData.put("productPrice", productPriceData);
        promoData.put("productTitle",productTitleData);
        // Thêm dữ liệu vào Firebase Realtime Database
        promoRef.child(mGroupId).setValue(promoData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Xử lý thành công
                        Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi
                        Toast.makeText(AddProductActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageProduct.setImageURI(imageUri);
        }
    }
}

