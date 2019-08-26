package com.example.pakaianbagus.presentation.barangmasuk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.BarangHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.PenerimaanBarangResponse;
import com.example.pakaianbagus.presentation.barangmasuk.adapter.BarangMasukAdapter;
import com.example.pakaianbagus.presentation.barangmasuk.detailbm.DetailBarangMasuk;
import com.example.pakaianbagus.models.BarangMasukModel;
import com.example.pakaianbagus.util.EndlessRecyclerViewScrollListener;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;

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

public class BarangMasukFragment extends Fragment {

    private List<BarangMasukModel> barangMasukModels;
    private String id;
    private int limit = 10;
    private int offset = 0;
    private BarangMasukAdapter barangMasukAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_history)
    ImageView toolbarHistory;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    public BarangMasukFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.barang_masuk_fragment, container, false);
        ButterKnife.bind(this, rootView);

        toolbarTitle.setText("BARANG MASUK");
        toolbarHistory.setVisibility(View.GONE);

        barangMasukModels = new ArrayList<>();
        getCurrentDateChecklist();

        id = Objects.requireNonNull(getArguments()).getString("id");
        if (id != null){
            getListBarangMasuk();

            swipeRefresh.setOnRefreshListener(() -> {
                barangMasukModels.clear();
                getListBarangMasuk();
            });
        }

        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false);
        recyclerView.setLayoutManager(linearLayout);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
                Log.d("Masuk", "Masuk onLoad || "+page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        return rootView;
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy MMMM dd");
        String formattedDate = df.format(c);
        tvDate.setText(formattedDate);
    }

    private void getListBarangMasuk(){
        swipeRefresh.setRefreshing(true);
        BarangHelper.getListBarangMasuk(id, limit, offset, new RestCallback<ApiResponse<List<PenerimaanBarangResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<PenerimaanBarangResponse>> body) {
                swipeRefresh.setRefreshing(false);
                if (body.getData() != null){
                    List<PenerimaanBarangResponse> res = body.getData();

                    if (res.size() < 1){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < res.size(); i++){
                        PenerimaanBarangResponse barangResponse = res.get(i);
                        barangMasukModels.add(new BarangMasukModel(
                                barangResponse.getId_penerimaan(),
                                barangResponse.getNo_bukti_penerimaan(),
                                barangResponse.getTotal_barang_diterima(),
                                barangResponse.getStatus()));

                    }
                    barangMasukAdapter = new BarangMasukAdapter(barangMasukModels, getContext(), BarangMasukFragment.this);
                    recyclerView.setAdapter(barangMasukAdapter);
                }
            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
                Log.d("onFailed List BM", error.getMessage());
            }

            @Override
            public void onCanceled() {

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
            BarangHelper.getListBarangMasuk(id, limit, limit*offset, new RestCallback<ApiResponse<List<PenerimaanBarangResponse>>>() {
                @Override
                public void onSuccess(Headers headers, ApiResponse<List<PenerimaanBarangResponse>> body) {
                    swipeRefresh.setRefreshing(false);
                    if (body.getData() != null){
                        List<PenerimaanBarangResponse> res = body.getData();
                        List<BarangMasukModel> barangMasukModelList = new ArrayList<>();
                        for (int i = 0; i < res.size(); i++){
                            PenerimaanBarangResponse barangResponse = res.get(i);
                            barangMasukModelList.add(new BarangMasukModel(
                                    barangResponse.getId_penerimaan(),
                                    barangResponse.getNo_bukti_penerimaan(),
                                    barangResponse.getTotal_barang_diterima(),
                                    barangResponse.getStatus()));

                        }
                        barangMasukModels.addAll(barangMasukModelList);
                        barangMasukAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailed(ErrorResponse error) {
                    swipeRefresh.setRefreshing(false);
                    Log.d("onFailed List BM", error.getMessage());
                }

                @Override
                public void onCanceled() {

                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void layoutListBarangMasuk(String ids){
        Bundle bundle = new Bundle();
        bundle.putString("id", ids);
        bundle.putString("id_barang_masuk", id);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        DetailBarangMasuk detailBMFragment = new DetailBarangMasuk();
        detailBMFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutBM, detailBMFragment);
        ft.commit();
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        ListTokoBMFragment listTokoBMFragment = new ListTokoBMFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutBM, listTokoBMFragment);
        ft.commit();
    }

}
