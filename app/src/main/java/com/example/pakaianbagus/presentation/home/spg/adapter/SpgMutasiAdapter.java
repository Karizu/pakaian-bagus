package com.example.pakaianbagus.presentation.home.spg.adapter;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.SpgModel;
import com.example.pakaianbagus.presentation.home.spg.SpgListMutasiFragment;

import java.util.List;

/**
 * Created by alfianhpratama on 26/08/2019.
 */

public class SpgMutasiAdapter extends RecyclerView.Adapter<SpgMutasiAdapter.ViewHolder> {
    private List<SpgModel> spgModels;
    private Context context;
    private Dialog dialog;
    private SpgListMutasiFragment spgListMutasiFragment;

    public SpgMutasiAdapter(List<SpgModel> spgModels, Context context, SpgListMutasiFragment spgListMutasiFragment) {
        this.spgModels = spgModels;
        this.context = context;
        this.spgListMutasiFragment = spgListMutasiFragment;
    }

    @NonNull
    @Override
    public SpgMutasiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_spg, parent, false);

        return new SpgMutasiAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SpgMutasiAdapter.ViewHolder holder, int position) {
        final SpgModel spgModel = spgModels.get(position);
//        final String id = katalogModel.getId();
        final String name = spgModel.getName();
        final String toko = spgModel.getToko();

        holder.textViewName.setText(name);
        holder.textViewQty.setText(toko);
        holder.layoutSpg.setOnClickListener(view -> spgListMutasiFragment.onClickLayoutListSPG());
    }

    @Override
    public int getItemCount() {
        return spgModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewQty;
        CardView layoutSpg;

        ViewHolder(View v) {
            super(v);

            textViewName = v.findViewById(R.id.tvName);
            textViewQty = v.findViewById(R.id.tvNamaToko);
            layoutSpg = v.findViewById(R.id.cvSpg);
        }
    }
}