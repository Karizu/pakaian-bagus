package com.example.pakaianbagus.presentation.home.photocounter.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.PlaceWorkModel;
import com.example.pakaianbagus.util.RoundedCornersTransformation;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private List<PlaceWorkModel> picturesList;
    private List<Uri> arrayList;
    private Context context;
    public static int sCorner = 15;
    public static int sMargin = 2;

    public PhotoAdapter(List<PlaceWorkModel> picturesList, Context context){
        this.picturesList = picturesList;
        this.context = context;
    }

    public PhotoAdapter(List<Uri> arrayList, Context context, int i){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_foto_counter, parent, false);

        return new PhotoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder, int position){
        final PlaceWorkModel kunjunganModel = picturesList.get(position);
//        final String id = katalogModel.getId();
//            holder.picture.setImageBitmap(kunjunganModel.getImg());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        kunjunganModel.getImg().compress(Bitmap.CompressFormat.PNG, 100, stream);
        Glide.with(context)
                .asBitmap()
                .load(stream.toByteArray())
                .apply(RequestOptions.bitmapTransform(
                        new RoundedCornersTransformation(context, sCorner, sMargin))).into(holder.picture);


//        Glide.with(context).load(kunjunganModel.getPhoto())
//                .into(holder.picture);

        holder.layoutPhoto.setOnClickListener(view -> {
//            View viewSheet = LayoutInflater.from(view.getContext()).inflate(R.layout.find_outreach_worker_bottom_sheet_dialog, null);
//            Log.d( "onClick: ",String.valueOf(viewSheet));
//            final BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
//            dialog.setContentView(viewSheet);
//            dialog.show();
        });
    }

    @Override
    public int getItemCount(){
        return picturesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        LinearLayout layoutPhoto;

        ViewHolder(View v){
            super(v);

            picture = v.findViewById(R.id.imgPhotoCounter);
            layoutPhoto = v.findViewById(R.id.layoutPhoto);
        }
    }
}
