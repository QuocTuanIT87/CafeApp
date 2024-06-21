package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView homeBtn, accBtn, logoutBtn;

    private Button btnChangePass, btnEditPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        btnChangePass = (Button) findViewById(R.id.btnChangePass);
        btnEditPro = (Button) findViewById(R.id.btnEditPro);

        homeBtn = (ImageView) findViewById(R.id.btnHome);
        accBtn = (ImageView) findViewById(R.id.btnAcc);
        logoutBtn = (ImageView) findViewById(R.id.btnLogOut);
        setVariable();
    }

    private void setVariable() {

        //Trang chu
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this,HomeActivity.class));
            }
        });
        //Tai khoan
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this,EditProfileActivity.class));
            }
        });
        // Dang xuat
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(EditProfileActivity.this, LoginActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
            }
        });

        //Edit
        btnEditPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditProfileDetail.class));
            }
        });

        //Change
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this,HomeActivity.class));
            }
        });
    }
}