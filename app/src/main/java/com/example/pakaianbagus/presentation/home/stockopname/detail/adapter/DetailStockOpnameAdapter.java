package com.example.pakaianbagus.presentation.home.stockopname.detail.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.StockHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.api.stockopname.StockCategoryResponse;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.models.stockopname.Details;
import com.example.pakaianbagus.models.stockopname.StockOpnameModels;
import com.example.pakaianbagus.models.stockopname.response.StockOpnameResponse;
import com.example.pakaianbagus.presentation.home.stockopname.StockOpnameFragment;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.Headers;

public class DetailStockOpnameAdapter extends RecyclerView.Adapter<DetailStockOpnameAdapter.ViewHolder> {
    private List<StockOpnameResponse> stockList;
    private Context context;
    private Dialog dialog;
    private Fragment fragment;
    private Boolean isVerify;
    private int selisih = 0;
    private List<Details> detailsList = new ArrayList<>();

    public DetailStockOpnameAdapter(List<StockOpnameResponse> stockList, Context context, Fragment fragment) {
        this.stockList = stockList;
        this.context = context;
        this.fragment = fragment;
    }

    public DetailStockOpnameAdapter(List<StockOpnameResponse> stockList, Context context, Fragment fragment, Boolean isVerify) {
        this.stockList = stockList;
        this.context = context;
        this.fragment = fragment;
        this.isVerify = isVerify;
    }

    @NonNull
    @Override
    public DetailStockOpnameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isVerify){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_stockopname_verify, parent, false);
            return new DetailStockOpnameAdapter.ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_stockopname, parent, false);
            return new DetailStockOpnameAdapter.ViewHolder(v);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DetailStockOpnameAdapter.ViewHolder holder, int position) {
        final StockOpnameResponse stock = stockList.get(position);
//        final int qty = stock.getQty();

        String name = stock.getCreatedAt()+"/"+stock.getBrand().getName();
//        if (stock.getType() == 1) {
//            try {
//                name = stock.getItem().getName();
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//        } else {
//            try {
//                name = stock.getCategory().getName();
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//        }

        holder.textViewName.setText(name);

//        if (isVerify){
//            getSelisihStok(stock.getMPlaceId()+"", stock.getArticleCode(), qty, holder);
//        } else {
//            holder.textViewQty.setText(qty + " pcs");
//            holder.imageViewMore.setOnClickListener(v -> {
//                View v1 = v.findViewById(R.id.btnMore);
//                PopupMenu pm = new PopupMenu(Objects.requireNonNull(context), v1);
//                pm.getMenuInflater().inflate(R.menu.menu_options, pm.getMenu());
//                pm.setOnMenuItemClickListener(menuItem -> {
//                    if (menuItem.getItemId() == R.id.navigation_ubah) {
//                        showDialog(context);
//                        ImageView imgClose = dialog.findViewById(R.id.imgClose);
//                        EditText etQty = dialog.findViewById(R.id.etQty);
//                        Button btnUbah = dialog.findViewById(R.id.btnUbah);
//                        btnUbah.setOnClickListener(v2 -> {
//                            if (!etQty.getText().toString().equals("")) {
//                                dialog.dismiss();
//                                stock.setQty(Integer.parseInt(etQty.getText().toString()));
//                                holder.textViewQty.setText(stock.getQty() + " pcs");
//                                doPosting(stock, stock.getType());
//                            } else {
//                                Toast.makeText(context, "Harap isi field", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        imgClose.setOnClickListener(v2 -> dialog.dismiss());
//                    }
//                    return true;
//                });
//                pm.show();
//            });
//        }

        holder.layoutListStock.setOnClickListener(view -> {
            if (fragment instanceof StockOpnameFragment){
                ((StockOpnameFragment)fragment).onClickItem(stock.getId()+"");
            }
        });
    }

    private void doPosting(StockCategoryResponse response, int type) {
        StockOpnameModels data = new StockOpnameModels();
        if (type == 1) {

            Details details = new Details();
            details.setType("1");
            details.setM_item_id(response.getItem().getId()+"");
            details.setM_category_id(response.getItem().getMCategoryId()+"");
            details.setArticle_code(response.getArticleCode());
            details.setSize_code(response.getSizeCode());
            details.setQty(String.valueOf(response.getQty()+1));

            detailsList.add(details);

            Date c = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(c);

            data.setM_place_id(response.getMPlaceId()+"");
            data.setDate(date);
            data.setPlace_type("S");
            data.setM_brand_id(response.getItem().getMBrandId()+"");

            data.setDetails(detailsList);

//            data.setType(1);
//            data.setmPlaceId(String.valueOf(response.getMPlaceId()));
//            data.setmItemId(String.valueOf(response.getMItemId()));
//            data.setPlaceType(response.getPlaceType());
//            data.setArticleCode(response.getArticleCode());
//            data.setSizeCode(response.getSizeCode());
//            data.setQty(response.getQty());
        } else {
//            data.setType(2);
//            data.setmPlaceId(String.valueOf(response.getMPlaceId()));
//            data.setmCategoryId(String.valueOf(response.getMCategoryId()));
//            data.setPlaceType(response.getPlaceType());
//            data.setQty(response.getQty());
        }
        ((StockOpnameFragment) fragment).doPostStock(data);
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewQty;
        LinearLayout layoutListStock;
        ImageView imageViewMore;

        ViewHolder(View v) {
            super(v);

            textViewName = v.findViewById(R.id.tvName);
            textViewQty = v.findViewById(R.id.tvQty);
            layoutListStock = v.findViewById(R.id.layoutStock);
            imageViewMore = v.findViewById(R.id.btnMore);
        }
    }

    private void showDialog(Context context) {
        dialog = new Dialog(Objects.requireNonNull(context));
        //set content
        dialog.setContentView(R.layout.dialog_ubah_qty);
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

    private void getSelisihStok(String place_id, String article_code, int qty, DetailStockOpnameAdapter.ViewHolder holder){
        Loading.show(context);
        StockHelper.getSelisihStok(place_id, article_code, new RestCallback<ApiResponse<List<Stock>>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Stock>> body) {
                Loading.hide(context);
                List<Stock> res = body.getData();
                for (int i = 0; i < res.size(); i++){
                    Stock stock = res.get(i);
                    selisih = stock.getQty() - qty;
                }
                holder.textViewQty.setText(qty + " pcs selisih "+selisih);
                if (selisih != 0){
                    holder.textViewQty.setTextColor(context.getResources().getColor(R.color.Red));
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(context);
                error.getMessage();
            }

            @Override
            public void onCanceled() {

            }
        });
    }
}
