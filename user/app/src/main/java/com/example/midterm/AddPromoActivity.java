package com.example.midterm;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddPromoActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private EditText promoCode,promoDes,promoPrice,promoMinimumPrice,expireDate;
    private Button addBtn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promo);

        //init ui view
        backBtn=findViewById(R.id.backBtn);
        promoCode=findViewById(R.id.inputPromoCode);
        promoDes=findViewById(R.id.inputPromoDes);
        promoPrice=findViewById(R.id.inputPromoPrice);
        promoMinimumPrice=findViewById(R.id.inputPromoMinimumPrice);
        expireDate=findViewById(R.id.inputExpireDate);
        addBtn=findViewById(R.id.addBtn);

        eventButton();

    }

    private void eventButton() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        expireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickDialog();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }

    private void datePickDialog() {
        Calendar c = Calendar.getInstance();
        int mYear=c.get(Calendar.YEAR);
        int mMonth=c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DecimalFormat mFormat = new DecimalFormat("00");
                String pDay = mFormat.format(dayOfMonth);
                String pMonth = mFormat.format(monthOfYear+1);
                String pYear= ""+year;
                String pDate = pDay+"/"+pMonth+"/"+pYear;
                expireDate.setText(pDate);
            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
    }
    private String descriptionData,promoCodeData, promoPriceData, minimumOrderPriceData,expireDateData;
    private void inputData(){
        //input
        promoCodeData=promoCode.getText().toString().trim();
        descriptionData=promoDes.getText().toString().trim();
        promoPriceData=promoPrice.getText().toString().trim();
        minimumOrderPriceData=promoMinimumPrice.getText().toString().trim();
        expireDateData=expireDate.getText().toString().trim();

        //validate for data
        if(TextUtils.isEmpty(promoCodeData)){
            Toast.makeText(this,"Nhập mã giảm giá",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(descriptionData)){
            Toast.makeText(this,"Nhập mô tả",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(promoPriceData)){
            Toast.makeText(this,"Nhập số tiền giảm giá",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(minimumOrderPriceData)){
            Toast.makeText(this,"Nhập điều kiện giảm giá",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(expireDateData)){
            Toast.makeText(this,"Nhập thời gian hiệu lưc",Toast.LENGTH_SHORT).show();
            return;
        }

        addtoDB();
    }

    private void addtoDB() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang thêm mã giảm giá");
        progressDialog.show();

        // Tham chiếu tới "promo" trong Firebase Realtime Database
        DatabaseReference promoRef = FirebaseDatabase.getInstance().getReference("Promotion");
        String mGroupId = promoRef.push().getKey();
        // Tạo một object chứa dữ liệu mới
        HashMap<String, Object> promoData = new HashMap<>();
        promoData.put("promoId", mGroupId);
        promoData.put("promoCode", promoCodeData);
        promoData.put("description", descriptionData);
        promoData.put("promoPrice", promoPriceData);
        promoData.put("minimumOrderPrice", minimumOrderPriceData);
        promoData.put("expireDate", expireDateData);
        // Thêm dữ liệu vào Firebase Realtime Database
        promoRef.child(mGroupId).setValue(promoData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Xử lý thành công
                        progressDialog.dismiss();
                        Toast.makeText(AddPromoActivity.this, "Thêm mã giảm giá thành công", Toast.LENGTH_SHORT).show();
                        // Reset các trường nhập liệu
                        resetInputFields();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi
                        progressDialog.dismiss();
                        Toast.makeText(AddPromoActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void resetInputFields() {
        promoCode.setText("");
        promoDes.setText("");
        promoPrice.setText("");
        promoMinimumPrice.setText("");
        expireDate.setText("");
    }
}