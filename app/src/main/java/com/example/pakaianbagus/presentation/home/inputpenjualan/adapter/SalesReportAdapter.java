package com.example.pakaianbagus.presentation.home.inputpenjualan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.BarangMasukModel;
import com.example.pakaianbagus.models.Discount;
import com.example.pakaianbagus.models.stock.StokToko;
import com.example.pakaianbagus.presentation.barangmasuk.BarangMasukFragment;
import com.example.pakaianbagus.presentation.home.inputpenjualan.InputPenjualan;
import com.example.pakaianbagus.util.RoundedCornersTransformation;
import com.example.pakaianbagus.util.adapter.SpinnerAdapter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sCorner;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sMargin;

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.ViewHolder> {
    private List<StokToko> stokTokoList;
    private List<Discount> discounts;
    private Context context;
    private Fragment fragment;

    public SalesReportAdapter(List<StokToko> stokTokoList, Context context, Fragment fragment, List<Discount> discounts) {
        this.stokTokoList = stokTokoList;
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
        holder.tvNamaBarang.setText(stokTokoList.get(position).getItem().getName());
        holder.tvHarga.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(stokTokoList.get(position).getPrice()));
        holder.tvQty.setText(String.valueOf(stokTokoList.get(position).getQty()));

        Glide.with(context)
                .applyDefaultRequestOptions(
                        new RequestOptions().placeholder(R.drawable.jeans).error(R.drawable.jeans))
                .load(stokTokoList.get(position).getItem().getImage())
                .apply(RequestOptions.bitmapTransform(
                        new RoundedCornersTransformation(context, sCorner, sMargin)))
                .into(holder.imgBarang);

        final SpinnerAdapter adapter = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, discounts);
        holder.spDiskon.setAdapter(adapter);

        holder.total = stokTokoList.get(position).getPrice() * stokTokoList.get(position).getQty();

        holder.spDiskon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {
                if (discounts.get(index).getType().equals("2")) {
                    holder.total = Integer.parseInt(discounts.get(index).getValue());
                } else {
                    int dis = (stokTokoList.get(position).getPrice() * Integer.parseInt(discounts.get(index).getValue()) / 100);
                    holder.total = (stokTokoList.get(position).getPrice() - dis) * stokTokoList.get(position).getQty();
                }

                holder.tvTotal.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(holder.total));

                stokTokoList.get(position).setTotal(holder.total);

                ((InputPenjualan) fragment).salesData(stokTokoList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.tvTotal.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(holder.total));
        stokTokoList.get(position).setTotal(holder.total);
    }

    @Override
    public int getItemCount() {
        return stokTokoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaBarang;
        TextView tvHarga;
        TextView tvQty;
        TextView tvTotal;
        ImageView imgBarang;
        ImageView btnMore;
        Spinner spDiskon;
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
            layoutListBarangMasuk = v.findViewById(R.id.layoutListBarangMasuk);
        }
    }
}
