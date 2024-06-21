package com.example.urcafe.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String Name, Password, Phone, IsStaff, Email;

    public User() {
    }

    public User(String name, String password, String phone, String isStaff, String email) {
        Name = name;
        Password = password;
        Phone = phone;
        IsStaff = isStaff;
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
