package com.example.midterm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.midterm.Model.Order;
import com.example.midterm.R;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private OnItemClickListener listener;

    public OrderAdapter(List<Order> orderList, OnItemClickListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtOrderTime.setText(order.getOrderDateTime());
        holder.txtOrderStatus.setText(convertStatus(order.getStatus()));
        holder.txtOrderId.setText("ID: " + order.getOrderId());
        holder.txtOrderName.setText(order.getName());
        holder.txtOrderQuantity.setText(order.getTotalItems() + " món");
        holder.txtStar.setText("•");
        holder.txtOrderTotals.setText(order.getTotal());

        // Gán hành động click cho itemView (toàn bộ item)
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
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderTime, txtOrderStatus, txtOrderName, txtOrderQuantity, txtOrderTotals, txtStar, txtOrderId;
        Button btnDetail;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderTime = itemView.findViewById(R.id.order_time);
            txtOrderStatus = itemView.findViewById(R.id.order_status);
            txtOrderName = itemView.findViewById(R.id.order_name);
            txtOrderQuantity = itemView.findViewById(R.id.order_quantity);
            txtStar = itemView.findViewById(R.id.textView);
            txtOrderTotals = itemView.findViewById(R.id.order_total);
            btnDetail = itemView.findViewById(R.id.order_detail);
            txtOrderId = itemView.findViewById(R.id.order_id);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Order order);
        void onDetailButtonClick(Order order);
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

