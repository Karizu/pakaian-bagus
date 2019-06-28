package com.example.pakaianbagus.presentation.home.spg.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.home.spg.SpgFragment;
import com.example.pakaianbagus.presentation.home.spg.model.SpgModel;

import java.util.List;

public class SpgAdapter extends RecyclerView.Adapter<SpgAdapter.ViewHolder> {
    private List<SpgModel> spgModels;
    private Context context;
    private Dialog dialog;
    private SpgFragment spgFragment;

    public SpgAdapter(List<SpgModel> spgModels, Context context, SpgFragment spgFragment){
        this.spgModels = spgModels;
        this.context = context;
        this.spgFragment = spgFragment;
    }

    @NonNull
    @Override
    public SpgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_spg, parent, false);

        return new SpgAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SpgAdapter.ViewHolder holder, int position){
        final SpgModel spgModel = spgModels.get(position);
//        final String id = katalogModel.getId();
        final String name = spgModel.getName();
        final String toko = spgModel.getToko();

        holder.textViewName.setText(name);
        holder.textViewQty.setText(toko);
        holder.layoutSpg.setOnClickListener(view ->
            spgFragment.onClickLayoutListSPG()
        );
    }

    @Override
    public int getItemCount(){
        return spgModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewQty;
        CardView layoutSpg;

        ViewHolder(View v){
            super(v);

            textViewName = v.findViewById(R.id.tvName);
            textViewQty = v.findViewById(R.id.tvNamaToko);
            layoutSpg = v.findViewById(R.id.cvSpg);
        }
    }
}
