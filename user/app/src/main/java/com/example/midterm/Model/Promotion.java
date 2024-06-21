package com.example.midterm.Model;

import java.io.Serializable;

public class Promotion implements Serializable {
    String promoCode,description,promoPrice,minimumOrderPrice,expireDate, promoId;
    public Promotion() {
    }

    public Promotion(String promoCode, String description, String promoPrice, String minimumOrderPrice, String expireDate, String promoId) {
        this.promoCode = promoCode;
        this.description = description;
        this.promoPrice = promoPrice;
        this.minimumOrderPrice = minimumOrderPrice;
        this.expireDate = expireDate;
        this.promoId = promoId;
    }

    public String getPromoId() {
        return promoId;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(String promoPrice) {
        this.promoPrice = promoPrice;
    }

    public String getMinimumOrderPrice() {
        return minimumOrderPrice;
    }

    public void setMinimumOrderPrice(String minimumOrderPrice) {
        this.minimumOrderPrice = minimumOrderPrice;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
