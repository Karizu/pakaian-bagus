package com.example.pakaianbagus.presentation.barangmasuk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.Toko;
import com.example.pakaianbagus.presentation.barangmasuk.ListTokoBMFragment;

import java.util.List;

public class ListTokoBMAdapter extends RecyclerView.Adapter<ListTokoBMAdapter.ViewHolder> {
    private List<Toko> tokos;
    private Context context;
    private ListTokoBMFragment listTokoBMFragment;

    public ListTokoBMAdapter(List<Toko> tokos, Context context, ListTokoBMFragment listTokoBMFragment){
        this.tokos = tokos;
        this.context = context;
        this.listTokoBMFragment = listTokoBMFragment;
    }

    @NonNull
    @Override
    public ListTokoBMAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_mutasi_barang, parent, false);

        return new ListTokoBMAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTokoBMAdapter.ViewHolder holder, int position){
        final Toko mbModel = tokos.get(position);

        final String name = mbModel.getName();
        final String datetime = mbModel.getDatetime();

        holder.textViewName.setText(name);
        holder.textViewDateTime.setText(datetime);

        holder.layoutListKunjungan.setOnClickListener(view -> {
            listTokoBMFragment.layoutListBarangMasuk();
        });
    }

    @Override
    public int getItemCount(){
        Log.d("itemCount", String.valueOf(tokos.size()));
        return tokos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDateTime;
        LinearLayout layoutListKunjungan;

        ViewHolder(View v){
            super(v);

            textViewName = v.findViewById(R.id.tvNamaToko);
            textViewDateTime = v.findViewById(R.id.tvDateTime);
            layoutListKunjungan = v.findViewById(R.id.layoutListKunjungan);
        }
    }
}
