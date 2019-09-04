package com.example.pakaianbagus.presentation.katalog.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.MainActivity;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.Brand;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.presentation.katalog.KatalogBrandFragment;
import com.example.pakaianbagus.presentation.katalog.KatalogFragment;

import java.util.List;

public class KatalogBrandAdapter extends RecyclerView.Adapter<KatalogBrandAdapter.ViewHolder> {
    private List<Brand> brandList;
    private Context context;
    private Fragment fragment;

    public KatalogBrandAdapter(List<Brand> brandList, Context context, Fragment fragment){
        this.brandList = brandList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public KatalogBrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_katalog_list_toko, parent, false);

        return new KatalogBrandAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KatalogBrandAdapter.ViewHolder holder, int position){
        final Brand brand = brandList.get(position);
        final String id = brand.getId_brand();
        final String name = brand.getNama_brand();
        final String address = brand.getDeskripsi();

        holder.textViewName.setText(name);
        holder.textViewAddress.setText(address);
//        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(holder.imageViewKatalog);

        holder.layout.setOnClickListener(view -> ((KatalogBrandFragment) fragment).onClickItem(id));
    }

    @Override
    public int getItemCount(){ return brandList.size();}

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