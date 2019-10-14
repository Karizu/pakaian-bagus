package com.example.pakaianbagus.presentation.mutasibarang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.api.mutation.Mutation;
import com.example.pakaianbagus.presentation.mutasibarang.MutasiBarangFragment;
import com.example.pakaianbagus.presentation.mutasibarang.MutasiBarangSPG;
import com.example.pakaianbagus.util.Constanta;

import java.util.List;

/**
 * Created by rizkidharmawan on 08/10/2019.
 * Organization: BL TEAM
 */

public class MutasiBarangAdapter extends RecyclerView.Adapter<MutasiBarangAdapter.ViewHolder> {

    private List<Mutation> mutationList;
    private Context context;
    private Fragment fragment;

    public MutasiBarangAdapter(List<Mutation> mutationList, Context context, Fragment fragment) {
        this.mutationList = mutationList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MutasiBarangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_item_barang_masuk, viewGroup, false);
        return new MutasiBarangAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MutasiBarangAdapter.ViewHolder holder, int i) {
        Mutation mutation = mutationList.get(i);
        holder.tvAticleBM.setText(mutation.getReceiptNo());
        holder.tvQtyBM.setText(String.valueOf(mutation.getTotalQty()));
        if (mutation.getStatus() == Constanta.MUTASI_VERIFIED_BY_KOORDINATOR) {
            holder.checkBoxBM.setImageResource(R.drawable.checked);
        } else {
            holder.checkBoxBM.setImageResource(R.drawable.unchecked);
        }
        holder.clickLayout.setOnClickListener(v -> ((MutasiBarangFragment) fragment).onClickItem(mutation));
        //((MutasiBarangSPG) fragment).onClickItem(String.valueOf(mutation.getId())));
    }

    @Override
    public int getItemCount() {
        return mutationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAticleBM;
        private TextView tvQtyBM;
        private ImageView checkBoxBM;
        private LinearLayout clickLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAticleBM = itemView.findViewById(R.id.tvAticleBM);
            tvQtyBM = itemView.findViewById(R.id.tvQtyBM);
            checkBoxBM = itemView.findViewById(R.id.checkBoxBM);
            clickLayout = itemView.findViewById(R.id.layoutListBarangMasuk);
        }
    }
}
