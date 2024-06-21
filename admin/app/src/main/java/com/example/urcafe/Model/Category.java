package com.example.urcafe.Model;

public class Category {
    private int Id;
    private String ImagePath,Name;


    public Category() {
        // Cần thiết cho Firebase
    }

    public Category(int id, String imagePath, String name) {
        Id = id;
        ImagePath = imagePath;
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}


