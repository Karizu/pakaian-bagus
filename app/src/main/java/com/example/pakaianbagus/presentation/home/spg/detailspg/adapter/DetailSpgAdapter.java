package com.example.pakaianbagus.presentation.home.spg.detailspg.adapter;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.home.spg.detailspg.model.DetailSpgModel;

import java.util.List;

public class DetailSpgAdapter extends RecyclerView.Adapter<DetailSpgAdapter.ViewHolder> {
    private List<DetailSpgModel> detailSpgModels;
    private Context context;
    private Dialog dialog;
    private View v;

    public DetailSpgAdapter(List<DetailSpgModel> detailSpgModels, Context context) {
        this.detailSpgModels = detailSpgModels;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailSpgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_item_detail_spg, parent, false);

        return new DetailSpgAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailSpgAdapter.ViewHolder holder, int position) {
        final DetailSpgModel detailSpgModel = detailSpgModels.get(position);
        final String date = detailSpgModel.getDate();
        final String startTime = detailSpgModel.getStartTime();
        final String endTime = detailSpgModel.getEndTime();

        holder.textViewName.setText(date);
        holder.textViewStartTime.setText(startTime);
        holder.textViewEndTime.setText(endTime);
        holder.layoutDetailSpg.setOnClickListener(view -> {
            Snackbar.make(view, date + "\n" +startTime+" - "+endTime, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

    }

    @Override
    public int getItemCount() {
        return detailSpgModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewStartTime;
        TextView textViewEndTime;
        LinearLayout layoutDetailSpg;

        ViewHolder(View v) {
            super(v);

            textViewName = v.findViewById(R.id.tvDate);
            textViewStartTime = v.findViewById(R.id.tvStartTime);
            textViewEndTime = v.findViewById(R.id.tvEndTime);
            layoutDetailSpg = v.findViewById(R.id.layoutDetailSpg);
        }
    }
}
