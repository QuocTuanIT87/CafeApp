package com.example.midterm;

import android.annotation.SuppressLint;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.midterm.Model.Product;
import com.example.midterm.Model.Promotion;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditPromotion extends AppCompatActivity {

    private ImageButton backBtn;
    private EditText promoCode, promoDes, promoPrice, promoMinimumPrice, expireDate;
    private Button updateBtn;
    ProgressDialog progressDialog;
    String idPromotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_promo);

        //init ui view
        backBtn = findViewById(R.id.backBtn);
        promoCode = findViewById(R.id.inputPromoCode);
        promoDes = findViewById(R.id.inputPromoDes);
        promoPrice = findViewById(R.id.inputPromoPrice);
        promoMinimumPrice = findViewById(R.id.inputPromoMinimumPrice);
        expireDate = findViewById(R.id.inputExpireDate);
        updateBtn = findViewById(R.id.updateBtn);

        eventButton();
        loadDataObject();
    }

    public void loadDataObject() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        Promotion promotion = (Promotion) bundle.get("object_promotion");
        promoCode.setText(promotion.getPromoCode());
        promoDes.setText(promotion.getDescription());
        promoPrice.setText(promotion.getPromoPrice());
        promoMinimumPrice.setText(promotion.getMinimumOrderPrice());
        expireDate.setText(promotion.getExpireDate());
        idPromotion = promotion.getPromoId();
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
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
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

    private void updateData(){
        Map<String, Object> map = new HashMap<>();
        map.put("promoCode", promoCode.getText().toString());
        map.put("description",promoDes.getText().toString());
        map.put("promoPrice", promoPrice.getText().toString());
        map.put("minimumOrderPrice", promoMinimumPrice.getText().toString());
        map.put("expireDate", expireDate.getText().toString());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Promotion/" + idPromotion);

        myRef.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(EditPromotion.this, "Update promotion successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
