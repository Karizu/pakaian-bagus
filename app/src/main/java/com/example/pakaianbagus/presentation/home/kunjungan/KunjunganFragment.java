package com.example.pakaianbagus.presentation.home.kunjungan;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.api.HomeHelper;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.expenditures.Expenditures;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.inputpenjualan.InputPenjualan;
import com.example.pakaianbagus.presentation.home.kunjungan.adapter.KunjunganAdapter;
import com.example.pakaianbagus.models.KunjunganModel;
import com.example.pakaianbagus.presentation.home.kunjungan.tambahkunjungan.TambahKunjunganFragment;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.DateUtils;
import com.example.pakaianbagus.util.dialog.Loading;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KunjunganFragment extends Fragment {


    private List<KunjunganModel> kunjunganModels;
    private String user_id,groupId,roleId;
    private String type = null;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.toolbar_title)
    TextView tvTitle;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.kunjungan_fragment, container, false);
        ButterKnife.bind(this, rootView);

        kunjunganModels = new ArrayList<>();

        try {
            user_id = Session.get(Constanta.USER_ID).getValue();
            groupId = Session.get(Constanta.GROUP_ID).getValue();
            roleId = Session.get(Constanta.ROLE_ID).getValue();
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }

        if ((getArguments() != null ? getArguments().getString(Constanta.KUNJUNGAN_SAYA) : null)!= null){
            tvTitle.setText(getArguments().getString(Constanta.KUNJUNGAN_SAYA));
            type = Constanta.KUNJUNGAN_SAYA;
            getListMyExpenditures();
            swipeRefresh.setOnRefreshListener(() -> {
                kunjunganModels.clear();
                getListMyExpenditures();
            });
        } else if ((getArguments() != null ? getArguments().getString(Constanta.KUNJUNGAN_KOORDINATOR) : null)!= null){
            tvTitle.setText(getArguments().getString(Constanta.KUNJUNGAN_KOORDINATOR));
            type = Constanta.KUNJUNGAN_KOORDINATOR;
            getListExpenditures();
            swipeRefresh.setOnRefreshListener(() -> {
                kunjunganModels.clear();
                getListExpenditures();
            });
        }


        return rootView;
    }

    private void getListMyExpenditures(){
        swipeRefresh.setRefreshing(true);
        HomeHelper.getListMyExpenditures(user_id, new Callback<ApiResponse<List<Expenditures>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<ApiResponse<List<Expenditures>>> call, Response<ApiResponse<List<Expenditures>>> response) {
                swipeRefresh.setRefreshing(false);
                try {
                    List<Expenditures> res = Objects.requireNonNull(response.body()).getData();

                    if (res.size() < 1){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < res.size(); i++){
                        Expenditures expenditures = res.get(i);
                        kunjunganModels.add(new KunjunganModel(
                                expenditures.getId(),
                                expenditures.getGroup().getName(),
                                expenditures.getDate()));
                    }
                    KunjunganAdapter kunjunganAdapter = new KunjunganAdapter(kunjunganModels, getContext(), KunjunganFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(kunjunganAdapter);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Expenditures>>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    private void getListMyExpendituresByDate(String date){
        swipeRefresh.setRefreshing(true);
        HomeHelper.getListMyExpendituresByDate(date, user_id, new Callback<ApiResponse<List<Expenditures>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<ApiResponse<List<Expenditures>>> call, Response<ApiResponse<List<Expenditures>>> response) {
                swipeRefresh.setRefreshing(false);
                try {
                    List<Expenditures> res = Objects.requireNonNull(response.body()).getData();

                    if (res.size() < 1){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < res.size(); i++){
                        Expenditures expenditures = res.get(i);
                        kunjunganModels.add(new KunjunganModel(
                                expenditures.getId(),
                                expenditures.getGroup().getName(),
                                expenditures.getDate()));
                    }
                    KunjunganAdapter kunjunganAdapter = new KunjunganAdapter(kunjunganModels, getContext(), KunjunganFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(kunjunganAdapter);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Expenditures>>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    private void getListExpenditures(){
        swipeRefresh.setRefreshing(true);
        HomeHelper.getListExpenditures(roleId, groupId, new Callback<ApiResponse<List<Expenditures>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<ApiResponse<List<Expenditures>>> call, Response<ApiResponse<List<Expenditures>>> response) {
                swipeRefresh.setRefreshing(false);
                try {
                    List<Expenditures> res = Objects.requireNonNull(response.body()).getData();

                    if (res.size() < 1){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < res.size(); i++){
                        Expenditures expenditures = res.get(i);
                        kunjunganModels.add(new KunjunganModel(
                                expenditures.getId(),
                                expenditures.getGroup().getName(),
                                expenditures.getDate()));
                    }
                    KunjunganAdapter kunjunganAdapter = new KunjunganAdapter(kunjunganModels, getContext(), KunjunganFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(kunjunganAdapter);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Expenditures>>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    private void getListExpendituresByDate(String date){
        swipeRefresh.setRefreshing(true);
        HomeHelper.getListExpendituresByDate(date, roleId, groupId, new Callback<ApiResponse<List<Expenditures>>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<ApiResponse<List<Expenditures>>> call, Response<ApiResponse<List<Expenditures>>> response) {
                swipeRefresh.setRefreshing(false);
                try {
                    List<Expenditures> res = Objects.requireNonNull(response.body()).getData();

                    if (res.size() < 1){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < res.size(); i++){
                        Expenditures expenditures = res.get(i);
                        kunjunganModels.add(new KunjunganModel(
                                expenditures.getId(),
                                expenditures.getGroup().getName(),
                                expenditures.getDate()));
                    }
                    KunjunganAdapter kunjunganAdapter = new KunjunganAdapter(kunjunganModels, getContext(), KunjunganFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayout.VERTICAL,
                            false));
                    recyclerView.setAdapter(kunjunganAdapter);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Expenditures>>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    public void onClickItem(String id){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        DetailKunjunganFragment detailKunjunganFragment = new DetailKunjunganFragment();
        detailKunjunganFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, detailKunjunganFragment).addToBackStack("detailKunjunganFragment");
        ft.commit();
    }

    @OnClick(R.id.toolbar_calendar)
    void onClickToolbarCalendar(){
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    kunjunganModels.clear();
                    if (tvTitle.getText().toString().equals("KUNJUNGAN SAYA")) {
                        getListMyExpendituresByDate(new DateUtils().formatDateToString(newDate.getTime(), "yyyy-MM-dd"));
                    } else if (tvTitle.getText().toString().equals("KUNJUNGAN KOORDINATOR")) {
                        getListExpendituresByDate(new DateUtils().formatDateToString(newDate.getTime(), "yyyy-MM-dd"));
                    }
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, homeFragment);
        ft.commit();
    }

    @OnClick(R.id.toolbar_tambah_pengeluaran)
    void toolbarTambahPengeluaran(){
        Bundle bundle = new Bundle();
        if (type.equals(Constanta.KUNJUNGAN_SAYA)){
            bundle.putString(Constanta.KUNJUNGAN_SAYA, "KUNJUNGAN SAYA");
        } else {
            bundle.putString(Constanta.KUNJUNGAN_KOORDINATOR, "KUNJUNGAN KOORDINATOR");
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        TambahKunjunganFragment tambahKunjunganFragment = new TambahKunjunganFragment();
        tambahKunjunganFragment.setArguments(bundle);
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayoutKunjungan, tambahKunjunganFragment).addToBackStack("tambahKunjunganFragment");
        ft.commit();
    }
}
