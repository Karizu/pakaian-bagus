package com.example.pakaianbagus.presentation.barangmasuk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pakaianbagus.R;

import butterknife.ButterKnife;

public class BarangMasukFragment extends Fragment {

    public BarangMasukFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.barang_masuk_fragment, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }
}
