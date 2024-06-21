package com.example.urcafe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;

import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.urcafe.Model.User;
import com.example.urcafe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_register);

        // Khởi tạo DatabaseReference để tham chiếu đến nơi lưu trữ thông tin người dùng
        usersRef = FirebaseDatabase.getInstance().getReference().child("User");

        setVariable();
    }

    private void setVariable() {
        findViewById(R.id.eyeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText passwordEdt = findViewById(R.id.passwordEdt);
                int inputType = passwordEdt.getInputType();
                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Nếu đang hiển thị mật khẩu, chuyển sang chế độ ẩn mật khẩu
                    passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ((ImageView) v).setImageResource(R.drawable.ic_mat); // Đặt lại hình ảnh của ImageView
                } else {
                    // Nếu đang ẩn mật khẩu, chuyển sang chế độ hiển thị mật khẩu
                    passwordEdt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ((ImageView) v).setImageResource(R.drawable.ic_mat_gach); // Đặt lại hình ảnh của ImageView
                }
                // Đặt con trỏ ở cuối của văn bản
                passwordEdt.setSelection(passwordEdt.getText().length());
            }
        });

        findViewById(R.id.btnSignUp).setOnClickListener(v -> {
            EditText emailEdt = findViewById(R.id.emailEdt);
            EditText passwordEdt = findViewById(R.id.passwordEdt);
            EditText nameEdt = findViewById(R.id.nameEdt);
            EditText phoneEdt = findViewById(R.id.phoneEdt);

            String email = emailEdt.getText().toString();
            String password = passwordEdt.getText().toString();
            String name = nameEdt.getText().toString();
            String phone = phoneEdt.getText().toString();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            } else if (password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Mật khẩu phải ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra xem email đã tồn tại trong database chưa
            checkEmailExists(email, password, name, phone);
        });

        //Đăng ký ngay
        findViewById(R.id.btnSignInNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void checkEmailExists(String email, String password, String name, String phone) {
        usersRef.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Số điện thoại đã tồn tại trong Realtime Database
                    Toast.makeText(RegisterActivity.this, "Số điện thoại đã tồn tại. Vui lòng sử dụng số điện thoại khác.", Toast.LENGTH_SHORT).show();
                } else {
                    // Số điện thoại chưa tồn tại trong Realtime Database, tiến hành đăng ký
                    // Tạo một đối tượng User mới
                    User newUser = new User(name, password, phone, "0", email);

                    // Thêm người dùng mới vào Firebase Realtime Database với số điện thoại làm key
                    usersRef.child(phone).setValue(newUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Đăng ký thành công, chuyển sang OtpActivity
                                    Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                                    intent.putExtra("email", email);
                                    intent.putExtra("password", password);
                                    intent.putExtra("name", name);
                                    intent.putExtra("phone", phone);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Xử lý khi có lỗi xảy ra trong quá trình thêm dữ liệu vào Firebase
                                    Toast.makeText(RegisterActivity.this, "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Realtime Database
                Toast.makeText(RegisterActivity.this, "Đã xảy ra lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
