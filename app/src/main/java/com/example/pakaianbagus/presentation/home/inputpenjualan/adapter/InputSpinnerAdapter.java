package com.example.pakaianbagus.presentation.home.inputpenjualan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pakaianbagus.models.Discount;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by alfianhpratama on 05/09/2019.
 * Organization: UTeam
 */
public class InputSpinnerAdapter extends ArrayAdapter<Discount> {

    private List<Discount> objects;

    public InputSpinnerAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Discount> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Nullable
    @Override
    public Discount getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return objects.get(position).getId();
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView result = (TextView) super.getView(position, convertView, parent);
        result.setTextSize(12);
        try {
            if (objects.get(position).getType().equals("1"))
                result.setText(objects.get(position).getValue() + "%");
            else
                result.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(objects.get(position).getValue())));
        } catch (Exception e) {
            result.setText(objects.get(position).getValue());
        }
        return result;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView result = (TextView) super.getDropDownView(position, convertView, parent);
        result.setTextSize(16);
        result.setText(objects.get(position).getName());
        return result;
    }
}
