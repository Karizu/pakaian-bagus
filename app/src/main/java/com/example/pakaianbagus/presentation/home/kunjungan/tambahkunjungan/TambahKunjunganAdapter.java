package com.example.pakaianbagus.presentation.home.kunjungan.tambahkunjungan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.Kunjungan;
import com.example.pakaianbagus.util.RoundedCornersTransformation;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sCorner;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sMargin;

public class TambahKunjunganAdapter extends RecyclerView.Adapter<TambahKunjunganAdapter.ViewHolder> {
    private List<Kunjungan> kunjunganModels;
    private Context context;
    private Dialog dialog;
    private View v;

    TambahKunjunganAdapter(List<Kunjungan> kunjunganModels, Context context){
        this.kunjunganModels = kunjunganModels;
        this.context = context;
    }

    @NonNull
    @Override
    public TambahKunjunganAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_tambah_kunjungan, parent, false);

        return new TambahKunjunganAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TambahKunjunganAdapter.ViewHolder holder, int position){
        final Kunjungan kunjunganModel = kunjunganModels.get(position);
//        final String id = katalogModel.getId();
        final String name = kunjunganModel.getName();
        final String nominal = kunjunganModel.getNominal();

        holder.tvTipePengeluaran.setText(name);

        int mNominal = Integer.parseInt(nominal);
        holder.tvNominal.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(mNominal));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        kunjunganModel.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
        Glide.with(context)
                .asBitmap()
                .load(stream.toByteArray())
                .apply(RequestOptions.bitmapTransform(
                        new RoundedCornersTransformation(context, sCorner, sMargin))).into(holder.imgKunjungan);

        holder.btnMore.setOnClickListener(v2 -> {
            View v1 = v2.findViewById(R.id.btnImgMore);
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
                        kunjunganModels.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, kunjunganModels.size());
                        break;
                }
                return true;
            });
            pm.show();
        });
    }

    @Override
    public int getItemCount(){
        return kunjunganModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipePengeluaran;
        TextView tvNominal;
        ImageView imgKunjungan;
        LinearLayout btnMore;

        ViewHolder(View v){
            super(v);

            tvTipePengeluaran = v.findViewById(R.id.tvTipePengeluaran);
            tvNominal = v.findViewById(R.id.tvNominal);
            imgKunjungan = v.findViewById(R.id.imgPengeluaran);
            btnMore = v.findViewById(R.id.btnImgMore);
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
