package com.example.midterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.midterm.Adapter.UserAdapter;
import com.example.midterm.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserManagement extends AppCompatActivity implements UserAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> userList;
    private List<User> customerList;
    private List<User> staffList;
    private Button btnCustomer, btnStaff, btnDialogCancel, btnDialogDelete;

    private Dialog dialogDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        // Khởi tạo danh sách khách hàng và danh sách nhân viên
        userList = new ArrayList<>();
        customerList = new ArrayList<>();
        staffList = new ArrayList<>();

        recyclerView = findViewById(R.id.userView);
        btnCustomer = findViewById(R.id.btnCustomer);
        btnStaff = findViewById(R.id.btnStaff);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo adapter và thiết lập cho RecyclerView
        adapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(adapter);

        ImageView backBtnUser = findViewById(R.id.backBtnOrder);
        ImageView btnAddUser = findViewById(R.id.btnAddUser);

        loadUser(); // Thay vì loadOrder, ta sẽ loadUser ở đây

        // Xử lý sự kiện khi nhấn nút "Khách hàng"
        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomerPage();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Nhân viên"
        btnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStaffPage();
            }
        });

        // Mặc định hiển thị trang khách hàng khi mở ứng dụng
        showStaffPage();

        backBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Bắt sự kiện khi người dùng nhấn nút thêm user
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển từ UserManagement sang AddUserActivity
                Intent intent = new Intent(UserManagement.this, AddUserActivity.class);
                startActivity(intent); // Bắt đầu AddUserActivity
            }
        });
    }

    private void loadUser() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("User");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                customerList.clear();
                staffList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    String phone = snapshot.getKey();

                    // Sử dụng thuộc tính isStaff để phân loại người dùng
                    if (user != null) {
                        if ("1".equals(user.getIsStaff())) {
                            // Thêm vào danh sách nhân viên
                            staffList.add(user);
                        } else {
                            // Thêm vào danh sách khách hàng
                            customerList.add(user);
                        }
                    }
                }

                // Hiển thị trang khách hàng mặc định
                showStaffPage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }

    private void showCustomerPage() {
        // Hiển thị danh sách khách hàng và ẩn danh sách nhân viên
        userList.clear();
        userList.addAll(customerList); // Load danh sách khách hàng từ Firebase
        adapter.notifyDataSetChanged();

        // Đặt màu sáng cho nút "Khách hàng"
        btnCustomer.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        // Đặt màu tắt cho nút "Nhân viên"
        btnStaff.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    // Phương thức hiển thị giao diện cho trang "Nhân viên"
    private void showStaffPage() {
        // Hiển thị danh sách nhân viên và ẩn danh sách khách hàng
        userList.clear();
        userList.addAll(staffList); // Load danh sách nhân viên từ Firebase
        adapter.notifyDataSetChanged();

        // Đặt màu sáng cho nút "Nhân viên"
        btnStaff.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        // Đặt màu tắt cho nút "Khách hàng"
        btnCustomer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public void onDeleteClick(int position) {
        showDialogDelete(position);
    }

    // Phương thức để hiển thị dialog xoá
    private void showDialogDelete(int position) {
        dialogDelete = new Dialog(UserManagement.this);
        dialogDelete.setContentView(R.layout.dialog_delete);
        dialogDelete.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogDelete.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialogDelete.setCancelable(false);
        dialogDelete.show();

        // Ánh xạ các view trong dialog
        btnDialogCancel = dialogDelete.findViewById(R.id.btnDialogCancel);
        btnDialogDelete = dialogDelete.findViewById(R.id.btnDialogDelete);

        // Xử lý sự kiện khi nhấn nút "Hủy"
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng dialog khi nhấn nút "Hủy"
                dialogDelete.dismiss();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Xoá"
        btnDialogDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User deletedUser = userList.get(position);
                String userPhone = deletedUser.getPhone();

                // Xoá người dùng khỏi Firebase
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(userPhone);
                userRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Xoá thành công từ Firebase, tiếp theo xoá khỏi danh sách hiển thị
                            userList.remove(position);
                            adapter.notifyItemRemoved(position);
                            Toast.makeText(UserManagement.this, "Xoá người dùng thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserManagement.this, "Xoá người dùng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Đóng dialog sau khi xoá
                dialogDelete.dismiss();
            }
        });
    }

    @Override
    public void onEditClick(int position) {
        User userToEdit = userList.get(position);

        // Chuyển sang trang chỉnh sửa thông tin user, truyền thông tin user cần chỉnh sửa
        Intent intent = new Intent(this, AddUserActivity.class);
        intent.putExtra("USER_TO_EDIT", (Serializable) userToEdit);
        startActivity(intent);
    }


    @Override
    public void onItemClick(int position) {
        // Xử lý sự kiện khi người dùng nhấn vào một mục trong RecyclerView (nếu cần)
    }


}
