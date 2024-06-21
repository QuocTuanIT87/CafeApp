package com.example.urcafe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.urcafe.R;

public class Information extends AppCompatActivity {
    private ImageView locationBtn;
    private Button feedBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        locationBtn=findViewById(R.id.locationBtn);
        feedBackBtn=findViewById(R.id.feedBackBtn);

        eventButton();
    }

    private void eventButton() {
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Information.this,MapsActivity.class);
                startActivity(intent);
            }
        });
        feedBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Information.this,FeedBackActivity.class);
                startActivity(i);
            }
        });

    }
}