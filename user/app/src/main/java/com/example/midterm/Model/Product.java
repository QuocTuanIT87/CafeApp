package com.example.midterm.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private String productTitle, productDescription, productImagePath, productId;
    private int categoryId, numberInCart;
    private double productPrice;
    private boolean popularProduct;
    private String productTotal; // Tổng số tiền của sản phẩm
    private boolean status;


    public Product() {
    }

    public Product(String productTitle, String productDescription, String productImagePath, int categoryId, String productId, int numberInCart, double productPrice, boolean popularProduct) {
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.productImagePath = productImagePath;
        this.categoryId = categoryId;
        this.productId = productId;
        this.numberInCart = numberInCart;
        this.productPrice = productPrice;
        this.popularProduct = popularProduct;
        this.productTotal = calculateProductTotal(); // Tính toán tổng số tiền khi khởi tạo sản phẩm
    }

    public Product(String productTitle, String productImagePath, double productPrice) {
        this.productTitle = productTitle;
        this.productImagePath = productImagePath;
        this.productPrice = productPrice;
    }

    public Product(String productId, String productTitle, String productImagePath, double productPrice) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.productImagePath = productImagePath;
        this.productPrice = productPrice;
    }
    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
        // Khi số lượng trong giỏ hàng thay đổi, tính lại tổng số tiền
        this.productTotal = calculateProductTotal();
    }



    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
        // Khi giá sản phẩm thay đổi, tính lại tổng số tiền
        this.productTotal = calculateProductTotal();
    }

    public boolean isPopularProduct() {
        return popularProduct;
    }

    public void setPopularProduct(boolean popularProduct) {
        this.popularProduct = popularProduct;
    }

    // Phương thức tính tổng số tiền của sản phẩm
    private String calculateProductTotal() {
        return String.valueOf(numberInCart * productPrice);
    }

    public String getProductTotal() {
        return productTotal;
    }

    public void setProductTotal(String productTotal) {
        this.productTotal = productTotal;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
