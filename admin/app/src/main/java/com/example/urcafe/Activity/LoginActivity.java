package com.example.urcafe.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.urcafe.Common.Common;
import com.example.urcafe.Model.User;
import com.example.urcafe.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity {
    EditText userEdt, passEdt;
    CheckBox cbGhiNho;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        userEdt = findViewById(R.id.userEdt);
        passEdt = findViewById(R.id.passEdt);
        cbGhiNho = findViewById(R.id.cbGhiNho);

        setVariable();
    }

    private void setVariable() {
        findViewById(R.id.eyeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType = passEdt.getInputType();
                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Nếu đang hiển thị mật khẩu, chuyển sang chế độ ẩn mật khẩu
                    passEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ((ImageView) v).setImageResource(R.drawable.ic_mat); // Đặt lại hình ảnh của ImageView
                } else {
                    // Nếu đang ẩn mật khẩu, chuyển sang chế độ hiển thị mật khẩu
                    passEdt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ((ImageView) v).setImageResource(R.drawable.ic_mat_gach); // Đặt lại hình ảnh của ImageView
                }
                // Đặt con trỏ ở cuối của văn bản
                passEdt.setSelection(passEdt.getText().length());
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String phone = userEdt.getText().toString();
            String password = passEdt.getText().toString();

            if (!phone.isEmpty() && !password.isEmpty()) {
                // Thực hiện đăng nhập bằng số điện thoại và mật khẩu
                loginUser(phone, password);
            } else {
                Toast.makeText(LoginActivity.this, "Số điện thoại và mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            }
        });

        //Đăng ký ngay
        findViewById(R.id.btnSignUpNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });
    }

    private void loginUser(String phone, String password) {
        // Truy cập vào nút "User" trong Firebase Realtime Database
        DatabaseReference userRef = databaseReference.child(phone);
        // Lắng nghe sự kiện khi dữ liệu thay đổi
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Kiểm tra xem số điện thoại tồn tại trong Firebase Realtime Database
                if (dataSnapshot.exists()) {
                    // Lấy dữ liệu mật khẩu và quyền người dùng từ Firebase
                    String savedPassword = dataSnapshot.child("password").getValue(String.class);
                    String isStaff = dataSnapshot.child("isStaff").getValue(String.class);

                    // So sánh mật khẩu
                    if (password.equals(savedPassword)) {
                        // Kiểm tra quyền người dùng
                        if (isStaff != null && isStaff.equals("0")) {
                            // Lưu thông tin người dùng vào biến Common.currentUser
                            Common.currentUser = dataSnapshot.getValue(User.class);
                            // Lưu thông tin tài khoản nếu người dùng chọn ghi nhớ
                            if (cbGhiNho.isChecked()) {
                                GhiNhoTaiKhoan();
                            }
                            else {
                                XoaGhiNho();
                            }
                            // Chuyển hướng tới HomeActivity
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            // Nếu không có quyền truy cập, hiển thị thông báo
                            Toast.makeText(LoginActivity.this, "Bạn không có quyền truy cập", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Nếu mật khẩu không đúng, hiển thị thông báo
                        Toast.makeText(LoginActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Nếu số điện thoại không tồn tại trong Firebase, hiển thị thông báo
                    Toast.makeText(LoginActivity.this, "Số điện thoại không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(LoginActivity.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void XoaGhiNho() {
        SharedPreferences sharedPreferences = getSharedPreferences("Lưu mật khẩu", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    private void GhiNhoTaiKhoan() {
        SharedPreferences sharedPreferences = getSharedPreferences("Lưu mật khẩu", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Phone", userEdt.getText().toString());
        editor.putString("Password", passEdt.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DocGhiNho();
    }

    private void DocGhiNho() {
        SharedPreferences sharedPreferences = getSharedPreferences("Lưu mật khẩu", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String phone = sharedPreferences.getString("Phone", "");
        String password = sharedPreferences.getString("Password", "");
        userEdt.setText(phone);
        passEdt.setText(password);
        if (!phone.isEmpty() && !password.isEmpty()) {
            cbGhiNho.setChecked(true);
        } else {
            cbGhiNho.setChecked(false);
        }
    }
}