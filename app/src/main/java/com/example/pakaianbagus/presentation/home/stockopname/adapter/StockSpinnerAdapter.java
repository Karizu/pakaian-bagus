package com.example.pakaianbagus.presentation.home.stockopname.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pakaianbagus.models.api.CategoryResponse;
import com.example.pakaianbagus.models.stock.Stock;

import java.util.List;

/**
 * Created by alfianhpratama on 12/09/2019.
 * Organization: UTeam
 */
public class StockSpinnerAdapter extends ArrayAdapter<CategoryResponse> {

    private List<CategoryResponse> objects;

    public StockSpinnerAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<CategoryResponse> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Nullable
    @Override
    public CategoryResponse getItem(int position) {
        return objects.get(position);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView result = (TextView) super.getView(position, convertView, parent);
        result.setTextSize(14);
        result.setText(objects.get(position).getCategory().getName());
        return result;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView result = (TextView) super.getDropDownView(position, convertView, parent);
        result.setText(objects.get(position).getCategory().getName());
        return result;
    }
}
