package com.example.pakaianbagus.presentation.mutasibarang.tambahmutasi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.MutasiBarang;
import com.example.pakaianbagus.models.api.mutation.detail.Detail;
import com.example.pakaianbagus.presentation.home.kunjungan.tambahkunjungan.TambahKunjunganFragment;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TambahMutasiBarangAdapter extends RecyclerView.Adapter<TambahMutasiBarangAdapter.ViewHolder> {
    private List<Detail> mbModels;
    private Context context;
    private Fragment fragment;
    private Dialog dialog;

    public TambahMutasiBarangAdapter(List<Detail> mbModels, Context context, Fragment fragment){
        this.mbModels = mbModels;
        this.context = context;
        this.fragment = fragment;
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
        final Detail mbModel = mbModels.get(position);
        final String name = mbModel.getCreatedAt();
        final String qty = String.valueOf(mbModel.getQty());

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
                        EditText etNominal = dialog.findViewById(R.id.etQty);
                        Button btnUbah = dialog.findViewById(R.id.btnUbah);
                        btnUbah.setOnClickListener(view1 -> {
                            holder.tvQty.setText(etNominal.getText().toString());
                            mbModel.setQty(Integer.parseInt(etNominal.getText().toString()));
                            mbModels.set(position, mbModel);
                            ((TambahMutasiFragment)fragment).updateData(mbModels);
                            dialog.dismiss();
                        });
                        imgClose.setOnClickListener(v3 -> dialog.dismiss());
                        break;
                    case R.id.navigation_hapus:
                        mbModels.remove(position);
                        ((TambahMutasiFragment)fragment).updateData(mbModels);
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
