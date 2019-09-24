package com.example.pakaianbagus.presentation.home.stockopname;


import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.StockHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.Brand;
import com.example.pakaianbagus.models.BrandResponse;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.stockopname.adapter.StockBrandAdapter;
import com.example.pakaianbagus.util.IOnBackPressed;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class StockListBrandFragment extends Fragment implements IOnBackPressed {

    View rootView;
    private List<Brand> brandList;
    private int choose;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_back)
    ImageView imgBack;
    @BindView(R.id.toolbar_search)
    ImageView toolbarSeacrh;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTitleHeader)
    TextView tvTitleHeader;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    public StockListBrandFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.stock_list_brand_fragment, container, false);
        ButterKnife.bind(this, rootView);
        toolbarSeacrh.setVisibility(View.GONE);
        tvTitleHeader.setText("LIST BRAND");
        getCurrentDateChecklist();

        try {
            choose = Objects.requireNonNull(getArguments()).getInt("choose");
        } catch (Exception e) {
            e.printStackTrace();
        }

        brandList = new ArrayList<>();

        getListToko();

        swipeRefresh.setOnRefreshListener(() -> {
            brandList.clear();
            getListToko();
        });

        return rootView;
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy MMMM dd");
        String formattedDate = df.format(c);
        tvDate.setText(formattedDate);
    }

    public void getListToko() {
        swipeRefresh.setRefreshing(true);
        StockHelper.getListBrand(new RestCallback<ApiResponse<List<BrandResponse>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<BrandResponse>> listApiResponse) {
                swipeRefresh.setRefreshing(false);
                try {
                    List<BrandResponse> res = listApiResponse.getData();

                    if (res.size() < 1) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < res.size(); i++) {
                        BrandResponse response = res.get(i);
                        brandList.add(new Brand(response.getId(),
                                response.getName(),
                                response.getCode()));
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    StockBrandAdapter adapter = new StockBrandAdapter(brandList, getContext(), StockListBrandFragment.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    Log.d("TAG EXCEPTION", e.getMessage());
                }
            }

            @Override
            public void onFailed(ErrorResponse errorResponse) {
                Loading.hide(getContext());
                Log.d("TAG onFialed", errorResponse.getMessage());
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public void onClickItem(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id_brand", id);
        bundle.putInt("choose", choose);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        StockListTokoFragment stockFragment = new StockListTokoFragment();
        stockFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, stockFragment);
        ft.commit();
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayout, homeFragment);
        ft.commit();
    }

    @Override
    public boolean onBackPressed() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayout, homeFragment);
        ft.commit();

        return false;
    }

}
