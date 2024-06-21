package com.example.urcafe.Model;

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
    private List<Product>product;
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
        this.orderDateTime=convertDateToString(new Date());
    }

    private String convertDateToString(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy,HH.mm", Locale.getDefault()).format(date);
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
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
}
