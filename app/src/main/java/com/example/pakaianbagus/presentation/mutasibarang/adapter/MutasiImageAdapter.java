package com.example.pakaianbagus.presentation.mutasibarang.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.api.mutation.detail.Detail;
import com.example.pakaianbagus.util.RoundedCornersTransformation;

import java.util.List;

import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sCorner;
import static com.example.pakaianbagus.presentation.home.photocounter.adapter.PhotoAdapter.sMargin;

/**
 * Created by alfianhpratama on 17/09/2019.
 * Organization: UTeam
 */
public class MutasiImageAdapter extends RecyclerView.Adapter<MutasiImageAdapter.ViewHolder> {

    private List<Detail> details;
    private Context context;

    public MutasiImageAdapter(List<Detail> details, Context context) {
        this.details = details;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        View v = LayoutInflater.from(group.getContext()).inflate(R.layout.content_item_image, group, false);
        return new MutasiImageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        String img = details.get(i).getStock().getItem().getImage();
        Glide.with(context)
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.jeans)
                        .error(R.drawable.jeans)).load(img).apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(context, sCorner, sMargin)))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
