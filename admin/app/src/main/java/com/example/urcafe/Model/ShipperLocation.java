package com.example.urcafe.Model;

public class ShipperLocation {
    private double latitude;
    private double longitude;

    public ShipperLocation() {
        // Hàm tạo mặc định được yêu cầu cho Firebase
    }

    public ShipperLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
