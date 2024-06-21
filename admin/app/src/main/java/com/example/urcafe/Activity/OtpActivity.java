package com.example.urcafe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.urcafe.Model.User;
import com.example.urcafe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import papaya.in.sendmail.SendMail;

public class OtpActivity extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private TextView tvResend;
    private String name, phone, address, email;
    private int random;
    private CountDownTimer resendTimer;
    private CountDownTimer otpTimer;
    private boolean isResendEnabled = true;
    private boolean isOtpExpired = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        tvResend = findViewById(R.id.tvResend);
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        address = getIntent().getStringExtra("address");
        email = getIntent().getStringExtra("email");

        random();

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isResendEnabled) {
                    random();
                    startResendTimer();
                } else {
                    Toast.makeText(OtpActivity.this, "Please wait 60s before requesting OTP again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP();
            }
        });
    }

    public void random() {
        Random rand = new Random();
        random = rand.nextInt(900000) + 100000;
        String mailBody = "Your OTP is -> " + random;

        SendMail mail = new SendMail("caohuuquoc462002@gmail.com", "ixdg zqni xpbp boka", email, "Login Signup app's OTP", mailBody);
        mail.execute();
        startOtpTimer();
        // Update OTP expiry timer
        startOtpTimer();

        // Reset the OTP expiry status
        isOtpExpired = false;
    }

    private void startResendTimer() {
        isResendEnabled = false;
        resendTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvResend.setText("Gửi lại OTP (" + millisUntilFinished / 1000 + "s)");
            }

            @Override
            public void onFinish() {
                isResendEnabled = true;
                tvResend.setText("Gửi lại OTP");            }
        }.start();
    }

    private void startOtpTimer() {
        otpTimer = new CountDownTimer(180000, 1000) { // 3 minutes
            @Override
            public void onTick(long millisUntilFinished) {
                // OTP is still valid
            }

            @Override
            public void onFinish() {
                isOtpExpired = true;
            }
        }.start();
    }

    private void verifyOTP() {
        String otp = otp1.getText().toString() +
                otp2.getText().toString() +
                otp3.getText().toString() +
                otp4.getText().toString() +
                otp5.getText().toString() +
                otp6.getText().toString();

        if (TextUtils.isEmpty(otp) || otp.length() != 6) {
            Toast.makeText(this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Integer.parseInt(otp) != random) {
            Toast.makeText(this, "Incorrect OTP. Please try again", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isOtpExpired) {
            Toast.makeText(this, "OTP has expired. Please request a new one", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proceed with other operations like saving user details to database

        // For demonstration purpose, I'm just displaying a toast message
        Toast.makeText(OtpActivity.this, "User registered successfully\nName: " + name + "\nPhone: " + phone + "\nAddress: " + address, Toast.LENGTH_LONG).show();

        // Navigate to login activity
        startActivity(new Intent(OtpActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (resendTimer != null) {
            resendTimer.cancel();
        }
        if (otpTimer != null) {
            otpTimer.cancel();
        }
    }
}
