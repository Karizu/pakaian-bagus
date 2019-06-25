package com.example.pakaianbagus.presentation.barangmasuk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.barangmasuk.detailbm.DetailBarangMasuk;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BarangMasukFragment extends Fragment {

    public BarangMasukFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.barang_masuk_fragment, container, false);
        ButterKnife.bind(this, rootView);

        TextView toolbarTitle = rootView.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("BARANG MASUK");
        ImageView toolbarHistory = rootView.findViewById(R.id.toolbar_history);
        toolbarHistory.setVisibility(View.GONE);

        return rootView;
    }

    @OnClick(R.id.layoutListBarangMasuk)
    public void layoutListBarangMasuk(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        DetailBarangMasuk detailBMFragment = new DetailBarangMasuk();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayoutBM, detailBMFragment);
        ft.commit();
    }

}
