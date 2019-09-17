package com.example.pakaianbagus.presentation.home.stockopname;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.InputHelper;
import com.example.pakaianbagus.api.StockHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.StockOpnameModel;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.stockopname.adapter.StockOpnameAdapter;
import com.example.pakaianbagus.presentation.home.stockopname.adapter.StockSpinnerAdapter;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.Scanner;
import com.example.pakaianbagus.util.SessionManagement;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class StockOpnameFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnBarcode)
    ImageView btnBarcode;
    @BindView(R.id.btnAdd)
    ImageView btnAdd;

    private Dialog dialog;
    private String roleId;
    private List<StockOpnameModel> stockOpnameModels = new ArrayList<>();
    private List<Stock> stockOpnameList = new ArrayList<>();
    private List<Stock> categoryList = new ArrayList<>();
    private int choose = 1; //1 artikel 2 kategori
    private String idBrand, idToko;
    private StockOpnameAdapter stockOpnameAdapter;
    final int REQUEST_CODE = 564;
    final int REQUEST_SCANNER = 999;

    public StockOpnameFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.stock_opname_fragment, container, false);
        ButterKnife.bind(this, rootView);

        choose = Objects.requireNonNull(getArguments()).getInt("choose");
        idBrand = Objects.requireNonNull(getArguments()).getString("id_brand");
        idToko = Objects.requireNonNull(getArguments()).getString("id_toko");

        try {
            roleId = Session.get(Constanta.ROLE_ID).getValue();
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }

        setScreen();

        swipeRefresh.setOnRefreshListener(() -> {
            stockOpnameList.clear();
            swipeRefresh.setRefreshing(true);
            if (getData()) {
                swipeRefresh.setRefreshing(false);
            }
        });

        stockOpnameAdapter = new StockOpnameAdapter(stockOpnameList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        recyclerView.setAdapter(stockOpnameAdapter);

        return rootView;
    }

    @SuppressLint("SetTextI18n")
    private void setScreen() {
        if (choose == Constanta.STOK_ARTIKEL) {
            btnBarcode.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
            tvTitle.setText("Stock Artikel".toUpperCase());
        } else if (choose == Constanta.STOK_KATEGORI) {
            btnBarcode.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
            tvTitle.setText("Stock Kategori".toUpperCase());
        }
    }

    private boolean getData() {
        StockHelper.getListStock(idToko, new RestCallback<ApiResponse<List<Stock>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Stock>> body) {
                categoryList.addAll(body.getData());
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Log.d("onFailed: ", error.toString());
            }

            @Override
            public void onCanceled() {

            }
        });

        return true;
    }

    @OnClick(R.id.btnBarcode)
    public void btnBarcodeClicked() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        } else {
            Intent intent = new Intent(getContext(), Scanner.class);
            startActivityForResult(intent, REQUEST_SCANNER);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCANNER && resultCode == Activity.RESULT_OK) {
            String resultData = data.getStringExtra("scan_data");
            addListFromBarcode(resultData);
        }
    }

    private void addListFromBarcode(String resultData) {
        Loading.show(getContext());
        InputHelper.getDetailStock(resultData, new RestCallback<ApiResponse<List<Stock>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Stock>> body) {
                Loading.hide(getContext());
                if (body.getData().size() > 0) {
                    Stock stock = body.getData().get(0);
                    if (stockOpnameList.size() > 0) {
                        boolean done = false;
                        for (int x = 0; x < stockOpnameList.size(); x++) {
                            if (stockOpnameList.get(x).getArticleCode().equals(stock.getArticleCode())) {
                                stock.setQty(stockOpnameList.get(x).getQty() + 1);
                                stockOpnameList.set(x, stock);
                                done = true;
                                break;
                            }
                        }
                        if (!done) {
                            stock.setQty(1);
                            stockOpnameList.add(stock);
                        }
                    } else {
                        stock.setQty(1);
                        stockOpnameList.add(stock);
                    }

                    stockOpnameAdapter.notifyDataSetChanged();

                    doPostStock(stock);
                } else {
                    Toast.makeText(getContext(), "Tidak ada dari barcode tersebut, mohon periksa kembali barcode atau coba lagi.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Log.d("onFailed: ", error.getMessage());
                Toast.makeText(getContext(), "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled() {
            }
        });
    }

    @OnClick(R.id.btnAdd)
    public void btnAddClicked() {
        if (categoryList.size() > 0) {
            showDialog(R.layout.dialog_stock_opname_kategori);

            ImageView imgClose = dialog.findViewById(R.id.imgClose);
            EditText etQuantity = dialog.findViewById(R.id.etQuantity);
            Button btnAdd = dialog.findViewById(R.id.btnAdd);
            Spinner spinner = dialog.findViewById(R.id.etNama);

            StockSpinnerAdapter adapter = new StockSpinnerAdapter(Objects.requireNonNull(getContext()),
                    android.R.layout.simple_spinner_item, categoryList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            imgClose.setOnClickListener(v -> dialog.dismiss());
            btnAdd.setOnClickListener(v -> {
                dialog.dismiss();
                addList((Stock) spinner.getSelectedItem(), Integer.parseInt(etQuantity.getText().toString()));
            });
        } else {
            Toast.makeText(getContext(), "Data kategori pada toko ini tidak ada", Toast.LENGTH_SHORT).show();
        }
    }

    private void addList(Stock stock, int qty) {
        if (stockOpnameList.size() > 0) {
            boolean done = false;
            for (int x = 0; x < stockOpnameList.size(); x++) {
                if (stockOpnameList.get(x).getArticleCode().equals(stock.getArticleCode())) {
                    stock.setQty(stockOpnameList.get(x).getQty() + qty);
                    stockOpnameList.set(x, stock);
                    done = true;
                    break;
                }
            }
            if (!done) {
                stock.setQty(qty);
                stockOpnameList.add(stock);
            }
        } else {
            stock.setQty(qty);
            stockOpnameList.add(stock);
        }

        doPostStock(stock);

        stockOpnameAdapter.notifyDataSetChanged();

    }

    private void doPostStock(Stock stock) {
        Loading.show(getContext());
        StockOpnameModel data = new StockOpnameModel();
        data.setMPlaceId(String.valueOf(stock.getMPlaceId()));
        data.setMItemId(String.valueOf(stock.getMItemId()));
        data.setPlaceType(stock.getPlaceType());
        data.setSeriesNumber(stock.getSeriesNumber());
        data.setSizeCode(stock.getSizeCode());
        data.setDescription(stock.getDescription());
        data.setQty(stock.getQty());
        data.setPrice(stock.getPrice());

        StockHelper.postStockOpname(data, new RestCallback<ApiResponse>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse body) {
                Loading.hide(getContext());
                Toast.makeText(getContext(), "Berhasil mengirimkan data Stock Opname", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Toast.makeText(getContext(), "Gagal mengirim data stock opname : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        Bundle bundle = new Bundle();
        bundle.putString("id_brand", idBrand);
        bundle.putInt("choose", choose);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        Fragment fragment;
        if (roleId.equals(SessionManagement.ROLE_SPG)) {
            fragment = new HomeFragment();
        } else {
            fragment = new StockListTokoFragment();
        }
        fragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.mainBaseStockOpname, fragment);
        ft.commit();
    }

    @OnClick(R.id.toolbar_filter)
    public void toolbarFilter() {
        showDialog(R.layout.dialog_filter_stock);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
        Button btnFilterArtikel = dialog.findViewById(R.id.btnFilterArtikel);
        btnFilterArtikel.setOnClickListener(v -> {
            dialog.dismiss();
            choose = Constanta.STOK_ARTIKEL;
            setScreen();
            Loading.show(getContext());
            if (getData()) {
                Loading.hide(getContext());
            }
        });
        Button btnFilterKategori = dialog.findViewById(R.id.btnFilterKategori);
        btnFilterKategori.setOnClickListener(v -> {
            dialog.dismiss();
            choose = Constanta.STOK_KATEGORI;
            setScreen();
            Loading.show(getContext());
            if (getData()) {
                Loading.hide(getContext());
            }
        });
    }

    @OnClick(R.id.toolbar_search)
    public void btnSearch() {
        showDialog(R.layout.dialog_search_stockopname);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void showDialog(int layout) {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(layout);
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
}
