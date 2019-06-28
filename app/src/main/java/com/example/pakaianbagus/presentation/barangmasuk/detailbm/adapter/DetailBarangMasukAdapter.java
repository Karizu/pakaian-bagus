package com.example.pakaianbagus.presentation.barangmasuk.detailbm.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.barangmasuk.detailbm.model.DetailBarangMasukModel;

import java.util.List;
import java.util.Objects;

public class DetailBarangMasukAdapter extends RecyclerView.Adapter<DetailBarangMasukAdapter.ViewHolder> {
    private List<DetailBarangMasukModel> detailBarangMasukModels;
    private Context context;
    private Dialog dialog;

    public DetailBarangMasukAdapter(List<DetailBarangMasukModel> detailBarangMasukModels, Context context){
        this.detailBarangMasukModels = detailBarangMasukModels;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailBarangMasukAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_detail_barang_masuk, parent, false);

        return new DetailBarangMasukAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailBarangMasukAdapter.ViewHolder holder, int position){
        final DetailBarangMasukModel detailBarangMasukModel = detailBarangMasukModels.get(position);
//        final String id = katalogModel.getId();
        final String name = detailBarangMasukModel.getName();
        final String qty = detailBarangMasukModel.getQty();

        holder.textViewName.setText(name);
        holder.textViewQty.setText(qty);
        holder.imageViewMore.setOnClickListener(v -> {
            View v1 = v.findViewById(R.id.btnMore);
            PopupMenu pm = new PopupMenu(Objects.requireNonNull(context), v1);
            pm.getMenuInflater().inflate(R.menu.menu_options, pm.getMenu());
            pm.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.navigation_ubah) {
                    showDialog();
                    ImageView imgClose = dialog.findViewById(R.id.imgClose);
                    imgClose.setOnClickListener(v2 -> dialog.dismiss());
                }
                return true;
            });
            pm.show();
        });
        holder.layoutDetailBarangMasuk.setOnClickListener(view -> {
//            View viewSheet = LayoutInflater.from(view.getContext()).inflate(R.layout.find_outreach_worker_bottom_sheet_dialog, null);
//            Log.d( "onClick: ",String.valueOf(viewSheet));
//            final BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
//            dialog.setContentView(viewSheet);
//            dialog.show();
        });
    }

    @Override
    public int getItemCount(){
        return detailBarangMasukModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewQty;
        ImageView imageViewMore;
        LinearLayout layoutDetailBarangMasuk;

        ViewHolder(View v){
            super(v);

            textViewName = v.findViewById(R.id.tvNamaBarang);
            textViewQty = v.findViewById(R.id.tvQty);
            imageViewMore = v.findViewById(R.id.btnMore);
            layoutDetailBarangMasuk = v.findViewById(R.id.layoutDetailBarangMasuk);
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
}
