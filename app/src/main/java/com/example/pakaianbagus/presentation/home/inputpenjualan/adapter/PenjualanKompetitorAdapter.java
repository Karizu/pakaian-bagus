package com.example.pakaianbagus.presentation.home.inputpenjualan.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.api.penjualankompetitor.Kompetitor;
import com.example.pakaianbagus.presentation.penjualan.InputHarianFragment;
import com.example.pakaianbagus.util.DateUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by alfianhpratama on 07/09/2019.
 * Organization: UTeam
 */
public class PenjualanKompetitorAdapter extends RecyclerView.Adapter<PenjualanKompetitorAdapter.ViewHolder> {
    private List<Kompetitor> kompetitorList;
    private Context context;
    private Fragment fragment;
    private Dialog dialog;

    public PenjualanKompetitorAdapter(List<Kompetitor> kompetitorList, Context context, Fragment fragment) {
        this.kompetitorList = kompetitorList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_item_penjualan_kompetitor, viewGroup, false);
        return new PenjualanKompetitorAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date = new DateUtils().formatDateStringToString(kompetitorList.get(position).getUpdatedAt(),
                "yyyy-MM-dd HH:mm:ss", "yyyy MMMM dd");

        holder.textViewDate.setText(date);

        holder.textViewName.setText(kompetitorList.get(position).getBrand());
        holder.textViewHarga.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(kompetitorList.get(position).getPrice()));
        holder.textViewQty.setText(String.valueOf(kompetitorList.get(position).getQty()));

        if (fragment instanceof InputHarianFragment) {
            holder.btnMore.setVisibility(View.VISIBLE);
            holder.btnMore.setOnClickListener(v -> {
                View v1 = v.findViewById(R.id.btnMore);
                PopupMenu pm = new PopupMenu(Objects.requireNonNull(context), v1);
                pm.getMenuInflater().inflate(R.menu.menu_options, pm.getMenu());
                pm.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.navigation_ubah) {
                        showDialog(context);

                        ImageView imgClose = dialog.findViewById(R.id.imgClose);
                        imgClose.setOnClickListener(v2 -> dialog.dismiss());

                        EditText etPrice = dialog.findViewById(R.id.etPrice);
                        etPrice.setText(String.valueOf(kompetitorList.get(position).getPrice()));

                        EditText etQty = dialog.findViewById(R.id.etQty);
                        etQty.setText(String.valueOf(kompetitorList.get(position).getQty()));

                        Button btnUbah = dialog.findViewById(R.id.btnUbah);
                        btnUbah.setOnClickListener(v2 -> {
                            if (!etQty.getText().toString().equals("") && !etPrice.getText().toString().equals("")) {
                                dialog.dismiss();
                                Kompetitor data = kompetitorList.get(position);
                                data.setPrice(Integer.parseInt(etPrice.getText().toString()));
                                data.setQty(Integer.parseInt(etQty.getText().toString()));
                                ((InputHarianFragment) fragment).addPenjualanKompetitor(String.valueOf(data.getId()), data);

                            } else {
                                Toast.makeText(context, "Harap isi field", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    return true;
                });
                pm.show();
            });
        } else {
            holder.btnMore.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return kompetitorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewQty;
        TextView textViewHarga;
        TextView textViewDate;
        LinearLayout layoutPenjualan;
        ImageView btnMore;

        ViewHolder(View v) {
            super(v);

            textViewName = v.findViewById(R.id.tvNamaBarang);
            textViewQty = v.findViewById(R.id.tvQty);
            textViewHarga = v.findViewById(R.id.tvHarga);
            textViewDate = v.findViewById(R.id.tvDate);
            layoutPenjualan = v.findViewById(R.id.layoutPenjualan);
            btnMore = v.findViewById(R.id.btnMore);
        }
    }

    private void showDialog(Context context) {
        dialog = new Dialog(Objects.requireNonNull(context));
        //set content
        dialog.setContentView(R.layout.dialog_edit_penjualan_kompetitor);
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
