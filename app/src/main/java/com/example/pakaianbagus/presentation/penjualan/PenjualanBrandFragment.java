package com.example.pakaianbagus.presentation.penjualan;

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
import com.example.pakaianbagus.api.PenjualanHelper;
import com.example.pakaianbagus.api.SpgHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.Brand;
import com.example.pakaianbagus.models.BrandResponse;
import com.example.pakaianbagus.presentation.penjualan.adapter.PenjualanBrandAdapter;
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
import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 */
public class PenjualanBrandFragment extends Fragment {

    View rootView;
    private List<Brand> brandList;

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

    public PenjualanBrandFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.penjualan_brand_fragment, container, false);
        ButterKnife.bind(this, rootView);
        imgBack.setVisibility(View.GONE);
        toolbarSeacrh.setVisibility(View.GONE);
        tvTitleHeader.setText("LIST BRAND");
        getCurrentDateChecklist();

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
        PenjualanHelper.getListBrand(new RestCallback<ApiResponse<List<BrandResponse>>>() {
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
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
                    PenjualanBrandAdapter adapter = new PenjualanBrandAdapter(brandList, getContext(), PenjualanBrandFragment.this);
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
        bundle.putString("id", id);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        PenjualanListTokoFragment katalogFragment = new PenjualanListTokoFragment();
        katalogFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.layoutKatalog, katalogFragment);
        ft.commit();
    }

}
