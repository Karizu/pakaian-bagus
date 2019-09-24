package com.example.pakaianbagus.presentation.home.inputpenjualan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.Discount;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.presentation.home.inputpenjualan.InputPenjualan;
import com.example.pakaianbagus.util.RoundedCornersTransformation;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sCorner;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sMargin;

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.ViewHolder> {
    private List<Stock> stockList;
    private List<Discount> discounts;
    private Context context;
    private Fragment fragment;

    public SalesReportAdapter(List<Stock> stockList, Context context, Fragment fragment, List<Discount> discounts) {
        this.stockList = stockList;
        this.context = context;
        this.fragment = fragment;
        this.discounts = discounts;
    }

    @NonNull
    @Override
    public SalesReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_sales_report, parent, false);

        return new SalesReportAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SalesReportAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvNamaBarang.setText(stockList.get(position).getItem().getName());
        holder.tvHarga.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(stockList.get(position).getPrice()));
        holder.tvQty.setText(String.valueOf(stockList.get(position).getQty()));
        holder.total = stockList.get(position).getPrice() * stockList.get(position).getQty();

        Glide.with(context)
                .applyDefaultRequestOptions(
                        new RequestOptions().placeholder(R.drawable.jeans).error(R.drawable.jeans))
                .load(stockList.get(position).getItem().getImage())
                .apply(RequestOptions.bitmapTransform(
                        new RoundedCornersTransformation(context, sCorner, sMargin)))
                .into(holder.imgBarang);

        final InputSpinnerAdapter adapter = new InputSpinnerAdapter(context, android.R.layout.simple_spinner_item, discounts);
        holder.spDiskon.setAdapter(adapter);
        holder.spDiskon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {
                if (discounts.get(index).getType().equals("2")) {
                    holder.total = Integer.parseInt(discounts.get(index).getValue()) * stockList.get(position).getQty();
                } else {
                    int dis = (stockList.get(position).getPrice() * Integer.parseInt(discounts.get(index).getValue()) / 100);
                    holder.total = (stockList.get(position).getPrice() - dis) * stockList.get(position).getQty();
                }

                holder.tvTotal.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(holder.total));

                stockList.get(position).setTotal(holder.total);

                ((InputPenjualan) fragment).salesData(stockList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (fragment instanceof InputPenjualan) {
            holder.tvDiskon.setVisibility(View.GONE);
            holder.spDiskon.setVisibility(View.VISIBLE);
        } else {
            holder.tvDiskon.setVisibility(View.VISIBLE);
            holder.spDiskon.setVisibility(View.GONE);
        }

        holder.tvTotal.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(holder.total));

    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaBarang;
        TextView tvHarga;
        TextView tvQty;
        TextView tvTotal;
        ImageView imgBarang;
        ImageView btnMore;
        Spinner spDiskon;
        TextView tvDiskon;
        LinearLayout layoutListBarangMasuk;

        private int total = 0;

        ViewHolder(View v) {
            super(v);
            tvNamaBarang = v.findViewById(R.id.tvNamaBarang);
            tvHarga = v.findViewById(R.id.tvHarga);
            tvTotal = v.findViewById(R.id.tvTotal);
            tvQty = v.findViewById(R.id.tvQty);
            imgBarang = v.findViewById(R.id.imgBarang);
            btnMore = v.findViewById(R.id.btnMore);
            spDiskon = v.findViewById(R.id.spDiskon);
            tvDiskon = v.findViewById(R.id.tvDiskon);
            layoutListBarangMasuk = v.findViewById(R.id.layoutListBarangMasuk);
        }
    }
}
