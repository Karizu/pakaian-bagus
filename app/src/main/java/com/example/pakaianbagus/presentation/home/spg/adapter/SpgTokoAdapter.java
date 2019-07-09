package com.example.pakaianbagus.presentation.home.spg.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.presentation.home.spg.SpgListTokoFragment;
import com.example.pakaianbagus.presentation.katalog.KatalogFragment;

import java.util.List;

public class SpgTokoAdapter extends RecyclerView.Adapter<SpgTokoAdapter.ViewHolder> {
    private List<KatalogTokoModel> katalogTokoModels;
    private Context context;
    private SpgListTokoFragment spgListTokoFragment;

    public SpgTokoAdapter(List<KatalogTokoModel> katalogTokoModels, Context context, SpgListTokoFragment spgListTokoFragment){
        this.katalogTokoModels = katalogTokoModels;
        this.context = context;
        this.spgListTokoFragment = spgListTokoFragment;
    }

    @NonNull
    @Override
    public SpgTokoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_katalog_list_toko, parent, false);

        return new SpgTokoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SpgTokoAdapter.ViewHolder holder, int position){
        final KatalogTokoModel katalogTokoModel = katalogTokoModels.get(position);
        final String id = katalogTokoModel.getId();
        final String name = katalogTokoModel.getName();
        final String address = katalogTokoModel.getAddress();

        holder.textViewName.setText(name);
        holder.textViewAddress.setText(address);
//        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(holder.imageViewKatalog);

        holder.layout.setOnClickListener(view -> {
            spgListTokoFragment.onClickItem(id);
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
