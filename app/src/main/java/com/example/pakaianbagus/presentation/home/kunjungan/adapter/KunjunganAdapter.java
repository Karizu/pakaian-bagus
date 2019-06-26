package com.example.pakaianbagus.presentation.home.kunjungan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.home.kunjungan.model.KunjunganModel;

import java.util.List;

public class KunjunganAdapter extends RecyclerView.Adapter<KunjunganAdapter.ViewHolder> {
    private List<KunjunganModel> kunjunganModels;
    private Context context;

    public KunjunganAdapter(List<KunjunganModel> kunjunganModels, Context context){
        this.kunjunganModels = kunjunganModels;
        this.context = context;
    }

    @NonNull
    @Override
    public KunjunganAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_mutasi_barang, parent, false);

        return new KunjunganAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KunjunganAdapter.ViewHolder holder, int position){
        final KunjunganModel kunjunganModel = kunjunganModels.get(position);
//        final String id = katalogModel.getId();
        final String name = kunjunganModel.getName();
        final String datetime = kunjunganModel.getDatetime();

        holder.textViewName.setText(name);
        holder.textViewDateTime.setText(datetime);

        holder.layoutListKunjungan.setOnClickListener(view -> {
//            View viewSheet = LayoutInflater.from(view.getContext()).inflate(R.layout.find_outreach_worker_bottom_sheet_dialog, null);
//            Log.d( "onClick: ",String.valueOf(viewSheet));
//            final BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
//            dialog.setContentView(viewSheet);
//            dialog.show();
        });
    }

    @Override
    public int getItemCount(){
        return kunjunganModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDateTime;
        LinearLayout layoutListKunjungan;

        ViewHolder(View v){
            super(v);

            textViewName = v.findViewById(R.id.tvNamaToko);
            textViewDateTime = v.findViewById(R.id.tvDateTime);
            layoutListKunjungan = v.findViewById(R.id.layoutListKunjungan);
        }
    }
}
