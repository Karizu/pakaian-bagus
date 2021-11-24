package com.example.pakaianbagus.presentation.mutasibarang;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.SpgHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.Brand;
import com.example.pakaianbagus.models.BrandResponse;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.spg.adapter.SpgBrandAdapter;
import com.example.pakaianbagus.util.Constanta;
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
public class ListBrandMutasiBarangFragment extends Fragment {

    View rootView;
    private List<Brand> brandList;
    private String flagMutasi;

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
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    public ListBrandMutasiBarangFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.spg_list_brand_fragment, container, false);
        ButterKnife.bind(this, rootView);
        toolbarSeacrh.setVisibility(View.GONE);

        try {
            flagMutasi = Objects.requireNonNull(getArguments()).getString(Constanta.FLAG_MUTASI);
        } catch (Exception e){
            e.printStackTrace();
        }

        toolbar_title.setText("MUTASI BARANG");
        tvTitleHeader.setText("LIST BRAND");
        getCurrentDateChecklist();

        brandList = new ArrayList<>();

        getListBrand();


        swipeRefresh.setOnRefreshListener(() -> {
            brandList.clear();
            getListBrand();
        });

        return rootView;
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy MMMM dd");
        String formattedDate = df.format(c);
        tvDate.setText(formattedDate);
    }

    public void getListBrand() {
        swipeRefresh.setRefreshing(true);
        SpgHelper.getListBrand(new RestCallback<ApiResponse<List<BrandResponse>>>() {
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
                    @SuppressLint("WrongConstant") LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
                    SpgBrandAdapter adapter = new SpgBrandAdapter(brandList, getContext(), ListBrandMutasiBarangFragment.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    Log.d("TAG EXCEPTION", Objects.requireNonNull(e.getMessage()));
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
        bundle.putString("brand_id", id);
        bundle.putString(Constanta.FLAG_MUTASI, flagMutasi);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        ListTokoMutasiBarangFragment listTokoMutasiBarangFragment = new ListTokoMutasiBarangFragment();
        listTokoMutasiBarangFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out).addToBackStack(null);
        ft.replace(R.id.layoutKatalog, listTokoMutasiBarangFragment);
        ft.commit();
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
//        HomeFragment homeFragment = new HomeFragment();
//        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
//        ft.replace(R.id.layoutKatalog, homeFragment);
//        ft.commit();
        int count = Objects.requireNonNull(getFragmentManager()).getBackStackEntryCount();
        if (count != 0) {
            Objects.requireNonNull(getFragmentManager()).popBackStack();
        }
    }
}
