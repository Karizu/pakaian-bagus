package com.example.pakaianbagus.presentation.home.spg.adapter;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.SpgHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.auth.Group;
import com.example.pakaianbagus.models.auth.Place;
import com.example.pakaianbagus.presentation.home.spg.SpgListFragment;
import com.example.pakaianbagus.models.SpgModel;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.util.List;

import okhttp3.Headers;

public class SpgAdapter extends RecyclerView.Adapter<SpgAdapter.ViewHolder> {
    private List<SpgModel> spgModels;
    private Context context;
    private Dialog dialog;
    private SpgListFragment spgListFragment;

    public SpgAdapter(List<SpgModel> spgModels, Context context, SpgListFragment spgListFragment) {
        this.spgModels = spgModels;
        this.context = context;
        this.spgListFragment = spgListFragment;
    }

    @NonNull
    @Override
    public SpgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_spg, parent, false);
        return new SpgAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SpgAdapter.ViewHolder holder, int position) {
        final SpgModel spgModel = spgModels.get(position);
        final String id = spgModel.getId();
        final String name = spgModel.getName();
        final String groupId = spgModel.getToko();

        holder.textViewName.setText(name);
        getDetailPlace(groupId, holder);
        holder.layoutSpg.setOnClickListener(view ->
                spgListFragment.onClickLayoutListSPG(id)
        );
    }

    @Override
    public int getItemCount() {
        return spgModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView tvNamaToko;
        CardView layoutSpg;

        ViewHolder(View v) {
            super(v);

            textViewName = v.findViewById(R.id.tvName);
            tvNamaToko = v.findViewById(R.id.tvNamaToko);
            layoutSpg = v.findViewById(R.id.cvSpg);
        }
    }

    private void getDetailPlace(String groupId, SpgAdapter.ViewHolder holder){
        Loading.show(context);
        SpgHelper.getDetailPlace(groupId, new RestCallback<ApiResponse<Group>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<Group> body) {
                Loading.hide(context);
                try {
                    Group place = body.getData();
                    holder.tvNamaToko.setText(place.getName());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(context);
            }

            @Override
            public void onCanceled() {

            }
        });
    }
}
