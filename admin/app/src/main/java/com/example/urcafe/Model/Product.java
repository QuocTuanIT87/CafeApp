package com.example.urcafe.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private String productTitle, productDescription, productImagePath,productId;
    private int categoryId, numberInCart;
    private int productPrice;
    private boolean popularProduct;
    private String productTotal; // Tổng số tiền của sản phẩm

    public Product() {
    }

    public Product(String productTitle, String productDescription, String productImagePath, int categoryId, String productId, int numberInCart, int productPrice, boolean popularProduct) {
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

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
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
}
