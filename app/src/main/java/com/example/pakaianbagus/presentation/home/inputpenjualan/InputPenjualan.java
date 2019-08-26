package com.example.pakaianbagus.presentation.home.inputpenjualan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.penjualan.ScanBarcodeActivity;

import butterknife.ButterKnife;

public class InputPenjualan extends Fragment {

    View rootView;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.input_penjualan_fragment, container, false);
//        ButterKnife.bind(this, rootView);

        Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
        intent.putExtra("mode", "PENJUALAN");
        startActivity(intent);

        return rootView;
    }


//    @OnClick(R.id.toolbar_back)
//    public void toolbarBack() {
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
//        HomeFragment homeFragment = new HomeFragment();
//        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
//        ft.replace(R.id.baseLayout, homeFragment);
//        ft.commit();
//    }

}
