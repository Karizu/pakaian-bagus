package com.example.pakaianbagus.presentation.katalog;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.KatalogHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.KatalogModel;
import com.example.pakaianbagus.models.stock.StokToko;
import com.example.pakaianbagus.presentation.katalog.adapter.KatalogAdapter;
import com.example.pakaianbagus.util.EndlessRecyclerViewScrollListener;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class SearchKatalogFragment extends Fragment {

    Dialog dialog;
    View rootView;
    private List<KatalogModel> katalogModels;
    boolean isLoading = false;
    private KatalogAdapter katalogAdapter;
    private String id, keyword;
    private int limit = 10;
    private int offset = 0;
    private EndlessRecyclerViewScrollListener scrollListener;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_search)
    ImageView toolbar_search;
    @BindView(R.id.layoutHeaderKatalog)
    LinearLayout layout;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tvNoData)
    TextView tvNoData;

    public SearchKatalogFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.katalog_fragment, container, false);
        ButterKnife.bind(this, rootView);

        toolbar_search.setVisibility(View.GONE);
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
        keyword = Objects.requireNonNull(getArguments()).getString("keyword");

        if (id != null){
            swipeRefresh.setOnRefreshListener(() -> {
                katalogModels.clear();
                getListStokToko(id, keyword);
            });
            getListStokToko(id, keyword);
        }

        return rootView;
    }

    private void getListStokToko(String id, String keyword){
        swipeRefresh.setRefreshing(true);

        KatalogHelper.searchKatalog(id, keyword, limit, offset, new RestCallback<ApiResponse<List<StokToko>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<StokToko>> body) {
                swipeRefresh.setRefreshing(false);
                if (body.getData() != null){
                    List<StokToko> stokTokos = body.getData();

                    if (stokTokos.size() < 1){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                        for (int i = 0; i < stokTokos.size(); i++){
                            StokToko stokToko = stokTokos.get(i);
                            katalogModels.add(new KatalogModel(stokToko.getArticleCode(),
                                    stokToko.getItem().getName(),
                                    stokToko.getItem().getImage(),
                                    stokToko.getQty(),
                                    stokToko.getPrice()));
                        }

                    katalogAdapter = new KatalogAdapter(katalogModels, getContext());
                    recyclerView.setAdapter(katalogAdapter);

                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
                Log.d("onFailed Search Katalog", error.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public void loadNextDataFromApi(int offset) {
        swipeRefresh.setRefreshing(true);
        try {
            KatalogHelper.searchKatalog(id, keyword, limit, limit*offset, new RestCallback<ApiResponse<List<StokToko>>>() {
                @Override
                public void onSuccess(Headers headers, ApiResponse<List<StokToko>> body) {
                    swipeRefresh.setRefreshing(false);
                    try {
                        if (body != null) {
                            List<StokToko> res = body.getData();
                            List<KatalogModel> katalogModelList = new ArrayList<>();
                            for (int i = 0; i < res.size(); i++) {
                                StokToko stokToko = res.get(i);
                                katalogModelList.add(new KatalogModel(stokToko.getArticleCode(),
                                        stokToko.getItem().getName(),
                                        stokToko.getItem().getImage(),
                                        stokToko.getQty(),
                                        stokToko.getPrice()));
                            }
                            Log.d("Masuk", "Masuk onLoad");
                            katalogModels.addAll(katalogModelList);
                            katalogAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        KatalogListBarang katalogFragment = new KatalogListBarang();
        katalogFragment.setArguments(bundle);
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

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
            KatalogFragment katalogFragment = new KatalogFragment();
            ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
            ft.replace(R.id.layoutKatalog, katalogFragment);
            ft.commit();
        });
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

