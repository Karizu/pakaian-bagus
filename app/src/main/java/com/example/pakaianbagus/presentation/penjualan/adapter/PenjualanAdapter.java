package com.example.pakaianbagus.presentation.penjualan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.penjualan.model.PenjualanModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PenjualanAdapter extends RecyclerView.Adapter<PenjualanAdapter.ViewHolder> {
    private List<PenjualanModel> penjualanModels;
    private Context context;
    private View v;

    public PenjualanAdapter(List<PenjualanModel> penjualanModels, Context context){
        this.penjualanModels = penjualanModels;
        this.context = context;
    }

    @NonNull
    @Override
    public PenjualanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_penjualan_kompetitor, parent, false);

        return new PenjualanAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PenjualanAdapter.ViewHolder holder, int position){
        final PenjualanModel penjualanModel = penjualanModels.get(position);
//        final String id = katalogModel.getId();
        final String name = penjualanModel.getName();
        final String qty = penjualanModel.getQty();
        final String harga = penjualanModel.getHarga();
        final String date = penjualanModel.getDate();

        int mHarga = Integer.parseInt(harga);

        holder.textViewName.setText(name);
        holder.textViewQty.setText(qty);
        holder.textViewHarga.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(mHarga));
        holder.textViewDate.setText(date);
//        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(holder.imageViewKatalog);

        holder.layoutPenjualan.setOnClickListener(view -> {
//            View viewSheet = LayoutInflater.from(view.getContext()).inflate(R.layout.find_outreach_worker_bottom_sheet_dialog, null);
//            Log.d( "onClick: ",String.valueOf(viewSheet));
//            final BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
//            dialog.setContentView(viewSheet);
//            dialog.show();
        });
    }

    @Override
    public int getItemCount(){ return penjualanModels.size();}

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewQty;
        TextView textViewHarga;
        TextView textViewDate;
        LinearLayout layoutPenjualan;

        ViewHolder(View v){
            super(v);

            textViewName = v.findViewById(R.id.tvNamaBarang);
            textViewQty = v.findViewById(R.id.tvQty);
            textViewHarga = v.findViewById(R.id.tvHarga);
            textViewDate = v.findViewById(R.id.tvDate);
            layoutPenjualan = v.findViewById(R.id.layoutPenjualan);
        }
    }
}
