package com.example.pakaianbagus.presentation.mutasibarang.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
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

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.api.mutation.detail.Detail;
import com.example.pakaianbagus.presentation.mutasibarang.MutasiDetail;

import java.util.List;
import java.util.Objects;

/**
 * Created by alfianhpratama on 17/09/2019.
 * Organization: UTeam
 */
public class MutasiDetailAdapter extends RecyclerView.Adapter<MutasiDetailAdapter.ViewHolder> {

    private List<Detail> details;
    private Context context;
    private Fragment fragment;
    private Dialog dialog;

    public MutasiDetailAdapter(List<Detail> details, Context context, Fragment fragment) {
        this.details = details;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_stockopname, parent, false);
        return new MutasiDetailAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Detail detail = details.get(i);
        final String name = detail.getStock().getItem().getName();
        final int qty = detail.getQty();

        holder.textViewName.setText(name);
        holder.textViewQty.setText(qty + " pcs");
        holder.imageViewMore.setOnClickListener(v -> {
            View v1 = v.findViewById(R.id.btnMore);
            PopupMenu pm = new PopupMenu(Objects.requireNonNull(context), v1);
            pm.getMenuInflater().inflate(R.menu.menu_options, pm.getMenu());
            pm.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.navigation_ubah) {
                    showDialog(context);
                    ImageView imgClose = dialog.findViewById(R.id.imgClose);
                    EditText etQty = dialog.findViewById(R.id.etQty);
                    Button btnUbah = dialog.findViewById(R.id.btnUbah);
                    btnUbah.setOnClickListener(v2 -> {
                        if (!etQty.getText().toString().equals("")) {
                            dialog.dismiss();
                            detail.setQty(Integer.parseInt(etQty.getText().toString()));
                            holder.textViewQty.setText(detail.getQty() + " pcs");
                            setChangeToParent();
                        } else {
                            Toast.makeText(context, "Harap isi field", Toast.LENGTH_SHORT).show();
                        }
                    });
                    imgClose.setOnClickListener(v2 -> dialog.dismiss());
                }
                return true;
            });
            pm.show();
        });

    }

    private void setChangeToParent() {
        ((MutasiDetail) fragment).setDetailsTemp(details);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewQty;
        LinearLayout layoutListStock;
        ImageView imageViewMore;

        ViewHolder(View v) {
            super(v);

            textViewName = v.findViewById(R.id.tvName);
            textViewQty = v.findViewById(R.id.tvQty);
            layoutListStock = v.findViewById(R.id.layoutStock);
            imageViewMore = v.findViewById(R.id.btnMore);
        }
    }

    private void showDialog(Context context) {
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
