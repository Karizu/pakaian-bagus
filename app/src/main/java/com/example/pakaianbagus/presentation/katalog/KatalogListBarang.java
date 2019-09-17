package com.example.pakaianbagus.presentation.katalog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.KatalogHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.KatalogModel;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.presentation.katalog.adapter.KatalogAdapter;
import com.example.pakaianbagus.presentation.penjualan.ScanBarcodeActivity;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.EndlessRecyclerViewScrollListener;
import com.example.pakaianbagus.util.SessionManagement;
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

public class KatalogListBarang extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.toolbar_filter)
    ImageView toolbar_filter;
    @BindView(R.id.toolbar_back)
    ImageView toolbar_back;

    Dialog dialog;
    View rootView;
    private int limit = 10;
    private int offset = 0;
    private EndlessRecyclerViewScrollListener scrollListener;
    private List<KatalogModel> katalogModels;
    boolean isLoading = false;
    private KatalogAdapter katalogAdapter;
    private String id, idBrand, roleId;
    private List<String> kategori;
    private List<String> terbaru;
    private Spinner spinnerKategori, spinnerTerbaru;

    public KatalogListBarang() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.katalog_fragment, container, false);
        ButterKnife.bind(this, rootView);

        try {
            roleId = Session.get(Constanta.ROLE_ID).getValue();
            if (roleId.equals(SessionManagement.ROLE_SPG)) {
                toolbar_back.setVisibility(View.GONE);
            }
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }

        toolbar_filter.setVisibility(View.VISIBLE);
        LinearLayout layout = rootView.findViewById(R.id.layoutHeaderKatalog);
        layout.setVisibility(View.GONE);
        katalogModels = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
                Log.d("Masuk", "Masuk onLoad || " + page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        id = Objects.requireNonNull(getArguments()).getString("id");
        idBrand = Objects.requireNonNull(getArguments()).getString("id_brand");

        if (id != null) {
            swipeRefresh.setOnRefreshListener(() -> {
                katalogModels.clear();
                getListStokToko();
            });

            getListStokToko();
        }

        return rootView;
    }

    @OnClick(R.id.toolbar_filter)
    void onClickFilter() {
        showDialog(R.layout.dialog_filter_katalog);
        kategori = new ArrayList<>();
        terbaru = new ArrayList<>();
        spinnerKategori = dialog.findViewById(R.id.spinnerKategori);
        spinnerTerbaru = dialog.findViewById(R.id.spinnerTerbaru);
        ImageView btnClose = dialog.findViewById(R.id.imgClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());
        Button btnApply = dialog.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(v -> {
            dialog.dismiss();
            //setsomething this
        });
        setSpinnerKategori();
        setSpinnerTerbaru();

    }

    private void setSpinnerTerbaru() {

        terbaru.add("Pilih Terbaru");
//        kategoriId.add("KI");
        for (int i = 0; i < 5; i++) {
//            CategoryModel category = res.get(i);
            terbaru.add("Terbaru " + i);
//            kategoriId.add(category.getId());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, terbaru) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
        // attaching data adapter to spinner
        spinnerTerbaru.setAdapter(dataAdapter);

        spinnerTerbaru.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (subKategori != null) {
//                    subKategori.clear();
//                    subKategoriId.clear();
//                }
//
//                setSpinnerNestedKatgori(position);
//
//                if (position != 0) {
//                    kategoriesId = kategoriId.get(position);
//                } else {
//                    kategoriesId = null;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSpinnerKategori() {
        kategori.add("Pilih Kategori");
//        kategoriId.add("KI");
        for (int i = 0; i < 5; i++) {
//            CategoryModel category = res.get(i);
            kategori.add("Kategori " + i);
//            kategoriId.add(category.getId());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
                        R.layout.layout_spinner_text, kategori) {
                    @Override
                    public boolean isEnabled(int position) {
                        return position != 0;
                    }
                };

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
        // attaching data adapter to spinner
        spinnerKategori.setAdapter(dataAdapter);

        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (subKategori != null) {
//                    subKategori.clear();
//                    subKategoriId.clear();
//                }
//
//                setSpinnerNestedKatgori(position);
//
//                if (position != 0) {
//                    kategoriesId = kategoriId.get(position);
//                } else {
//                    kategoriesId = null;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getListStokToko() {
        swipeRefresh.setRefreshing(true);

        KatalogHelper.getListStokToko(id, idBrand, limit, offset, new RestCallback<ApiResponse<List<Stock>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Stock>> body) {
                swipeRefresh.setRefreshing(false);
                if (body.getData() != null) {
                    List<Stock> stocks = body.getData();

                    if (stocks.size() < 1) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < stocks.size(); i++) {
                        Stock stock = stocks.get(i);
                        katalogModels.add(new KatalogModel(stock.getArticleCode(),
                                stock.getItem().getName(),
                                stock.getItem().getImage(),
                                stock.getQty(),
                                stock.getPrice()));
                    }

                    katalogAdapter = new KatalogAdapter(katalogModels, getContext());
                    recyclerView.setAdapter(katalogAdapter);

                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
                Log.d("TAG ERROR", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("id_brand", idBrand);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        KatalogFragment katalogFragment = new KatalogFragment();
        katalogFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.layoutKatalog, katalogFragment);
        ft.commit();
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.toolbar_search)
    public void toolbarSearch() {
        showDialog(R.layout.dialog_search_stockopname);
        TextView toolbar = dialog.findViewById(R.id.tvToolbar);
        toolbar.setText("SEARCH KATALOG");
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
        EditText etKodeNamaBarang = dialog.findViewById(R.id.etKodeNamaBarang);
        Button btnScanBarcode = dialog.findViewById(R.id.btnScanBarcode);
        btnScanBarcode.setVisibility(View.VISIBLE);
        btnScanBarcode.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
            intent.putExtra("mode", "KATALOG");
            startActivity(intent);
        });
        Button btnCari = dialog.findViewById(R.id.btnCari);
        btnCari.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("keyword", etKodeNamaBarang.getText().toString());

            if (etKodeNamaBarang.getText().toString().equals("")) {
                etKodeNamaBarang.setError("Field harus diisi");
            } else {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                SearchKatalogFragment katalogFragment = new SearchKatalogFragment();
                katalogFragment.setArguments(bundle);
                ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                ft.replace(R.id.layoutKatalog, katalogFragment);
                ft.commit();

                dialog.dismiss();
            }
        });
    }

    public void loadNextDataFromApi(int offset) {
        swipeRefresh.setRefreshing(true);
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        try {
            KatalogHelper.getListStokToko(id, idBrand, limit, limit * offset, new RestCallback<ApiResponse<List<Stock>>>() {
                @Override
                public void onSuccess(Headers headers, ApiResponse<List<Stock>> body) {
                    swipeRefresh.setRefreshing(false);
                    try {
                        if (body != null) {
                            List<Stock> res = body.getData();
                            List<KatalogModel> katalogModelList = new ArrayList<>();
                            for (int i = 0; i < res.size(); i++) {
                                Stock stock = res.get(i);
                                katalogModelList.add(new KatalogModel(stock.getArticleCode(),
                                        stock.getItem().getName(),
                                        stock.getItem().getImage(),
                                        stock.getQty(),
                                        stock.getPrice()));
                            }
                            Log.d("Masuk", "Masuk onLoad");
                            katalogModels.addAll(katalogModelList);
                            katalogAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailed(ErrorResponse error) {
                    Log.i("response", "Response Failed");
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onCanceled() {
                    Log.i("response", "Response Failed");
                }
            });
        } catch (Exception e) {

        }
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

