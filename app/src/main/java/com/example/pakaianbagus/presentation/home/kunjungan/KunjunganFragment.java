package com.example.pakaianbagus.presentation.home.kunjungan;

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
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.kunjungan.adapter.KunjunganAdapter;
import com.example.pakaianbagus.presentation.home.kunjungan.model.KunjunganModel;
import com.example.pakaianbagus.presentation.home.kunjungan.tambahkunjungan.TambahKunjunganFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KunjunganFragment extends Fragment {


    private List<KunjunganModel> kunjunganModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public KunjunganFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.kunjungan_fragment, container, false);
        ButterKnife.bind(this, rootView);

        TextView tvTitle = rootView.findViewById(R.id.toolbar_title);
        if ((getArguments() != null ? getArguments().getString("kunjunganSaya") : null)!= null){
            tvTitle.setText(getArguments().getString("kunjunganSaya"));
        }

        kunjunganModels = new ArrayList<>();
        setRecylerView();

        return rootView;
    }

    private void setRecylerView(){
        for (int i = 0; i < 20; i++){
            kunjunganModels.add(new KunjunganModel("Toko Adil Makmur", "08:00 | 29 Juni 2019"));
        }

        KunjunganAdapter kunjunganAdapter = new KunjunganAdapter(kunjunganModels, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false));
        recyclerView.setAdapter(kunjunganAdapter);
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayout, homeFragment);
        ft.commit();
    }

    @OnClick(R.id.toolbar_tambah_pengeluaran)
    public void toolbarTambahPengeluaran(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        TambahKunjunganFragment tambahKunjunganFragment = new TambahKunjunganFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayoutKunjungan, tambahKunjunganFragment);
        ft.commit();
    }
}
