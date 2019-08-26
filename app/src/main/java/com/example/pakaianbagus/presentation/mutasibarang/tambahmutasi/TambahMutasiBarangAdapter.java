package com.example.pakaianbagus.presentation.mutasibarang.tambahmutasi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.MutasiBarang;
import com.example.pakaianbagus.models.MutasiBarangModel;

import java.util.List;
import java.util.Objects;

public class TambahMutasiBarangAdapter extends RecyclerView.Adapter<TambahMutasiBarangAdapter.ViewHolder> {
    private List<MutasiBarang> mbModels;
    private Context context;
    private Dialog dialog;

    public TambahMutasiBarangAdapter(List<MutasiBarang> mbModels, Context context){
        this.mbModels = mbModels;
        this.context = context;
    }

    @NonNull
    @Override
    public TambahMutasiBarangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_tambah_mutasi_barang, parent, false);

        return new TambahMutasiBarangAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TambahMutasiBarangAdapter.ViewHolder holder, int position){
        final MutasiBarang mbModel = mbModels.get(position);
//        final String id = katalogModel.getId();
        final String name = mbModel.getName();
        final String qty = mbModel.getQty();

        holder.tvNamaBarang.setText(name);
        holder.tvQty.setText(qty);

        holder.btnImgMore.setOnClickListener(view -> {
            View v1 = view.findViewById(R.id.btnImgMore);
            PopupMenu pm = new PopupMenu(Objects.requireNonNull(context), v1);
            pm.getMenuInflater().inflate(R.menu.menu_list_kunjungan, pm.getMenu());
            pm.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_ubah:
                        showDialog();
                        ImageView imgClose = dialog.findViewById(R.id.imgClose);
                        imgClose.setOnClickListener(v3 -> dialog.dismiss());
                        break;
                    case R.id.navigation_hapus:
                        mbModels.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mbModels.size());
                        break;
                }
                return true;
            });
            pm.show();
        });
    }

    @Override
    public int getItemCount(){
        Log.d("itemCount", String.valueOf(mbModels.size()));
        return mbModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaBarang;
        TextView tvQty;
        LinearLayout btnImgMore;

        ViewHolder(View v){
            super(v);

            tvNamaBarang = v.findViewById(R.id.tvNamaBarang);
            tvQty = v.findViewById(R.id.tvQty);
            btnImgMore = v.findViewById(R.id.btnImgMore);
        }
    }

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(context));
        //set content
        dialog.setContentView(R.layout.dialog_ubah_qty);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

//    public void updateData(List<NearestOutreachModel> newUser){
//        articleModels = new ArrayList<>();
//        articleModels.addAll(newUser);
//        notifyDataSetChanged();
//    }
}
