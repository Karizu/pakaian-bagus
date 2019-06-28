package com.example.pakaianbagus.presentation.mutasibarang;

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
import com.example.pakaianbagus.presentation.mutasibarang.adapter.MutasiBarangAdapter;
import com.example.pakaianbagus.models.MutasiBarangModel;
import com.example.pakaianbagus.presentation.mutasibarang.tambahmutasi.TambahMutasiFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MutasiBarangFragment extends Fragment {

    View rootView;
    private List<MutasiBarangModel> mbModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public MutasiBarangFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.mutasi_barang_fragment, container, false);
        ButterKnife.bind(this, rootView);

        mbModels = new ArrayList<>();

        setRecylerView();

        return rootView;
    }

    private void setRecylerView(){
        for (int i = 0; i < 20; i++){
            mbModels.add(new MutasiBarangModel("Toko Adil Makmur", "08:00 | 29 Juni 2019"));
        }

        MutasiBarangAdapter mbAdapter = new MutasiBarangAdapter(mbModels, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false));
        recyclerView.setAdapter(mbAdapter);
    }

    @OnClick(R.id.toolbar_add)
    public void onCLickBtnAddMutasiBrg(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        TambahMutasiFragment tambahMutasiFragment = new TambahMutasiFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayoutMutasi, tambahMutasiFragment);
        ft.commit();
    }
}
