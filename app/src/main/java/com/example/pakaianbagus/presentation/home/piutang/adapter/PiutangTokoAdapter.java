package com.example.pakaianbagus.presentation.home.piutang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.presentation.home.piutang.PiutangListTokoFragment;
import java.util.List;

public class PiutangTokoAdapter extends RecyclerView.Adapter<PiutangTokoAdapter.ViewHolder> {
    private List<KatalogTokoModel> katalogTokoModels;
    private Context context;
    private PiutangListTokoFragment spgListTokoFragment;

    public PiutangTokoAdapter(List<KatalogTokoModel> katalogTokoModels, Context context, PiutangListTokoFragment spgListTokoFragment){
        this.katalogTokoModels = katalogTokoModels;
        this.context = context;
        this.spgListTokoFragment = spgListTokoFragment;
    }

    @NonNull
    @Override
    public PiutangTokoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_katalog_list_toko, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PiutangTokoAdapter.ViewHolder holder, int position){
        final KatalogTokoModel katalogTokoModel = katalogTokoModels.get(position);
        final String id = katalogTokoModel.getId();
        final String name = katalogTokoModel.getName();
        final String address = katalogTokoModel.getAddress();
        final String hutang = katalogTokoModel.getAccounts_receivable();

        holder.textViewName.setText(name);
        holder.textViewAddress.setText(address);
//        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(holder.imageViewKatalog);

        holder.layout.setOnClickListener(view -> {
            spgListTokoFragment.onClickItem(id, name, hutang);
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
