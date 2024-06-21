package com.example.midterm.Common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.midterm.Model.User;


public class Common {
    public static User currentUser;
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static final int PICK_IMAGE_REQUEST = 71;

    public static final String baseUrl = "https://maps.googleapis.com";

    public static String convertCodeToStatus(String code){
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "On My Way";
        else
            return "Shipped";
    }
}
