package com.example.pakaianbagus.presentation.home.spg.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
                .inflate(R.layout.content_item_mutasi_spg, parent, false);

        return new SpgMutasiAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SpgMutasiAdapter.ViewHolder holder, int position) {
        final SpgModel spgModel = spgModels.get(position);
        final String id = spgModel.getId();
        final String name = spgModel.getName();
        final String toToko = spgModel.getToToko();
        final String fromToko = spgModel.getFromToko();
        final int status = spgModel.getStatus();

        holder.textViewName.setText(name);
        holder.textViewDeskripsi.setText(fromToko + " ke "+toToko);
        switch (status){
            case 0:
                holder.imgStatusStripe.setVisibility(View.VISIBLE);
                holder.imgStatusCheck.setVisibility(View.GONE);
                break;
            case 1:
                holder.imgStatusStripe.setVisibility(View.GONE);
                holder.imgStatusCheck.setVisibility(View.VISIBLE);
                break;
        }
//        holder.layoutSpg.setOnClickListener(view -> spgListMutasiFragment.onClickLayoutListSPG(id));
    }

    @Override
    public int getItemCount() {
        return spgModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDeskripsi;
        ImageView imgStatusCheck;
        ImageView imgStatusStripe;
        CardView layoutSpg;

        ViewHolder(View v) {
            super(v);

            textViewName = v.findViewById(R.id.tvName);
            textViewDeskripsi = v.findViewById(R.id.tvDeskripsi);
            imgStatusCheck = v.findViewById(R.id.imgStatusCheck);
            imgStatusStripe = v.findViewById(R.id.imgStatusStripe);
            layoutSpg = v.findViewById(R.id.cvSpg);
        }
    }
}