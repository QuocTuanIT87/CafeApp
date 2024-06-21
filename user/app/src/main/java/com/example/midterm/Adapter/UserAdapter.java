package com.example.midterm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm.Model.User;
import com.example.midterm.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private UserAdapter.OnItemClickListener listener;


    public UserAdapter(List<User> userList, UserAdapter.OnItemClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);

        // Set thông tin người dùng vào ViewHolder

        holder.btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            }
        });

        ImageView btnEditUser = holder.itemView.findViewById(R.id.btnEditProduct); // Khai báo btnEditUser ở đây

        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy vị trí của mục được nhấn
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onEditClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserName;
        private TextView txtUserPhone;
        private TextView txtUserEmail;

        private ImageView btnDeleteUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.user_name);
            txtUserPhone = itemView.findViewById(R.id.user_phone);
            txtUserEmail = itemView.findViewById(R.id.user_email);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
        }

        public void bind(User user) {
            txtUserName.setText("Họ tên: " + user.getName());
            txtUserPhone.setText("SĐT: " + user.getPhone());
            txtUserEmail.setText("Email: " + user.getEmail());


        }
    }


    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
