package com.example.pakaianbagus.presentation.home.inputpenjualan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.stock.StokToko;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by alfianhpratama on 07/09/2019.
 * Organization: UTeam
 */
public class PenjualanKompetitorAdapter extends RecyclerView.Adapter<PenjualanKompetitorAdapter.ViewHolder> {
    private List<StokToko> kompetitorList;
    private Context context;
    private Fragment fragment;
    private String date;

    public PenjualanKompetitorAdapter(List<StokToko> kompetitorList, Context context, Fragment fragment, String date) {
        this.kompetitorList = kompetitorList;
        this.context = context;
        this.fragment = fragment;
        this.date = date;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_item_penjualan_kompetitor, viewGroup, false);
        return new PenjualanKompetitorAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat dateFormatView = new SimpleDateFormat("yyyy MMMM dd", Locale.US);
        try {
            Date theDate = dateFormat.parse(date);
            holder.textViewDate.setText(dateFormatView.format(theDate));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.textViewDate.setText(date);
        }

        holder.textViewName.setText(kompetitorList.get(position).getItem().getName());
        holder.textViewHarga.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(kompetitorList.get(position).getTotal()));
        holder.textViewQty.setText(String.valueOf(kompetitorList.get(position).getQty()));
    }

    @Override
    public int getItemCount() {
        return kompetitorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewQty;
        TextView textViewHarga;
        TextView textViewDate;
        LinearLayout layoutPenjualan;

        ViewHolder(View v) {
            super(v);

            textViewName = v.findViewById(R.id.tvNamaBarang);
            textViewQty = v.findViewById(R.id.tvQty);
            textViewHarga = v.findViewById(R.id.tvHarga);
            textViewDate = v.findViewById(R.id.tvDate);
            layoutPenjualan = v.findViewById(R.id.layoutPenjualan);
        }
    }
}
