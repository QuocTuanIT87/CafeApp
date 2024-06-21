package com.example.midterm;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.midterm.Adapter.CustomSpinnerAdapter;
import com.example.midterm.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddUserActivity extends AppCompatActivity {
    private EditText passwordEdt, nameEdt, phoneEdt, emailEdt;
    private ImageView eyeBtn;
    private Button btnAdd;
    private TextView txtAddUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        passwordEdt = findViewById(R.id.user_password);
        nameEdt = findViewById(R.id.userName);
        phoneEdt = findViewById(R.id.user_phone);
        emailEdt = findViewById(R.id.user_email);

        eyeBtn = findViewById(R.id.btnEye);
        btnAdd = findViewById(R.id.btnAddUser);
        txtAddUser = findViewById(R.id.txtAddUser);

        ImageView backBtnUser = findViewById(R.id.backBtnOrder);

        backBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Spinner spinner = findViewById(R.id.spinner1);

        int[] icons = {R.drawable.icon_staff, R.drawable.icon_customer};
        String[] accountTypes = getResources().getStringArray(R.array.account_types);

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.item_role, accountTypes, icons);
        spinner.setAdapter(adapter);


        eyeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputType = passwordEdt.getInputType();
                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyeBtn.setImageResource(R.drawable.eye);
                } else {
                    passwordEdt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyeBtn.setImageResource(R.drawable.eye_crossed);
                }
                passwordEdt.setSelection(passwordEdt.getText().length());
            }
        });

        // Lấy Intent
        Intent intent = getIntent();
        if (intent.hasExtra("USER_TO_EDIT")) {
            // Người dùng muốn chỉnh sửa thông tin của một user đã tồn tại

            // Nhận đối tượng User từ Intent
            User userToEdit = (User) intent.getSerializableExtra("USER_TO_EDIT");

            // Đổ dữ liệu của người dùng cần chỉnh sửa vào các trường EditText và Spinner
            nameEdt.setText(userToEdit.getName());
            phoneEdt.setText(userToEdit.getPhone());
            passwordEdt.setText(userToEdit.getPassword());
            emailEdt.setText(userToEdit.getEmail());
            // Đặt trạng thái cho Spinner dựa trên trường isStaff của user
            if (userToEdit.getIsStaff().equals("1")) {
                spinner.setSelection(0); // Nhân viên
            } else {
                spinner.setSelection(1); // Khách hàng
            }

            // Đổi tiêu đề của button "Thêm" thành "Lưu"
            btnAdd.setText("Lưu");

            txtAddUser.setText("Chỉnh sửa thông tin");

            // Thay đổi phần xử lý sự kiện của nút "Lưu"
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = nameEdt.getText().toString();
                    String phone = phoneEdt.getText().toString();
                    String email = emailEdt.getText().toString();
                    String isStaff = spinner.getSelectedItemPosition() == 0 ? "1" : "0"; // 1: Nhân viên, 0: Khách hàng

                    // Tạo một đối tượng User mới với thông tin đã chỉnh sửa
                    User updatedUser = new User(name, userToEdit.getPassword(), phone, isStaff, email);

                    // Lưu thông tin đã cập nhật vào Firebase
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

                    // Xoá đơn hàng cũ trước khi thêm đơn hàng mới
                    databaseReference.child(userToEdit.getPhone()).removeValue();

                    // Thêm đơn hàng mới
                    databaseReference.child(phone).setValue(updatedUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddUserActivity.this, "Cập nhật thông tin người dùng thành công", Toast.LENGTH_SHORT).show();
                                        // Sau khi cập nhật thành công, bạn có thể chuyển hướng hoặc thực hiện các thao tác khác ở đây
                                    } else {
                                        Toast.makeText(AddUserActivity.this, "Cập nhật thông tin người dùng thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });


        }
        else {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String password = passwordEdt.getText().toString();
                    String name = nameEdt.getText().toString();
                    String phone = phoneEdt.getText().toString();
                    String email = emailEdt.getText().toString();
                    String isStaff = spinner.getSelectedItemPosition() == 0 ? "1" : "0"; // 1: Nhân viên, 0: Khách hàng

                    // Kiểm tra xem tất cả các trường đều được nhập
                    if (password.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                        Toast.makeText(AddUserActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Kiểm tra mật khẩu có đủ độ dài
                    if (password.length() < 6) {
                        Toast.makeText(AddUserActivity.this, "Mật khẩu phải ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Tạo một đối tượng User
                    User user = new User(name, password, phone, isStaff, email);

                    // Ghi dữ liệu vào Firebase với key là số điện thoại
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(phone);
                    databaseReference.setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddUserActivity.this, "Thêm người dùng thành công", Toast.LENGTH_SHORT).show();
                                        // Sau khi thêm thành công, bạn có thể chuyển hướng hoặc thực hiện các thao tác khác ở đây
                                    } else {
                                        Toast.makeText(AddUserActivity.this, "Thêm người dùng thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        }
    }
}
