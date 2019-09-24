package com.example.pakaianbagus.presentation.mutasibarang.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.MutasiBarangModel;

import java.util.List;

public class MutasiBarangAdapter extends RecyclerView.Adapter<MutasiBarangAdapter.ViewHolder> {
    private List<MutasiBarangModel> mbModels;
    private Context context;

    public MutasiBarangAdapter(List<MutasiBarangModel> mbModels, Context context){
        this.mbModels = mbModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MutasiBarangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_mutasi_barang, parent, false);

        return new MutasiBarangAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MutasiBarangAdapter.ViewHolder holder, int position){
        final MutasiBarangModel mbModel = mbModels.get(position);
//        final String id = katalogModel.getId();
        final String name = mbModel.getName();
        final String datetime = mbModel.getDatetime();

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
        Log.d("itemCount", String.valueOf(mbModels.size()));
        return mbModels.size();
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

//    public void updateData(List<NearestOutreachModel> newUser){
//        articleModels = new ArrayList<>();
//        articleModels.addAll(newUser);
//        notifyDataSetChanged();
//    }
}
