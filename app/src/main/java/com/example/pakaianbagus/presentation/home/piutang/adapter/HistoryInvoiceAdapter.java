package com.example.pakaianbagus.presentation.home.piutang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.DetailInvoiceModel;
import com.example.pakaianbagus.models.InvoiceModels;
import com.example.pakaianbagus.presentation.home.piutang.ListInvoiceFragment;
import com.example.pakaianbagus.util.RoundedCornersTransformation;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sCorner;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sMargin;

public class HistoryInvoiceAdapter extends RecyclerView.Adapter<HistoryInvoiceAdapter.ViewHolder> {
    private List<InvoiceModels> invoiceModels;
    private List<DetailInvoiceModel> detailInvoiceModels;
    private Context context;
    private ListInvoiceFragment spgListTokoFragment;

    public HistoryInvoiceAdapter(List<InvoiceModels> invoiceModels, Context context, ListInvoiceFragment spgListTokoFragment){
        this.invoiceModels = invoiceModels;
        this.context = context;
        this.spgListTokoFragment = spgListTokoFragment;
    }

    public HistoryInvoiceAdapter(List<DetailInvoiceModel> detailInvoiceModels, Context context){
        this.detailInvoiceModels = detailInvoiceModels;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryInvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_item_sales_report, parent, false);

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryInvoiceAdapter.ViewHolder holder, int position){
        if (detailInvoiceModels != null){
            final DetailInvoiceModel detailInvoiceModel = detailInvoiceModels.get(position);
            final int id = detailInvoiceModel.getId();
            final int qty = detailInvoiceModel.getQty();
            final int price = detailInvoiceModel.getPrice();
            final String name = detailInvoiceModel.getName();
            final String image = detailInvoiceModel.getImage();
            final int total = detailInvoiceModel.getTotal();

            holder.tvNamaBarang.setText(name);
            holder.tvQty.setText(qty+" Pcs");
            holder.btnMore.setVisibility(View.GONE);
            holder.layAcara.setVisibility(View.GONE);
            holder.tvHarga.setText("Rp. "+ NumberFormat.getNumberInstance(Locale.US).format(price));
            holder.tvTotal.setText("Rp. "+ NumberFormat.getNumberInstance(Locale.US).format(total));
            Glide.with(context)
                    .applyDefaultRequestOptions(
                            new RequestOptions().placeholder(R.drawable.jeans).error(R.drawable.jeans))
                    .load(image)
                    .apply(RequestOptions.bitmapTransform(
                            new RoundedCornersTransformation(context, sCorner, sMargin)))
                    .into(holder.imgBarang);

        }
    }

    @Override
    public int getItemCount(){
            return detailInvoiceModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaBarang;
        TextView tvHarga;
        TextView tvQty;
        TextView tvTotal;
        ImageView imgBarang;
        ImageView btnMore;
        LinearLayout layoutListBarangMasuk;
        LinearLayout layAcara;

        ViewHolder(View v){
            super(v);
            tvNamaBarang = v.findViewById(R.id.tvNamaBarang);
            tvHarga = v.findViewById(R.id.tvHarga);
            tvTotal = v.findViewById(R.id.tvTotal);
            tvQty = v.findViewById(R.id.tvQuantity);
            imgBarang = v.findViewById(R.id.imgBarang);
            btnMore = v.findViewById(R.id.btnMore);
            layoutListBarangMasuk = v.findViewById(R.id.layoutListBarangMasuk);
            layAcara = v.findViewById(R.id.layAcara);
        }
    }
}
