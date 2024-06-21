package com.example.urcafe.Adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urcafe.Model.Order;
import com.example.urcafe.R;

import java.util.List;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.HistoryOrderViewHolder> {
    private List<Order> historyOrderList;
    private OnItemClickListener listener;

    public HistoryOrderAdapter(List<Order> historyOrderList, OnItemClickListener listener) {
        this.historyOrderList = historyOrderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new HistoryOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryOrderViewHolder holder, int position) {
        Order order = historyOrderList.get(position);

        holder.txtOrderTime.setText(order.getOrderDateTime());
        holder.txtOrderStatus.setText(convertStatus(order.getStatus()));
        holder.txtOrderName.setText(order.getName());

        holder.txtOrderTotals.setText(order.getTotal());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(order);
                }
            }
        });

        // Gán hành động click cho nút btnDetail
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện chuyển hướng đến trang chi tiết sản phẩm activity_detail_order.xml
                // Lưu ý: Đoạn code sau cần phải chỉnh sửa phù hợp với hành động chuyển hướng của bạn
                // Ví dụ: Nếu bạn sử dụng Intent để chuyển hướng, thay "startActivity" bằng "startActivity(intent)"
                if (listener != null) {
                    listener.onDetailButtonClick(order);
                }
            }
        });
    }

    private String convertStatus(String status) {
        switch (status) {
            case "0":
                return "Đang xử lý";
            case "1":
                return "Đang chuẩn bị";
            case "2":
                return "Đang giao hàng";
            case "3":
                return "Đã giao hàng";
            case "-1":
                return "Đã hủy";
            default:
                return "";
        }
    }

    @Override
    public int getItemCount() {
        return historyOrderList.size();
    }
    static class HistoryOrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderTime, txtOrderStatus, txtOrderName, txtOrderQuantity, txtOrderTotals, txtStar;
        Button btnDetail;

        HistoryOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderTime = itemView.findViewById(R.id.order_time);
            txtOrderStatus = itemView.findViewById(R.id.order_status);
            txtOrderName = itemView.findViewById(R.id.order_name);
            txtOrderTotals = itemView.findViewById(R.id.order_total);
            btnDetail = itemView.findViewById(R.id.order_detail);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Order order);

        void onDetailButtonClick(Order order);
    }


}

