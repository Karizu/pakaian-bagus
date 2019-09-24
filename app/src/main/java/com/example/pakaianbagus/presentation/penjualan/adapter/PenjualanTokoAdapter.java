package com.example.pakaianbagus.presentation.penjualan.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.presentation.penjualan.PenjualanListTokoFragment;

import java.util.List;

public class PenjualanTokoAdapter extends RecyclerView.Adapter<PenjualanTokoAdapter.ViewHolder> {
    private List<KatalogTokoModel> katalogTokoModels;
    private Context context;
    private PenjualanListTokoFragment penjualanListTokoFragment;

    public PenjualanTokoAdapter(List<KatalogTokoModel> katalogTokoModels, Context context, PenjualanListTokoFragment penjualanListTokoFragment){
        this.katalogTokoModels = katalogTokoModels;
        this.context = context;
        this.penjualanListTokoFragment = penjualanListTokoFragment;
    }

    @NonNull
    @Override
    public PenjualanTokoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_katalog_list_toko, parent, false);

        return new PenjualanTokoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PenjualanTokoAdapter.ViewHolder holder, int position){
        final KatalogTokoModel katalogTokoModel = katalogTokoModels.get(position);
        final String id = katalogTokoModel.getId();
        final String name = katalogTokoModel.getName();
        final String address = katalogTokoModel.getAddress();

        holder.textViewName.setText(name);
        holder.textViewAddress.setText(address);
//        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(holder.imageViewKatalog);

        holder.layout.setOnClickListener(view -> {
            penjualanListTokoFragment.onClickItem(id);
        });
    }

    @Override
    public int getItemCount(){ return katalogTokoModels.size();}

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewAddress;
        LinearLayout layout;

        ViewHolder(View v){
            super(v);

            textViewName = v.findViewById(R.id.tvNamaToko);
            textViewAddress = v.findViewById(R.id.tvAlamat);
            layout = v.findViewById(R.id.layoutListToko);
        }
    }
}
