package com.example.pakaianbagus.presentation.home.inventaris.adapter;

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
import com.example.pakaianbagus.models.InventarisModel;

import java.util.List;
import java.util.Objects;

public class InventarisAdapter extends RecyclerView.Adapter<InventarisAdapter.ViewHolder> {
    private List<InventarisModel> inventarisModels;
    private Context context;
    private Dialog dialog;

    public InventarisAdapter(List<InventarisModel> inventarisModels, Context context){
        this.inventarisModels = inventarisModels;
        this.context = context;
    }

    @NonNull
    @Override
    public InventarisAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_inventaris, parent, false);

        return new InventarisAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InventarisAdapter.ViewHolder holder, int position){
        final InventarisModel inventarisModel = inventarisModels.get(position);
//        final String id = katalogModel.getId();
        final String name = inventarisModel.getName();
        final String qty = inventarisModel.getQty();

        holder.textViewName.setText(name);
        holder.textViewQty.setText(qty);
        holder.imageViewMore.setOnClickListener(v -> {
            View v1 = v.findViewById(R.id.btnMore);
            PopupMenu pm = new PopupMenu(Objects.requireNonNull(context), v1);
            pm.getMenuInflater().inflate(R.menu.menu_options, pm.getMenu());
            pm.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_ubah:
                        showDialog(R.layout.dialog_ubah_qty, context);
                        ImageView imgClose = dialog.findViewById(R.id.imgClose);
                        imgClose.setOnClickListener(v2 -> dialog.dismiss());
                        break;
                }
                return true;
            });
            pm.show();
        });

        holder.layoutInventaris.setOnClickListener(view -> {
//            View viewSheet = LayoutInflater.from(view.getContext()).inflate(R.layout.find_outreach_worker_bottom_sheet_dialog, null);
//            Log.d( "onClick: ",String.valueOf(viewSheet));
//            final BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
//            dialog.setContentView(viewSheet);
//            dialog.show();
        });
    }

    @Override
    public int getItemCount(){
        return inventarisModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewQty;
        LinearLayout layoutInventaris;
        ImageView imageViewMore;

        ViewHolder(View v){
            super(v);

            textViewName = v.findViewById(R.id.tvNamaBarang);
            textViewQty = v.findViewById(R.id.tvQty);
            layoutInventaris = v.findViewById(R.id.layoutInventaris);
            imageViewMore = v.findViewById(R.id.btnMore);
        }
    }

    private void showDialog(int layout, Context context) {
        dialog = new Dialog(Objects.requireNonNull(context));
        //set content
        dialog.setContentView(layout);
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
