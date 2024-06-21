package com.example.midterm.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Order implements Serializable {
    private String orderId;
    private String phone;
    private String name;
    private String address;
    private String status;
    private String total;
    private String totalItems;

    private List<Product> product;
    private String orderDateTime;

    public Order() {

    }

    public Order(String phone, String name, String address, String total, List<Product> product) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.product = product;
        this.status = "0";
        this.orderDateTime = convertDateToString(new Date()); // Lấy ngày giờ hiện tại và chuyển đổi thành chuỗi
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    private String convertDateToString(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy, hh:mm a", Locale.getDefault()).format(date);
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }


    public String calculateTotalItems() {
        int total = 0;
        if (product != null) {
            for (Product p : product) {
                total += p.getNumberInCart();
            }
        }
        // Chuyển đổi tổng số lượng thành String
        return String.valueOf(total);
    }

    // Getter cho tổng số lượng món, tính toán giá trị khi cần thiết
    public String getTotalItems() {
        totalItems = calculateTotalItems();
        return totalItems;
    }

    // Setter cho tổng số lượng món
    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }
}
