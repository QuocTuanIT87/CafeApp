package com.example.midterm.Interface;

import android.view.View;

import com.example.midterm.Model.Order;

public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);

    void onClick(Order order);
}
