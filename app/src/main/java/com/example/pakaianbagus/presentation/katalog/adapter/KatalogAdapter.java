package com.example.pakaianbagus.presentation.katalog.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.KatalogModel;

import java.util.List;

public class KatalogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KatalogModel> katalogModels;
    private Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public KatalogAdapter(List<KatalogModel> katalogModels, Context context){
        this.katalogModels = katalogModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_item_katalog, parent, false);
            return new KatalogAdapter.ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new KatalogAdapter.ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder){
            populateItemRows((ViewHolder) viewHolder, i);
        } else if(viewHolder instanceof LoadingViewHolder){
            showLoadingView((LoadingViewHolder)viewHolder, i);
        }
    }

//    @Override
//    public void onBindViewHolder(@NonNull KatalogAdapter.ViewHolder holder, int position){
//
//
//        populateItemRows(holder, position);
//
//        final KatalogModel katalogModel = katalogModels.get(position);
////        final String id = katalogModel.getId();
//        final String name = katalogModel.getName();
//        final String image = katalogModel.getImage();
//        final String qty = katalogModel.getQty();
//        final String kode = katalogModel.getKode();
//
//        holder.textViewName.setText(name);
//        holder.textViewQty.setText(qty);
//        holder.textViewKode.setText(kode);
//        Glide.with(context).load(image).into(holder.imageViewKatalog);
////        Glide.with(context).load(image).apply(RequestOptions.circleCropTransform()).into(holder.imageViewKatalog);
//
//        holder.cardView.setOnClickListener(view -> {
//            Toast.makeText(context, "Celana Jeans", Toast.LENGTH_SHORT).show();
////            View viewSheet = LayoutInflater.from(view.getContext()).inflate(R.layout.find_outreach_worker_bottom_sheet_dialog, null);
////            Log.d( "onClick: ",String.valueOf(viewSheet));
////            final BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
////            dialog.setContentView(viewSheet);
////            dialog.show();
//        });
//    }

    @Override
    public int getItemCount(){ return katalogModels == null ? 0 : katalogModels.size();}

    @Override
    public int getItemViewType(int position) {
        return katalogModels.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
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

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(View v) {
            super(v);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    @SuppressLint("SetTextI18n")
    private void populateItemRows(ViewHolder viewHolder, int position) {

        if (katalogModels.get(position) != null){
            String name = katalogModels.get(position).getName();
            viewHolder.textViewName.setText(name);
            String qty = katalogModels.get(position).getQty();
            viewHolder.textViewQty.setText(qty+ " pcs");
            String kode = katalogModels.get(position).getKode();
            viewHolder.textViewKode.setText(kode);
            String img = katalogModels.get(position).getImage();
            Glide.with(context).load(img).into(viewHolder.imageViewKatalog);
            viewHolder.cardView.setOnClickListener(v -> {

            });
        }

    }

//    public void updateData(List<NearestOutreachModel> newUser){
//        articleModels = new ArrayList<>();
//        articleModels.addAll(newUser);
//        notifyDataSetChanged();
//    }
}
