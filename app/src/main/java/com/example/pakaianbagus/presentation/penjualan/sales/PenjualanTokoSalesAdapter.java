package com.example.pakaianbagus.presentation.penjualan.sales;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.presentation.penjualan.PenjualanListTokoFragment;
import com.example.pakaianbagus.presentation.penjualan.adapter.PenjualanTokoAdapter;

import java.util.List;

public class PenjualanTokoSalesAdapter extends RecyclerView.Adapter<PenjualanTokoSalesAdapter.ViewHolder> {
    private List<KatalogTokoModel> katalogTokoModels;
    private Context context;
    private Fragment penjualanListTokoFragment;

    public PenjualanTokoSalesAdapter(List<KatalogTokoModel> katalogTokoModels, Context context, Fragment penjualanListTokoFragment){
        this.katalogTokoModels = katalogTokoModels;
        this.context = context;
        this.penjualanListTokoFragment = penjualanListTokoFragment;
    }

    @NonNull
    @Override
    public PenjualanTokoSalesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_katalog_list_toko, parent, false);

        return new PenjualanTokoSalesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PenjualanTokoSalesAdapter.ViewHolder holder, int position){
        final KatalogTokoModel katalogTokoModel = katalogTokoModels.get(position);
        final String id = katalogTokoModel.getId();
        final String name = katalogTokoModel.getName();
        final String address = katalogTokoModel.getAddress();
        final String limit = katalogTokoModel.getLimit();
        final String totalHutang = katalogTokoModel.getAccounts_receivable();
        Log.d("hutang", totalHutang);

        holder.textViewName.setText(name);
        holder.textViewAddress.setText(address);
//        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(holder.imageViewKatalog);

        holder.layout.setOnClickListener(view -> {
            if (limit == null){
                String mLimit = "0";
                ((PenjualanListTokoSalesFragment)penjualanListTokoFragment).onClickItem(id, mLimit, totalHutang);
            } else {
                ((PenjualanListTokoSalesFragment)penjualanListTokoFragment).onClickItem(id, limit, totalHutang);
            }
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
