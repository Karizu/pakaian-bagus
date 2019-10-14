package com.example.pakaianbagus.presentation.home.piutang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.DetailInvoiceModel;
import com.example.pakaianbagus.models.InvoiceModels;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.presentation.home.piutang.ListInvoiceFragment;
import com.example.pakaianbagus.presentation.home.piutang.PiutangListTokoFragment;
import com.example.pakaianbagus.util.RoundedCornersTransformation;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sCorner;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sMargin;

public class ListInvoiceAdapter extends RecyclerView.Adapter<ListInvoiceAdapter.ViewHolder> {
    private List<InvoiceModels> invoiceModels;
    private List<DetailInvoiceModel> detailInvoiceModels;
    private Context context;
    private ListInvoiceFragment spgListTokoFragment;

    public ListInvoiceAdapter(List<InvoiceModels> invoiceModels, Context context, ListInvoiceFragment spgListTokoFragment){
        this.invoiceModels = invoiceModels;
        this.context = context;
        this.spgListTokoFragment = spgListTokoFragment;
    }

    public ListInvoiceAdapter(List<DetailInvoiceModel> detailInvoiceModels, Context context){
        this.detailInvoiceModels = detailInvoiceModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ListInvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v;
        if (invoiceModels!=null){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_item_list_invoice_2, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_item_list_invoice_1, parent, false);
        }

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListInvoiceAdapter.ViewHolder holder, int position){
        if (detailInvoiceModels != null){
            final DetailInvoiceModel detailInvoiceModel = detailInvoiceModels.get(position);
            final int id = detailInvoiceModel.getId();
            final int qty = detailInvoiceModel.getQty();
            final int price = detailInvoiceModel.getPrice();
            final String name = detailInvoiceModel.getName();
            final String image = detailInvoiceModel.getImage();

            holder.tvNamaBarang.setText(name);
            holder.tvQty.setText(qty);
            holder.btnMore.setVisibility(View.GONE);
            holder.layAcara.setVisibility(View.GONE);
            holder.tvHarga.setText("Rp. "+ NumberFormat.getNumberInstance(Locale.US).format(price));
            Glide.with(context)
                    .applyDefaultRequestOptions(
                            new RequestOptions().placeholder(R.drawable.jeans).error(R.drawable.jeans))
                    .load(image)
                    .apply(RequestOptions.bitmapTransform(
                            new RoundedCornersTransformation(context, sCorner, sMargin)))
                    .into(holder.imgBarang);

        }

        if (invoiceModels != null){
            final InvoiceModels invoiceModel = invoiceModels.get(position);
            final String id = invoiceModel.getId();
            final String amount = invoiceModel.getAmount();
            final String noTrx = invoiceModel.getNo();
            final String trxId = invoiceModel.getTransaction_id();

            int mAmount = Integer.parseInt(amount);
            holder.textViewName.setText("Rp. "+ NumberFormat.getNumberInstance(Locale.US).format(mAmount));
            holder.textViewAddress.setText(noTrx);

            holder.layout.setOnClickListener(view -> {
                if (spgListTokoFragment != null){
                    spgListTokoFragment.onClickItem(amount, noTrx, trxId);
                }
            });
        }
    }

    @Override
    public int getItemCount(){
        if (detailInvoiceModels != null){
            return detailInvoiceModels.size();
        } else {
            return invoiceModels.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewAddress;
        CardView layout;

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

            textViewName = v.findViewById(R.id.tvNamaToko);
            textViewAddress = v.findViewById(R.id.tvDateTime);
            layout = v.findViewById(R.id.layoutListKunjungan);

            tvNamaBarang = v.findViewById(R.id.tvNamaBarang);
            tvHarga = v.findViewById(R.id.tvHarga);
            tvTotal = v.findViewById(R.id.tvTotal);
            tvQty = v.findViewById(R.id.tvQty);
            imgBarang = v.findViewById(R.id.imgBarang);
            btnMore = v.findViewById(R.id.btnMore);
            layoutListBarangMasuk = v.findViewById(R.id.layoutListBarangMasuk);
            layAcara = v.findViewById(R.id.layAcara);
        }
    }
}
