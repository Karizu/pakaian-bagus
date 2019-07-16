package com.example.pakaianbagus.presentation.penjualan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.SalesReportModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.ViewHolder> {
    private List<SalesReportModel> salesReportModels;
    private Context context;
    private View v;

    public SalesReportAdapter(List<SalesReportModel> salesReportModels, Context context){
        this.salesReportModels = salesReportModels;
        this.context = context;
    }

    @NonNull
    @Override
    public SalesReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_sales_report, parent, false);

        return new SalesReportAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SalesReportAdapter.ViewHolder holder, int position){
        final SalesReportModel salesReportModel = salesReportModels.get(position);
//        final String id = katalogModel.getId();
        final String name = salesReportModel.getName();
        final String image = salesReportModel.getImage();
        final String qty = salesReportModel.getQty();
        final String harga = salesReportModel.getHarga();
        final String diskon = salesReportModel.getDiskon();
        final String total = salesReportModel.getTotal();

        int mHarga = Integer.parseInt(harga);
        int mDiskon = 0;
        if (diskon != null){
            mDiskon = Integer.parseInt(diskon);
        }
        int mTotal = Integer.parseInt(total);

        holder.textViewName.setText(name);
        holder.textViewQty.setText(qty);
        holder.textViewHarga.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(mHarga));
        holder.textViewDiskon.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(mDiskon));
        holder.textViewTotal.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(mTotal));
//        Glide.with(context).load(image).into(holder.imageViewSalesReport);
//        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(holder.imageViewKatalog);

        holder.imageViewMore.setOnClickListener(v -> {
            View v1 = v.findViewById(R.id.btnMore);
            PopupMenu pm = new PopupMenu(Objects.requireNonNull(context), v1);
            pm.getMenuInflater().inflate(R.menu.menu_options, pm.getMenu());
            pm.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_ubah:
                        Toast.makeText(context, String.valueOf(menuItem.getTitle()), Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            });
            pm.show();
        });

        holder.layoutSalesReport.setOnClickListener(view -> {
//            View viewSheet = LayoutInflater.from(view.getContext()).inflate(R.layout.find_outreach_worker_bottom_sheet_dialog, null);
//            Log.d( "onClick: ",String.valueOf(viewSheet));
//            final BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
//            dialog.setContentView(viewSheet);
//            dialog.show();
        });
    }

    @Override
    public int getItemCount(){ return salesReportModels.size();}

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewQty;
        TextView textViewHarga;
        TextView textViewDiskon;
        TextView textViewTotal;
        ImageView imageViewSalesReport;
        LinearLayout layoutSalesReport;
        ImageView imageViewMore;

        ViewHolder(View v){
            super(v);

            textViewName = v.findViewById(R.id.tvNamaBarang);
            textViewQty = v.findViewById(R.id.tvQty);
            textViewHarga = v.findViewById(R.id.tvHarga);
            textViewDiskon = v.findViewById(R.id.tvDiskon);
            textViewTotal = v.findViewById(R.id.tvTotal);
            imageViewSalesReport = v.findViewById(R.id.imgBarang);
            imageViewMore = v.findViewById(R.id.btnMore);
            layoutSalesReport = v.findViewById(R.id.layoutSalesReport);
        }
    }
}
