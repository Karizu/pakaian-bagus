package com.example.pakaianbagus.presentation.mutasibarang;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.home.kunjungan.tambahkunjungan.TambahKunjunganFragment;
import com.example.pakaianbagus.presentation.mutasibarang.tambahmutasi.TambahMutasiFragment;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MutasiBarangFragment extends Fragment {

    View rootView;
    Dialog dialog;

    public MutasiBarangFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.mutasi_barang_fragment, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.toolbar_add)
    public void onCLickBtnAddMutasiBrg(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        TambahMutasiFragment tambahMutasiFragment = new TambahMutasiFragment();
        ft.replace(R.id.baseLayoutMutasi, tambahMutasiFragment);
        ft.commit();
    }
}
