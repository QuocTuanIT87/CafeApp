package com.example.urcafe.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.urcafe.Model.Product;

import java.util.ArrayList;

public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(Product item) {
        ArrayList<Product> listProduct = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int y = 0; y < listProduct.size(); y++) {
            if (listProduct.get(y).getProductTitle().equals(item.getProductTitle())) {
                existAlready = true;
                n = y;
                break;
            }
        }
        if (existAlready) {
            listProduct.get(n).setNumberInCart(item.getNumberInCart());
        } else {
            listProduct.add(item);
        }
        tinyDB.putListObject("CartList", listProduct);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Product> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public void minusItem(ArrayList<Product> listProduct, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listProduct.get(position).getNumberInCart() == 1) {
            listProduct.remove(position);
        } else {
            listProduct.get(position).setNumberInCart(listProduct.get(position).getNumberInCart() - 1);
        }
        tinyDB.putListObject("CartList", listProduct);
        changeNumberItemsListener.change();
    }

    public void plusItem(ArrayList<Product> listProduct, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listProduct.get(position).setNumberInCart(listProduct.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", listProduct);
        changeNumberItemsListener.change();
    }


    public Double getTotalFee() {
        ArrayList<Product> listProduct2 = getListCart();
        double fee = 0;
        for (int i = 0; i < listProduct2.size(); i++) {
            fee = fee + (listProduct2.get(i).getProductPrice() * listProduct2.get(i).getNumberInCart());
        }
        return fee;
    }


}
