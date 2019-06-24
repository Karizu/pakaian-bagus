package com.example.pakaianbagus.presentation.penjualan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pakaianbagus.R;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PenjualanKompetitorFragment extends Fragment {

    View rootView;

    public PenjualanKompetitorFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.penjualan_kompetitor_fragment, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.tabSalesReport)
    public void tabSalesReport() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        InputHarianFragment inputHarianFragment = new InputHarianFragment();
        ft.replace(R.id.baseLayoutInputHarianPenjualan, inputHarianFragment);
        ft.commit();
    }

}
