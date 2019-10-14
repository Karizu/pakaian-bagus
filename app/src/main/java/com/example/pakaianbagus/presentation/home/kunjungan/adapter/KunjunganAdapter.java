package com.example.pakaianbagus.presentation.home.kunjungan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.KunjunganModel;
import com.example.pakaianbagus.presentation.home.kunjungan.KunjunganFragment;
import com.example.pakaianbagus.util.Constanta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class KunjunganAdapter extends RecyclerView.Adapter<KunjunganAdapter.ViewHolder> {
    private List<KunjunganModel> kunjunganModels;
    private Context context;
    private Fragment fragment;

    public KunjunganAdapter(List<KunjunganModel> kunjunganModels, Context context, Fragment fragment) {
        this.kunjunganModels = kunjunganModels;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public KunjunganAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_mutasi_barang, parent, false);

        return new KunjunganAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KunjunganAdapter.ViewHolder holder, int position) {
        final KunjunganModel kunjunganModel = kunjunganModels.get(position);
        final String id = kunjunganModel.getId();
        final String name = kunjunganModel.getName();
        final String datetime = kunjunganModel.getDatetime();

        holder.textViewName.setText(name);
        holder.textViewDateTime.setText(getDate(datetime));

        holder.layoutListKunjungan.setOnClickListener(view -> {
            if (fragment instanceof KunjunganFragment){
                ((KunjunganFragment)fragment).onClickItem(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kunjunganModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDateTime;
        LinearLayout layoutListKunjungan;

        ViewHolder(View v) {
            super(v);

            textViewName = v.findViewById(R.id.tvNamaToko);
            textViewDateTime = v.findViewById(R.id.tvDateTime);
            layoutListKunjungan = v.findViewById(R.id.layoutListKunjungan);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private static String getDate(String strDate) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("HH:mm | dd MMM yyyy");
        return format.format(Objects.requireNonNull(newDate));
    }
}
