package com.example.midterm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUserPhone;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ các trường EditText
        edtUserPhone = findViewById(R.id.edtUserPhone);
        edtPassword = findViewById(R.id.edtUserPassword);

        // Xử lý sự kiện khi nhấn nút đăng nhập
        findViewById(R.id.btnLogin).setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String phone = edtUserPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Kiểm tra xem username và password có trống không
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Truy cập vào nút "User" trong Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(phone);
        // Lắng nghe sự kiện khi dữ liệu thay đổi
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Kiểm tra xem dữ liệu tồn tại
                if (dataSnapshot.exists()) {
                    // Lấy dữ liệu từ Firebase
                    String savedPassword = dataSnapshot.child("password").getValue(String.class);
                    String isStaff = dataSnapshot.child("isStaff").getValue(String.class);

                    // So sánh mật khẩu
                    if (password.equals(savedPassword)) {
                        // Kiểm tra quyền người dùng
                        if (isStaff != null && isStaff.equals("1")) {
                            // Nếu là admin, chuyển hướng tới HomeActivity
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            // Nếu không phải admin, hiển thị thông báo đăng nhập thất bại
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Nếu mật khẩu không đúng, hiển thị thông báo đăng nhập thất bại
                        Toast.makeText(LoginActivity.this, "Sai số điện thoại hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Nếu tên đăng nhập không tồn tại trong Firebase, hiển thị thông báo đăng nhập thất bại
                    Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(LoginActivity.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
