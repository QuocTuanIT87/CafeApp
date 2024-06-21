package com.example.urcafe.Activity;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.urcafe.Model.Feedback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.urcafe.R;

public class FeedBackActivity extends AppCompatActivity {
    private EditText name, phone, email, content;
    private DatabaseReference feedbackRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        // Ánh xạ các view từ layout
        name = findViewById(R.id.editTextName);
        phone = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.editTextEmail);
        content = findViewById(R.id.editTextContent);

        // Khởi tạo đối tượng Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Tham chiếu đến node "feedback"
        feedbackRef = database.getReference("Feedback");

        // Lắng nghe sự kiện khi người dùng gửi phản hồi
        findViewById(R.id.buttonSendFeedback).setOnClickListener(v -> {
            // Lấy giá trị người dùng nhập vào
            String userName = name.getText().toString();
            String userPhone = phone.getText().toString();
            String userEmail = email.getText().toString();
            String userContent = content.getText().toString();

            // Kiểm tra xem người dùng đã nhập thông tin hay chưa
            if (!userName.isEmpty() && !userPhone.isEmpty() && !userEmail.isEmpty() && !userContent.isEmpty()) {
                // Tạo một đối tượng Feedback để lưu vào database
                Feedback feedback = new Feedback(userName, userPhone, userEmail, userContent);
                // Đưa dữ liệu vào Firebase Realtime Database
                feedbackRef.push().setValue(feedback)
                        .addOnSuccessListener(aVoid -> {
                            // Nếu gửi thành công, làm mới trang feedback
                            Toast.makeText(FeedBackActivity.this, "Gửi phản hồi thành công!", Toast.LENGTH_SHORT).show();
                            // Clear input fields
                            name.setText("");
                            phone.setText("");
                            email.setText("");
                            content.setText("");
                            // Refresh activity
                            finish();
                            startActivity(getIntent());
                        })
                        .addOnFailureListener(e -> {
                            // Nếu gửi thất bại, hiển thị thông báo lỗi
                            Toast.makeText(FeedBackActivity.this, "Gửi phản hồi thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Hiển thị thông báo yêu cầu nhập đầy đủ thông tin
                // (Bạn có thể sử dụng Toast hoặc AlertDialog)
            }
        });
    }
}
