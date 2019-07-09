package com.example.pakaianbagus.presentation.katalog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.KatalogHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.KatalogModel;
import com.example.pakaianbagus.models.StokToko;
import com.example.pakaianbagus.presentation.katalog.adapter.KatalogAdapter;
import com.example.pakaianbagus.util.EndlessRecyclerViewScrollListener;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class KatalogListBarang extends Fragment {

    Dialog dialog;
    View rootView;
    private int limit = 10;
    private int offset = 0;
    private EndlessRecyclerViewScrollListener scrollListener;
    private List<KatalogModel> katalogModels;
    boolean isLoading = false;
    private KatalogAdapter katalogAdapter;
    private String id;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public KatalogListBarang() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.katalog_fragment, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayout layout = rootView.findViewById(R.id.layoutHeaderKatalog);
        layout.setVisibility(View.GONE);
        katalogModels = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
                Log.d("Masuk", "Masuk onLoad || "+page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        id = Objects.requireNonNull(getArguments()).getString("id");

        if (id != null){
            getListStokToko();
        }

        return rootView;
    }

    private void initAdapter() {
        katalogAdapter = new KatalogAdapter(katalogModels, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(katalogAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == katalogModels.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        Loading.show(getContext());
        KatalogHelper.getListStokToko(id, limit, offset, new RestCallback<ApiResponse<List<StokToko>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<StokToko>> body) {
                if (body.getData() != null){
                    Loading.hide(getContext());
                    List<StokToko> stokTokos = body.getData();
                    katalogModels.add(null);
                    katalogAdapter.notifyItemInserted(katalogModels.size() - 1);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        katalogModels.remove(katalogModels.size() - 1);
                        int scrollPosition = katalogModels.size();
                        katalogAdapter.notifyItemRemoved(scrollPosition);
                        int currentSize = scrollPosition;
                        int nextLimit = currentSize + 10;

                        while (currentSize - 1 < nextLimit) {
                            StokToko stokToko = stokTokos.get(currentSize);
                            katalogModels.add(new KatalogModel(stokToko.getId_artikel(),
                                    stokToko.getNama_barang(),
                                    stokToko.getGambar(),
                                    stokToko.getTotal_barang(),
                                    stokToko.getNo_artikel()));
                            currentSize++;
                        }

                        katalogAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }, 2000);
//                    for (int i = 0; i < 10; i++){
//                        StokToko stokToko = stokTokos.get(i);
//                        katalogModels.add(new KatalogModel(stokToko.getId_stok_toko(),
//                                stokToko.getNama_barang(),
//                                stokToko.getPath(),
//                                stokToko.getQty(),
//                                stokToko.getBarcode()));
//                    }
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                Loading.hide(getContext());
                Log.d("onFailed List Katalog", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void getListStokToko(){
        Loading.show(getContext());

        KatalogHelper.getListStokToko(id, limit, offset, new RestCallback<ApiResponse<List<StokToko>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<StokToko>> body) {
                Loading.hide(getContext());
                if (body.getData() != null){
                    List<StokToko> stokTokos = body.getData();

                        for (int i = 0; i < stokTokos.size(); i++){
                            StokToko stokToko = stokTokos.get(i);
                            katalogModels.add(new KatalogModel(stokToko.getId_artikel(),
                                    stokToko.getNama_barang(),
                                    stokToko.getGambar(),
                                    stokToko.getTotal_barang(),
                                    stokToko.getNo_artikel()));
                        }

                    katalogAdapter = new KatalogAdapter(katalogModels, getContext());
//                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    recyclerView.setAdapter(katalogAdapter);

                }
            }

            @Override
            public void onFailed(ErrorResponse error) {

            }

            @Override
            public void onCanceled() {

            }
        });
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        KatalogFragment katalogFragment = new KatalogFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.layoutKatalog, katalogFragment);
        ft.commit();
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.toolbar_search)
    public void toolbarSearch() {
        showDialog();
        TextView toolbar = dialog.findViewById(R.id.tvToolbar);
        toolbar.setText("SEARCH KATALOG");
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
        EditText etKodeNamaBarang = dialog.findViewById(R.id.etKodeNamaBarang);
        Button btnCari = dialog.findViewById(R.id.btnCari);
        btnCari.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("keyword", etKodeNamaBarang.getText().toString());

            if (etKodeNamaBarang.getText().toString().equals("")){
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
        Loading.show(getContext());
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        try {
            KatalogHelper.getListStokToko(id, limit, limit*offset, new RestCallback<ApiResponse<List<StokToko>>>() {
                @Override
                public void onSuccess(Headers headers, ApiResponse<List<StokToko>> body) {
                    Loading.hide(getContext());
                    try {
                        if (body != null) {
                            List<StokToko> res = body.getData();
                            List<KatalogModel> katalogModelList = new ArrayList<>();
                            for (int i = 0; i < res.size(); i++) {
                                StokToko stokToko = res.get(i);
                                katalogModelList.add(new KatalogModel(stokToko.getId_artikel(),
                                        stokToko.getNama_barang(),
                                        stokToko.getGambar(),
                                        stokToko.getTotal_barang(),
                                        stokToko.getNo_artikel()));
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
                    Loading.hide(getContext());
                }

                @Override
                public void onCanceled() {
                    Log.i("response", "Response Failed");
                }
            });
        } catch (Exception e) {

        }
    }


    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_search_stockopname);
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

