package com.example.pakaianbagus.presentation.barangmasuk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.barangmasuk.BarangMasukFragment;
import com.example.pakaianbagus.models.BarangMasukModel;

import java.util.List;

public class BarangMasukAdapter extends RecyclerView.Adapter<BarangMasukAdapter.ViewHolder> {
    private List<BarangMasukModel> barangMasukModels;
    private Context context;
    private BarangMasukFragment barangMasukFragment;

    public BarangMasukAdapter(List<BarangMasukModel> barangMasukModels, Context context, BarangMasukFragment barangMasukFragment){
        this.barangMasukModels = barangMasukModels;
        this.context = context;
        this.barangMasukFragment = barangMasukFragment;
    }

    @NonNull
    @Override
    public BarangMasukAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_barang_masuk, parent, false);

        return new BarangMasukAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BarangMasukAdapter.ViewHolder holder, int position){
        final BarangMasukModel barangMasukModel = barangMasukModels.get(position);
//        final String id = katalogModel.getId();
        final String kodeArtikel = barangMasukModel.getKodeArtikel();
        final String qty = barangMasukModel.getQty();
        final String status = barangMasukModel.getStatus();

        holder.textViewName.setText(kodeArtikel);
        holder.textViewDateTime.setText(qty);
        if (status.equals("1")){
            holder.imageViewStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.checked));
        }

        holder.layoutListBarangMasuk.setOnClickListener(view ->
            barangMasukFragment.layoutListBarangMasuk()
        );
    }

    @Override
    public int getItemCount(){
        return barangMasukModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDateTime;
        ImageView imageViewStatus;
        LinearLayout layoutListBarangMasuk;

        ViewHolder(View v){
            super(v);

            textViewName = v.findViewById(R.id.tvAticleBM);
            textViewDateTime = v.findViewById(R.id.tvQtyBM);
            imageViewStatus = v.findViewById(R.id.checkBoxBM);
            layoutListBarangMasuk = v.findViewById(R.id.layoutListBarangMasuk);
        }
    }
}
