package com.example.pakaianbagus.presentation.home.stockopname.adapter;

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
import com.example.pakaianbagus.presentation.home.stockopname.StockListTokoFragment;

import java.util.List;

public class StockTokoAdapter extends RecyclerView.Adapter<StockTokoAdapter.ViewHolder> {
    private List<KatalogTokoModel> katalogTokoModels;
    private Context context;
    private StockListTokoFragment stockListTokoFragment;

    public StockTokoAdapter(List<KatalogTokoModel> katalogTokoModels, Context context, StockListTokoFragment stockListTokoFragment){
        this.katalogTokoModels = katalogTokoModels;
        this.context = context;
        this.stockListTokoFragment = stockListTokoFragment;
    }

    @NonNull
    @Override
    public StockTokoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_katalog_list_toko, parent, false);

        return new StockTokoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StockTokoAdapter.ViewHolder holder, int position){
        final KatalogTokoModel katalogTokoModel = katalogTokoModels.get(position);
        final String id = katalogTokoModel.getId();
        final String name = katalogTokoModel.getName();
        final String address = katalogTokoModel.getAddress();

        holder.textViewName.setText(name);
        holder.textViewAddress.setText(address);
//        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(holder.imageViewKatalog);

        holder.layout.setOnClickListener(view -> {
            stockListTokoFragment.onClickItem(id);
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
