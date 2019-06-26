package com.example.pakaianbagus.presentation.penjualan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.penjualan.adapter.PenjualanAdapter;
import com.example.pakaianbagus.presentation.penjualan.model.PenjualanModel;
import com.example.pakaianbagus.presentation.penjualan.model.SalesReportModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PenjualanKompetitorFragment extends Fragment {

    View rootView;
    private List<PenjualanModel> penjualanModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    public PenjualanKompetitorFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.penjualan_kompetitor_fragment, container, false);
        ButterKnife.bind(this, rootView);

        penjualanModels = new ArrayList<>();
        setRecylerView();

        return rootView;
    }

    private void setRecylerView(){
        for (int i = 0; i < 20; i++){
            penjualanModels.add(new PenjualanModel("Celana Jeans", "2 pcs", "250000", "2019 JUNE 29"));
        }

        PenjualanAdapter penjualanAdapter = new PenjualanAdapter(penjualanModels, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false));
        recyclerView.setAdapter(penjualanAdapter);
    }

    @OnClick(R.id.tabSalesReport)
    public void tabSalesReport() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        InputHarianFragment inputHarianFragment = new InputHarianFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayoutInputHarianPenjualan, inputHarianFragment);
        ft.commit();
    }

}
