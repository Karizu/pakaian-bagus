package com.example.pakaianbagus.presentation.penjualan.sales;

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
import com.example.pakaianbagus.api.InputHelper;
import com.example.pakaianbagus.api.KatalogHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.models.TokoResponse;
import com.example.pakaianbagus.models.transaction.Member;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.penjualan.InputHarianFragment;
import com.example.pakaianbagus.presentation.penjualan.PenjualanListTokoFragment;
import com.example.pakaianbagus.presentation.penjualan.adapter.PenjualanTokoAdapter;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.IOnBackPressed;
import com.example.pakaianbagus.util.SessionManagement;
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
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenjualanListTokoSalesFragment extends Fragment implements IOnBackPressed {


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

    public PenjualanListTokoSalesFragment() {
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

        try {
            if (Session.get("RoleId").getValue().equals(SessionManagement.ROLE_SALES)) {
                getMember();
                swipeRefresh.setOnRefreshListener(() -> {
                    katalogTokoModels.clear();
                    getMember();
                });
            } else {
                getListToko();
                swipeRefresh.setOnRefreshListener(() -> {
                    katalogTokoModels.clear();
                    getListToko();
                });
            }
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy MMMM dd");
        String formattedDate = df.format(c);
        tvDate.setText(formattedDate);
    }

    private void getMember(){
        swipeRefresh.setRefreshing(true);
        InputHelper.getMember(new RestCallback<ApiResponse<List<Member>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Member>> body) {
                swipeRefresh.setRefreshing(false);
                try {
                    if (Objects.requireNonNull(body).getData() != null) {
                        List<Member> memberResponse = body.getData();

                        if (memberResponse.size() < 1) {
                            tvNoData.setVisibility(View.VISIBLE);
                        } else {
                            tvNoData.setVisibility(View.GONE);
                        }

                        for (int i = 0; i < memberResponse.size(); i++) {
                            Member dataToko = memberResponse.get(i);
                            katalogTokoModels.add(new KatalogTokoModel(dataToko.getId(), dataToko.getFullname(), dataToko.getAddress(), dataToko.getLimit(), dataToko.getAccountsReceivable()));
                        }

                        PenjualanTokoSalesAdapter penjualanTokoSalesAdapter = new PenjualanTokoSalesAdapter(katalogTokoModels, getContext(), PenjualanListTokoSalesFragment.this);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                                LinearLayout.VERTICAL,
                                false));
                        recyclerView.setAdapter(penjualanTokoSalesAdapter);
                    }
                } catch (Exception e){
                    e.printStackTrace();
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

    public void getListToko() {
        swipeRefresh.setRefreshing(true);
        KatalogHelper.getListToko(new Constanta().PLACE_TYPE_SHOP, new Callback<ApiResponse<List<TokoResponse>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<TokoResponse>>> call, @NonNull Response<ApiResponse<List<TokoResponse>>> response) {
                swipeRefresh.setRefreshing(false);
                if (Objects.requireNonNull(response.body()).getData() != null) {
                    List<TokoResponse> tokoResponse = response.body().getData();

                    if (tokoResponse.size() < 1) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < tokoResponse.size(); i++) {
                        TokoResponse dataToko = tokoResponse.get(i);
                        katalogTokoModels.add(new KatalogTokoModel(dataToko.getId(), dataToko.getName(), dataToko.getType()));
                    }
                    PenjualanTokoSalesAdapter penjualanTokoSalesAdapter = new PenjualanTokoSalesAdapter(katalogTokoModels, getContext(), PenjualanListTokoSalesFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(penjualanTokoSalesAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<TokoResponse>>> call, @NonNull Throwable t) {
                swipeRefresh.setRefreshing(false);
                Log.d("List Toko Katalog", t.getMessage());

            }
        });
    }

    public void onClickItem(String id, String limit, String totalHutang) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("limit", limit);
        bundle.putString("totalHutang", totalHutang);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        InputHarianSalesFragment inputHarianFragment = new InputHarianSalesFragment();
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
