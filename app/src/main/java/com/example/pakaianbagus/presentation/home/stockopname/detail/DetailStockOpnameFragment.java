package com.example.pakaianbagus.presentation.home.stockopname.detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.InputHelper;
import com.example.pakaianbagus.api.StockHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.api.StockCategory;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.models.stockopname.Details;
import com.example.pakaianbagus.models.stockopname.StockOpnameModels;
import com.example.pakaianbagus.models.stockopname.response.StockOpnameResponse;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.stockopname.StockListTokoFragment;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class DetailStockOpnameFragment extends Fragment {

    @BindView(R.id.rvArtikel)
    RecyclerView rvArtikel;
    @BindView(R.id.rvKategori)
    RecyclerView rvKategori;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnBarcode)
    ImageView btnBarcode;
    @BindView(R.id.btnAdd)
    ImageView btnAdd;

    @OnClick(R.id.btnSubmit)
    void onClickSubmit(){

        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(c);
        StockOpnameModels model = new StockOpnameModels();

        model.setM_place_id(idToko);
        model.setDate(date);
        model.setPlace_type("S");
        model.setM_brand_id(idBrand);

        model.setDetails(detailsList);
        doPostStock(model);

    }

    private Dialog dialog;
    private String roleId;
    private List<StockOpnameResponse> stockOpnameArticleList = new ArrayList<>();
    private List<StockOpnameResponse> stockOpnameCategoryList = new ArrayList<>();
    private List<StockCategory> categoryList = new ArrayList<>();
    private int choose = Constanta.STOK_ARTIKEL;
    private String idBrand, idToko, id;
    private StockOpnameAdapter artikelAdapter, kategoriAdapter;
    private final int REQUEST_CODE = 564;
    private final int REQUEST_SCANNER = 999;
    private List<Details> detailsList = new ArrayList<>();

    public DetailStockOpnameFragment() {
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
            if (roleId.equals(SessionManagement.ROLE_SPG)){
                idBrand = Session.get(Constanta.BRAND).getValue();
                idToko = Session.get(Constanta.TOKO).getValue();
                Log.d("TAG ID", idBrand+" "+idToko);
            }
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }

        setScreen();

        swipeRefresh.setOnRefreshListener(() -> {
            stockOpnameArticleList.clear();
            stockOpnameCategoryList.clear();
            swipeRefresh.setRefreshing(true);
            if (choose == 1) {
                if (getDataArticle()) {
                    swipeRefresh.setRefreshing(false);
                }
            } else {
                if (getDataCategory()) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        });

        initData();

        artikelAdapter = new StockOpnameAdapter(stockOpnameArticleList, getContext(), DetailStockOpnameFragment.this, false);
        rvArtikel.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvArtikel.setAdapter(artikelAdapter);

        kategoriAdapter = new StockOpnameAdapter(stockOpnameCategoryList, getContext(), DetailStockOpnameFragment.this, false);
        rvKategori.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvKategori.setAdapter(kategoriAdapter);

        return rootView;
    }

    private void initData() {
        Loading.show(getContext());
        if (getDataArticle() && getListStock() && getDataCategory()) {
            Loading.hide(getContext());
        }
    }

    private boolean getDataArticle() {
        StockHelper.getListStockOpname(idBrand, idToko, 1, new RestCallback<ApiResponse<List<StockOpnameResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<StockOpnameResponse>> body) {
                stockOpnameArticleList.clear();
                stockOpnameArticleList.addAll(body.getData());
                artikelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(ErrorResponse error) {

            }

            @Override
            public void onCanceled() {

            }
        });
        return true;
    }

    private boolean getDataCategory() {
        StockHelper.getListStockOpname(idBrand, idToko, 2, new RestCallback<ApiResponse<List<StockOpnameResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<StockOpnameResponse>> body) {
                stockOpnameCategoryList.clear();
                stockOpnameCategoryList.addAll(body.getData());
                kategoriAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(ErrorResponse error) {

            }

            @Override
            public void onCanceled() {

            }
        });
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void setScreen() {
        if (choose == Constanta.STOK_ARTIKEL) {
            btnBarcode.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
            rvArtikel.setVisibility(View.VISIBLE);
            rvKategori.setVisibility(View.GONE);
            tvTitle.setText("Stock Artikel".toUpperCase());
        } else if (choose == Constanta.STOK_KATEGORI) {
            btnBarcode.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
            rvArtikel.setVisibility(View.GONE);
            rvKategori.setVisibility(View.VISIBLE);
            tvTitle.setText("Stock Kategori".toUpperCase());
        }
    }

    private boolean getListStock() {
        StockHelper.getListStock(idToko, idBrand, new RestCallback<ApiResponse<List<StockCategory>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<StockCategory>> body) {
                categoryList.clear();
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
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA)
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
        InputHelper.getDetailStockSPG(resultData, idToko, idBrand, new RestCallback<ApiResponse<List<Stock>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Stock>> body) {
                Loading.hide(getContext());
                if (body.getData().size() > 0) {
                    Stock stock = body.getData().get(0);
                    String mPlaceId = String.valueOf(stock.getMPlaceId());
                    String mBrandId = String.valueOf(stock.getItem().getMBrandId());
                    boolean placeTrue = idToko.equals(mPlaceId);
                    boolean brandTrue = idBrand.equals(mBrandId);
                    Log.d("TAG IDS", mBrandId+" "+mPlaceId);
                    if (placeTrue && brandTrue) {

                        Details details = new Details();
                        details.setType("2");
                        details.setM_item_id(stock.getItem().getId()+"");
                        details.setM_category_id(stock.getItem().getMCategoryId()+"");
                        details.setArticle_code(stock.getArticleCode());
                        details.setSize_code(stock.getSizeCode());
                        details.setQty(String.valueOf(stock.getQty()+1));

                        detailsList.add(details);
                    } else {
                        Log.d("TAG", "MASUK SO");
                        Toast.makeText(getContext(), "Tolong scan barcode sesuai dengan toko dan brand anda", Toast.LENGTH_SHORT).show();
                    }
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
                StockCategory response = (StockCategory) spinner.getSelectedItem();
                StockOpnameModels data = new StockOpnameModels();



                Details details = new Details();
                details.setType("2");
//                details.setSize_code(response.ge);
//                data.setType(2);
//                data.setmPlaceId(String.valueOf(response.getMPlaceId()));
//                data.setmCategoryId(String.valueOf(response.getMCategoryId()));
//                data.setPlaceType("S");
//                data.setQty(Integer.parseInt(etQuantity.getText().toString()));

                doPostStock(data);
            });
        } else {
            Toast.makeText(getContext(), "Data kategori pada toko ini tidak ada", Toast.LENGTH_SHORT).show();
        }
    }

    public void doPostStock(StockOpnameModels data) {
        Loading.show(getContext());

        StockHelper.postStockOpname(data, new RestCallback<ApiResponse>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse body) {
                Toast.makeText(getContext(), "Berhasil mengirimkan data Stock Opname ", Toast.LENGTH_SHORT).show();
                if (choose == 1) {
                    if (getDataArticle()) {
                        Loading.hide(getContext());
                    }
                } else {
                    if (getDataCategory()) {
                        Loading.hide(getContext());
                    }
                }
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
            if (getDataArticle()) {
                Loading.hide(getContext());
            }
        });
        Button btnFilterKategori = dialog.findViewById(R.id.btnFilterKategori);
        btnFilterKategori.setOnClickListener(v -> {
            dialog.dismiss();
            choose = Constanta.STOK_KATEGORI;
            setScreen();
            Loading.show(getContext());
            if (getDataCategory()) {
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
