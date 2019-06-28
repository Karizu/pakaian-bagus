package com.example.pakaianbagus.presentation.katalog.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.KatalogModel;

import java.util.List;

public class KatalogAdapter extends RecyclerView.Adapter<KatalogAdapter.ViewHolder> {
    private List<KatalogModel> katalogModels;
    private Context context;

    public KatalogAdapter(List<KatalogModel> katalogModels, Context context){
        this.katalogModels = katalogModels;
        this.context = context;
    }

    @NonNull
    @Override
    public KatalogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_katalog, parent, false);

        return new KatalogAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KatalogAdapter.ViewHolder holder, int position){
        final KatalogModel katalogModel = katalogModels.get(position);
//        final String id = katalogModel.getId();
        final String name = katalogModel.getName();
        final String image = katalogModel.getImage();
        final String qty = katalogModel.getQty();
        final String kode = katalogModel.getKode();

        holder.textViewName.setText(name);
        holder.textViewQty.setText(qty);
        holder.textViewKode.setText(kode);
        Glide.with(context).load(image).into(holder.imageViewKatalog);
//        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(holder.imageViewKatalog);

        holder.cardView.setOnClickListener(view -> {
            Toast.makeText(context, "Celana Jeans", Toast.LENGTH_SHORT).show();
//            View viewSheet = LayoutInflater.from(view.getContext()).inflate(R.layout.find_outreach_worker_bottom_sheet_dialog, null);
//            Log.d( "onClick: ",String.valueOf(viewSheet));
//            final BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
//            dialog.setContentView(viewSheet);
//            dialog.show();
        });
    }

    @Override
    public int getItemCount(){ return katalogModels.size();}

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewQty;
        TextView textViewKode;
        ImageView imageViewKatalog;
        CardView cardView;

        ViewHolder(View v){
            super(v);

            textViewName = v.findViewById(R.id.tvNamaKatalog);
            textViewQty = v.findViewById(R.id.tvQty);
            textViewKode = v.findViewById(R.id.tvKode);
            imageViewKatalog = v.findViewById(R.id.imgKatalog);
            cardView = v.findViewById(R.id.cardViewKatalog);
        }
    }

//    public void updateData(List<NearestOutreachModel> newUser){
//        articleModels = new ArrayList<>();
//        articleModels.addAll(newUser);
//        notifyDataSetChanged();
//    }
}
