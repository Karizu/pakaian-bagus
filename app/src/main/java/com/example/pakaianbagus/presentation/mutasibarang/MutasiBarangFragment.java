package com.example.pakaianbagus.presentation.mutasibarang;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.MutasiHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.api.mutation.Mutation;
import com.example.pakaianbagus.presentation.mutasibarang.adapter.MutasiBarangAdapter;
import com.example.pakaianbagus.presentation.mutasibarang.tambahmutasi.TambahMutasiFragment;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.DateUtils;
import com.example.pakaianbagus.util.SessionManagement;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.reztrofit.ErrorResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class MutasiBarangFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tvNoData)
    TextView tvNoData;

    private List<Mutation> mutationList = new ArrayList<>();
    private MutasiBarangAdapter mutasiSpgAdapter;
    private String idBrand, idToko, flagMutasi;

    public MutasiBarangFragment() {

    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.mutasi_barang_fragment, container, false);
        ButterKnife.bind(this, rootView);

        try {
            idToko = Objects.requireNonNull(getArguments()).getString("store_id");
            idBrand = Objects.requireNonNull(getArguments()).getString("brand_id");
            flagMutasi = Objects.requireNonNull(getArguments()).getString(Constanta.FLAG_MUTASI);
            Log.d("TAG", Objects.requireNonNull(flagMutasi));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (Session.get(Constanta.ROLE_ID).getValue().equals(SessionManagement.ROLE_SPG)) {
                idBrand = Session.get(Constanta.BRAND).getValue();
                idToko = Session.get(Constanta.TOKO).getValue();
            }
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }


        mutasiSpgAdapter = new MutasiBarangAdapter(mutationList, getContext(), MutasiBarangFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL, false));
        recyclerView.setAdapter(mutasiSpgAdapter);

        if (flagMutasi.equals(Constanta.BARANG_MASUK)) {
            getDataForIncome();
            swipeRefresh.setOnRefreshListener(this::getDataForIncome);
        } else {
            getDataForMutation();
            swipeRefresh.setOnRefreshListener(this::getDataForMutation);
        }

        return rootView;
    }

    private void getDataForIncomeByDate(String date) {
        swipeRefresh.setRefreshing(true);
        List<Integer> status = new ArrayList<>();
        status.clear();
        status.add(4);
        MutasiHelper.getListMutationByDate(status, idBrand, idToko, date, new RestCallback<ApiResponse<List<Mutation>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Mutation>> body) {
                swipeRefresh.setRefreshing(false);
                try {
                    mutationList.clear();
                    mutationList.addAll(body.getData());
                    mutasiSpgAdapter.notifyDataSetChanged();
                    if (body.getData().size() < 1) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void getDataForIncome() {
        swipeRefresh.setRefreshing(true);
        List<Integer> status = new ArrayList<>();
        status.clear();
        status.add(4);
        MutasiHelper.getListMutation(status, idBrand, idToko, new RestCallback<ApiResponse<List<Mutation>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Mutation>> body) {
                swipeRefresh.setRefreshing(false);
                try {
                    mutationList.clear();
                    mutationList.addAll(body.getData());
                    mutasiSpgAdapter.notifyDataSetChanged();
                    if (body.getData().size() < 1) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void getDataForMutationByDate(String date) {
        swipeRefresh.setRefreshing(true);
        List<Integer> status = new ArrayList<>();
        status.clear();
        status.add(3);
        status.add(5);
        MutasiHelper.getListMutationByDate(status, idBrand, idToko, date, new RestCallback<ApiResponse<List<Mutation>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Mutation>> body) {
                swipeRefresh.setRefreshing(false);
                try {
                    mutationList.clear();
                    mutationList.addAll(body.getData());
                    mutasiSpgAdapter.notifyDataSetChanged();
                    if (body.getData().size() < 1) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    private void getDataForMutation() {
        swipeRefresh.setRefreshing(true);
        List<Integer> status = new ArrayList<>();
        status.clear();
        status.add(3);
        status.add(5);
        MutasiHelper.getListMutation(status, idBrand, idToko, new RestCallback<ApiResponse<List<Mutation>>>() {
            @Override
            public void onSuccess(Headers headers, ApiResponse<List<Mutation>> body) {
                swipeRefresh.setRefreshing(false);
                try {
                    mutationList.clear();
                    mutationList.addAll(body.getData());
                    mutasiSpgAdapter.notifyDataSetChanged();
                    if (body.getData().size() < 1) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(ErrorResponse error) {
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onCanceled() {

            }
        });
    }

    public void onClickItem(Mutation mutation) {
        Bundle bundle = new Bundle();
        bundle.putInt("mutationId", mutation.getId());
        bundle.putInt("status", mutation.getStatus());
        bundle.putString("store_id", idToko);
        bundle.putString("brand_id", idBrand);
        bundle.putString(Constanta.FLAG_MUTASI, flagMutasi);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        Fragment fragment = new MutasiDetail();
        fragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out).addToBackStack(null);
        ft.replace(R.id.baseLayoutMutasi, fragment);
        ft.commit();
    }

    @OnClick(R.id.toolbar_filter)
    void onClickFilter() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    if (flagMutasi.equals(Constanta.BARANG_MASUK)) {
                        getDataForIncomeByDate(new DateUtils().formatDateToString(newDate.getTime(), "yyyy-MM-dd"));
                    } else {
                        getDataForMutationByDate(new DateUtils().formatDateToString(newDate.getTime(), "yyyy-MM-dd"));
                    }
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @OnClick(R.id.toolbar_add)
    void onClickAddMutasi() {
        Bundle bundle = new Bundle();
        bundle.putString("store_id", idToko);
        bundle.putString("brand_id", idBrand);
        bundle.putString(Constanta.FLAG_MUTASI, flagMutasi);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        Fragment fragment = new TambahMutasiFragment();
        fragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out).addToBackStack(null);
        ft.replace(R.id.baseLayoutMutasi, fragment);
        ft.commit();
    }
}
