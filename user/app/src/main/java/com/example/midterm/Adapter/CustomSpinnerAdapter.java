package com.example.midterm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.midterm.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private String[] mValues;
    private int[] mIcons;

    public CustomSpinnerAdapter(Context context, int resource, String[] values, int[] icons) {
        super(context, resource, values);
        mContext = context;
        mValues = values;
        mIcons = icons;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_role, parent, false);

        TextView textView = view.findViewById(R.id.spinner_text);
        ImageView imageView = view.findViewById(R.id.spinner_icon);

        textView.setText(mValues[position]);
        imageView.setImageResource(mIcons[position]);

        return view;
    }
}

