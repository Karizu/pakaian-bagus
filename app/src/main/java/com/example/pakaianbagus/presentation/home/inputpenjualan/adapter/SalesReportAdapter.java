package com.example.pakaianbagus.presentation.home.inputpenjualan.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.Discount;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.presentation.home.inputpenjualan.InputPenjualan;
import com.example.pakaianbagus.presentation.penjualan.InputHarianFragment;
import com.example.pakaianbagus.util.RoundedCornersTransformation;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sCorner;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sMargin;

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.ViewHolder> {
    private List<Stock> stockList;
    private List<Discount> discounts;
    private Context context;
    private Fragment fragment;
    private Dialog dialog;

    public SalesReportAdapter(List<Stock> stockList, Context context, Fragment fragment, List<Discount> discounts) {
        this.stockList = stockList;
        this.context = context;
        this.fragment = fragment;
        this.discounts = discounts;
    }

    @NonNull
    @Override
    public SalesReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_sales_report, parent, false);

        return new SalesReportAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SalesReportAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvNamaBarang.setText(stockList.get(position).getItem().getName());
        holder.tvHarga.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(stockList.get(position).getPrice()));
        holder.tvQty.setText(String.valueOf(stockList.get(position).getQty()));
        holder.total = stockList.get(position).getTotals() * stockList.get(position).getQty();

        Glide.with(context)
                .applyDefaultRequestOptions(
                        new RequestOptions().placeholder(R.drawable.jeans).error(R.drawable.jeans))
                .load(stockList.get(position).getItem().getImage())
                .apply(RequestOptions.bitmapTransform(
                        new RoundedCornersTransformation(context, sCorner, sMargin)))
                .into(holder.imgBarang);

        final InputSpinnerAdapter adapter = new InputSpinnerAdapter(context, android.R.layout.simple_spinner_item, discounts);
        holder.spDiskon.setAdapter(adapter);

        try {
            holder.tvDiskon.setText(stockList.get(position).getDiscount().getValue().length() < 4 ?
                    stockList.get(position).getDiscount().getValue()+"%":
                    "Rp. " + NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(stockList.get(position).getDiscount().getValue())));

        } catch (Exception e){
            e.printStackTrace();
        }

        if (fragment instanceof InputPenjualan) {
            holder.spDiskon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {
                    if (index > 0) {
                        if (discounts.get(index).getType().equals("2")) {
                            if (Integer.parseInt(discounts.get(index).getValue()) >= stockList.get(position).getPrice()){
                                Toast.makeText(context, "Promo tidak bisa digunakan \n promo sama atau lebih dari harga", Toast.LENGTH_SHORT).show();
                            } else {
                                int dis =(stockList.get(position).getPrice() - Integer.parseInt(discounts.get(index).getValue()));
                                holder.total = dis * stockList.get(position).getQty();
                            }
                        } else {
                            int dis = (stockList.get(position).getPrice() * Integer.parseInt(discounts.get(index).getValue()) / 100);
                            holder.total = (stockList.get(position).getPrice() - dis) * stockList.get(position).getQty();
                        }
                    } else {
                        holder.total = stockList.get(position).getPrice() * stockList.get(position).getQty();
                    }

                    holder.tvTotal.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(holder.total));

                    Discount disco = discounts.get(index);

                    if (index > 0) {
                        holder.tvDiskon.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US)
                                .format(Integer.parseInt(disco.getValue())));
                    } else {
                        holder.tvDiskon.setText(disco.getValue());
                    }

                    stockList.get(position).setDiscount(disco);
                    stockList.get(position).setTotal(holder.total);

                    ((InputPenjualan) fragment).salesData(stockList);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        if (fragment instanceof InputPenjualan) {
            holder.btnMore.setVisibility(View.GONE);
            if (stockList.get(position).isNew()) {
                holder.tvDiskon.setVisibility(View.GONE);
                holder.spDiskon.setVisibility(View.VISIBLE);
            } else {

                holder.tvDiskon.setVisibility(View.VISIBLE);
                holder.spDiskon.setVisibility(View.GONE);
            }
        } else {
            holder.tvDiskon.setVisibility(View.VISIBLE);
            holder.spDiskon.setVisibility(View.GONE);
            holder.btnMore.setVisibility(View.VISIBLE);
        }

        if (fragment instanceof InputHarianFragment) {
            holder.btnMore.setOnClickListener(v -> {
                View v1 = v.findViewById(R.id.btnMore);
                PopupMenu pm = new PopupMenu(Objects.requireNonNull(context), v1);
                pm.getMenuInflater().inflate(R.menu.menu_options, pm.getMenu());
                pm.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.navigation_ubah) {
                        showDialog(context);
                        ImageView imgClose = dialog.findViewById(R.id.imgClose);
                        EditText etQty = dialog.findViewById(R.id.etQty);
                        etQty.setText(String.valueOf(stockList.get(position).getQty()));
                        Button btnUbah = dialog.findViewById(R.id.btnUbah);
                        Spinner spDiskon = dialog.findViewById(R.id.spDiskon);
                        spDiskon.setAdapter(adapter);
                        spDiskon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {
                                if (index > 0) {
                                    if (discounts.get(index).getType().equals("2")) {
                                        holder.dialogTotal = Integer.parseInt(discounts.get(index).getValue());
                                        // * stockList.get(position).getQty();
                                    } else {
                                        int dis = (stockList.get(position).getPrice() * Integer.parseInt(discounts.get(index).getValue()) / 100);
                                        holder.dialogTotal = (stockList.get(position).getPrice() - dis);// * stockList.get(position).getQty();
                                    }
                                } else {
                                    holder.dialogTotal = stockList.get(position).getPrice();
                                }
                                holder.discount = discounts.get(index);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        btnUbah.setOnClickListener(v2 -> {
                            if (!etQty.getText().toString().equals("")) {
                                dialog.dismiss();
                                holder.total = holder.dialogTotal * Integer.parseInt(etQty.getText().toString());
                                holder.tvTotal.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(holder.total));
                                holder.tvQty.setText(etQty.getText().toString());

                                if (holder.dialogTotal != stockList.get(position).getPrice()) {
                                    if (holder.discount.getType().equals("2")) {
                                        holder.tvDiskon.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US)
                                                .format(Integer.parseInt(holder.discount.getValue())));
                                    } else {
                                        holder.tvDiskon.setText(holder.discount.getValue() + " %");
                                    }
                                } else {
                                    holder.tvDiskon.setText(holder.discount.getValue());
                                }

                                stockList.get(position).setDiscount(holder.discount);
                                stockList.get(position).setQty(Integer.parseInt(etQty.getText().toString()));
                                stockList.get(position).setTotal(holder.total);
                                stockList.get(position).setNew(true);

                                ((InputHarianFragment) fragment).salesData(stockList);

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

        holder.tvTotal.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(holder.total));

    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaBarang;
        TextView tvHarga;
        TextView tvQty;
        TextView tvTotal;
        ImageView imgBarang;
        ImageView btnMore;
        Spinner spDiskon;
        TextView tvDiskon;
        LinearLayout layoutListBarangMasuk;

        private int total = 0;
        private int dialogTotal = 0;
        private Discount discount;

        ViewHolder(View v) {
            super(v);
            tvNamaBarang = v.findViewById(R.id.tvNamaBarang);
            tvHarga = v.findViewById(R.id.tvHarga);
            tvTotal = v.findViewById(R.id.tvTotal);
            tvQty = v.findViewById(R.id.tvQty);
            imgBarang = v.findViewById(R.id.imgBarang);
            btnMore = v.findViewById(R.id.btnMore);
            spDiskon = v.findViewById(R.id.spDiskon);
            tvDiskon = v.findViewById(R.id.tvDiskon);
            layoutListBarangMasuk = v.findViewById(R.id.layoutListBarangMasuk);
        }
    }

    private void showDialog(Context context) {
        dialog = new Dialog(Objects.requireNonNull(context));
        //set content
        dialog.setContentView(R.layout.dialog_diskon_qty);
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
