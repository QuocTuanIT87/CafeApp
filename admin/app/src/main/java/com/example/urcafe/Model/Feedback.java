package com.example.urcafe.Model;

public class Feedback {
    private String name;
    private String phone;
    private String email;
    private String content;

    // Constructor mặc định (cần thiết cho Firebase)
    public Feedback() {
    }

    // Constructor có tham số
    public Feedback(String name, String phone, String email, String content) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.content = content;
    }

    // Getter và Setter cho các thuộc tính
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}