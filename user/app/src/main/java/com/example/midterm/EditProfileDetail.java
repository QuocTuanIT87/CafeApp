package com.example.midterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.midterm.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileDetail extends AppCompatActivity {

    private EditText userNameEdt, userPhoneEdt, userEmailEdt;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_detail);

        userNameEdt = findViewById(R.id.userName);
        userPhoneEdt = findViewById(R.id.user_phone);
        userEmailEdt = findViewById(R.id.user_email);
        btnSave = findViewById(R.id.btnSave);

        ImageView backBtnUser = findViewById(R.id.backBtnOrder);

        backBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();

        // Nhận dữ liệu người dùng từ Intent
        User userToEdit = (User) intent.getSerializableExtra("USER_TO_EDIT");

        if (userToEdit != null) {
            // Đổ dữ liệu của người dùng vào các trường EditText và Spinner
            userNameEdt.setText(userToEdit.getName());
            userPhoneEdt.setText(userToEdit.getPhone());
            userEmailEdt.setText(userToEdit.getEmail());
            // Đặt trạng thái cho Spinner dựa trên trường isStaff của user

            // Thay đổi tiêu đề của nút "Lưu" thành "Lưu"
            btnSave.setText("Lưu");

            // Thêm sự kiện cho nút "Lưu"
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lấy thông tin từ các trường EditText và Spinner
                    String name = userNameEdt.getText().toString();
                    String phone = userPhoneEdt.getText().toString();
                    String email = userEmailEdt.getText().toString();

                    // Tạo một đối tượng User mới với thông tin đã cập nhật
                    User updatedUser = new User(name, userToEdit.getPassword(), phone, email, "1");

                    // Lưu thông tin đã cập nhật vào Firebase
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

                    // Xoá thông tin người dùng cũ
                    databaseReference.child(userToEdit.getPhone()).setValue(null);

                    // Thêm thông tin người dùng mới
                    databaseReference.child(phone).setValue(updatedUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditProfileDetail.this, "Cập nhật thông tin người dùng thành công", Toast.LENGTH_SHORT).show();
                                        // Sau khi cập nhật thành công, bạn có thể chuyển hướng hoặc thực hiện các thao tác khác ở đây
                                    } else {
                                        Toast.makeText(EditProfileDetail.this, "Cập nhật thông tin người dùng thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        } else {
            // Xử lý khi không nhận được dữ liệu người dùng từ Intent
            Toast.makeText(this, "Không nhận được dữ liệu người dùng", Toast.LENGTH_SHORT).show();
        }
    }
}
