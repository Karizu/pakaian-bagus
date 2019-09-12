package com.example.pakaianbagus.presentation.home.stockopname.adapter;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.stock.Stock;

import java.util.List;
import java.util.Objects;

public class StockOpnameAdapter extends RecyclerView.Adapter<StockOpnameAdapter.ViewHolder> {
    private List<Stock> stockList;
    private Context context;
    private Dialog dialog;

    public StockOpnameAdapter(List<Stock> stockList, Context context) {
        this.stockList = stockList;
        this.context = context;
    }

    @NonNull
    @Override
    public StockOpnameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_stockopname, parent, false);
        return new StockOpnameAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StockOpnameAdapter.ViewHolder holder, int position) {
        final Stock stock = stockList.get(position);
        final String name = stock.getItem().getCategory().getName();
        final int qty = stock.getQty();

        holder.textViewName.setText(name);
        holder.textViewQty.setText(String.valueOf(qty) + " pcs");
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
                            stock.setQty(Integer.parseInt(etQty.getText().toString()));
                            holder.textViewQty.setText(stock.getQty() + " pcs");
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

        /*holder.layoutListStock.setOnClickListener(view -> {
//            View viewSheet = LayoutInflater.from(view.getContext()).inflate(R.layout.find_outreach_worker_bottom_sheet_dialog, null);
//            Log.d( "onClick: ",String.valueOf(viewSheet));
//            final BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
//            dialog.setContentView(viewSheet);
//            dialog.show();
        });*/
    }

    @Override
    public int getItemCount() {
        return stockList.size();
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
