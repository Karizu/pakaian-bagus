package com.example.pakaianbagus.presentation.home.kunjungan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.KunjunganKoordinatorModel;
import com.example.pakaianbagus.models.KunjunganModel;
import com.example.pakaianbagus.presentation.home.kunjungan.KunjunganKoordinatorFragment;

import java.util.List;

public class KunjunganKoordinatorAdapter extends RecyclerView.Adapter<KunjunganKoordinatorAdapter.ViewHolder> {
    private List<KunjunganKoordinatorModel> kunjunganModels;
    private Context context;
    private KunjunganKoordinatorFragment kunjunganKoordinatorFragment;

    public KunjunganKoordinatorAdapter(List<KunjunganKoordinatorModel> kunjunganModels, Context context, KunjunganKoordinatorFragment kunjunganKoordinatorFragment){
        this.kunjunganModels = kunjunganModels;
        this.context = context;
        this.kunjunganKoordinatorFragment = kunjunganKoordinatorFragment;
    }

    @NonNull
    @Override
    public KunjunganKoordinatorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_mutasi_barang, parent, false);

        return new KunjunganKoordinatorAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KunjunganKoordinatorAdapter.ViewHolder holder, int position){
        final KunjunganKoordinatorModel kunjunganModel = kunjunganModels.get(position);
        final String id = kunjunganModel.getId();
        final String name = kunjunganModel.getFirst_name();
//        final String datetime = kunjunganModel.getDatetime();

        holder.textViewName.setText(name);
        holder.textViewDateTime.setText("");

        holder.layoutListKunjungan.setOnClickListener(view -> {
            kunjunganKoordinatorFragment.onClickItem(id);
        });
    }

    @Override
    public int getItemCount(){
        return kunjunganModels.size();
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
