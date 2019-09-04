package com.example.pakaianbagus.presentation.home.inputpenjualan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.penjualan.ScanBarcodeActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputPenjualan extends Fragment {

    @BindView(R.id.tabSalesReport)
    TextView tabSalesReport;
    @BindView(R.id.tabPenjualan)
    TextView tabPenjualan;
    @BindView(R.id.btnBarcode)
    ImageView btnBarcode;
    @BindView(R.id.btnAdd)
    ImageView btnAdd;
    @BindView(R.id.rvSales)
    RecyclerView rvSales;
    @BindView(R.id.rvPenjualan)
    RecyclerView rvPenjualan;

    View rootView;
    String date;
    int pager;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.input_penjualan_fragment, container, false);
        ButterKnife.bind(this, rootView);

        date = Objects.requireNonNull(getArguments()).getString("date");

        defaultScreen();

        getData();

        return rootView;
    }

    private void getData() {
    }

    @OnClick(R.id.tabSalesReport)
    public void onSalesReportSelected() {
        pager = 0;
        rvSales.setVisibility(View.VISIBLE);
        rvPenjualan.setVisibility(View.GONE);
        tabSalesReport.setTextColor(getResources().getColor(R.color.DarkSlateBlue));
        tabPenjualan.setTextColor(getResources().getColor(R.color.Background));
        btnBarcode.setVisibility(View.VISIBLE);
        btnAdd.setVisibility(View.GONE);
    }

    @OnClick(R.id.tabPenjualan)
    public void onPenjualanKompetitorSelected() {
        pager = 1;
        rvSales.setVisibility(View.GONE);
        rvPenjualan.setVisibility(View.VISIBLE);
        tabSalesReport.setTextColor(getResources().getColor(R.color.Background));
        tabPenjualan.setTextColor(getResources().getColor(R.color.DarkSlateBlue));
        btnBarcode.setVisibility(View.GONE);
        btnAdd.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnSubmit)
    public void btnSubmitClicked() {
        if (pager == 0) {
            Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
        } else if (pager == 1) {
            Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayout, homeFragment);
        ft.commit();
    }

    private void defaultScreen() {
        pager = 0;
        rvSales.setVisibility(View.VISIBLE);
        rvPenjualan.setVisibility(View.GONE);
        tabSalesReport.setTextColor(getResources().getColor(R.color.DarkSlateBlue));
        tabPenjualan.setTextColor(getResources().getColor(R.color.Background));
        btnBarcode.setVisibility(View.VISIBLE);
        btnAdd.setVisibility(View.GONE);
    }

}
