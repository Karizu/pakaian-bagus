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
import com.example.pakaianbagus.api.KatalogHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.models.TokoResponse;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.penjualan.adapter.PenjualanTokoAdapter;
import com.example.pakaianbagus.util.IOnBackPressed;
import com.example.pakaianbagus.util.dialog.Loading;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenjualanListTokoFragment extends Fragment implements IOnBackPressed {


    private List<KatalogTokoModel> katalogTokoModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.toolbar_back)
    ImageView imgBack;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tvNoData)
    TextView tvNoData;

    public PenjualanListTokoFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.spg_fragment, container, false);
        ButterKnife.bind(this, rootView);

        toolbar_title.setText("PENJUALAN");
        imgBack.setVisibility(View.GONE);
        katalogTokoModels = new ArrayList<>();

        getCurrentDateChecklist();
        getListToko();

        swipeRefresh.setOnRefreshListener(() -> {
            katalogTokoModels.clear();
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

    public void getListToko(){
        swipeRefresh.setRefreshing(true);
        KatalogHelper.getListToko(getContext(), new Callback<ApiResponse<List<TokoResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<TokoResponse>>> call, @NonNull Response<ApiResponse<List<TokoResponse>>> response) {
                swipeRefresh.setRefreshing(false);
                if (Objects.requireNonNull(response.body()).getData() != null){
                    List<TokoResponse> tokoResponse = response.body().getData();

                    if (tokoResponse.size() < 1){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < tokoResponse.size(); i++){
                        TokoResponse dataToko = tokoResponse.get(i);
                        if (dataToko.getType().equalsIgnoreCase("S")) {
                            katalogTokoModels.add(new KatalogTokoModel(dataToko.getId(), dataToko.getName(), dataToko.getType()));
                        }
                    }
                    PenjualanTokoAdapter penjualanTokoAdapter = new PenjualanTokoAdapter(katalogTokoModels, getContext(), PenjualanListTokoFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(penjualanTokoAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<TokoResponse>>> call, @NonNull Throwable t) {
                swipeRefresh.setRefreshing(false);
                Log.d("List Toko Katalog", t.getMessage());

            }
        });
    }

    public void onClickItem(String id){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        InputHarianFragment inputHarianFragment = new InputHarianFragment();
        inputHarianFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutSpg, inputHarianFragment);
        ft.commit();
    }

    @Override
    public boolean onBackPressed() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, homeFragment);
        ft.commit();

        return false;
    }
}
