package com.example.urcafe.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
;import com.example.urcafe.Model.Order;
import com.example.urcafe.Model.Product;
import com.example.urcafe.R;
import com.squareup.picasso.Picasso;

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

    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtOrderTime.setText(order.getOrderDateTime());
        holder.txtOrderStatus.setText(convertStatus(order.getStatus()));
        holder.txtOrderName.setText(order.getName());
        holder.txtOrderTotals.setText(order.getTotal());

        // Lấy danh sách sản phẩm từ đơn hàng và hiển thị ảnh của sản phẩm đầu tiên (nếu có)
        List<Product> productList = order.getProduct();
        if (productList != null && !productList.isEmpty()) {
            Picasso.get().load(productList.get(0).getProductImagePath()).into(holder.imageViewFood);
        }

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
        TextView txtOrderTime, txtOrderStatus, txtOrderName, txtOrderQuantity, txtOrderTotals, txtStar;
        Button btnDetail;
        ImageView imageViewFood;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderTime = itemView.findViewById(R.id.order_time);
            txtOrderStatus = itemView.findViewById(R.id.order_status);
            txtOrderName = itemView.findViewById(R.id.order_name);
            txtOrderTotals = itemView.findViewById(R.id.order_total);
            btnDetail = itemView.findViewById(R.id.order_detail);
            imageViewFood=itemView.findViewById(R.id.imageViewFood);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Order order);
        void onDetailButtonClick(Order order);
    }

}
