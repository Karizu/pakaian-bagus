package com.example.pakaianbagus.presentation.barangmasuk;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.Toko;
import com.example.pakaianbagus.presentation.barangmasuk.adapter.ListTokoBMAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListTokoBMFragment extends Fragment {
    private List<Toko> tokos;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public ListTokoBMFragment() {

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
        ImageView toolbarBack = rootView.findViewById(R.id.toolbar_back);
        toolbarBack.setVisibility(View.GONE);
        TextView tvList = rootView.findViewById(R.id.tvList);
        tvList.setText("LIST TOKO");

        tokos = new ArrayList<>();
        setRecylerView();

        return rootView;
    }

    private void setRecylerView(){
        for (int i = 0; i < 20; i++){
            tokos.add(new Toko("Toko Adil Makmur", "08.00 | 29 Juni 2019"));
        }

        ListTokoBMAdapter listTokoBMAdapter = new ListTokoBMAdapter(tokos, getContext(), ListTokoBMFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false));
        recyclerView.setAdapter(listTokoBMAdapter);
    }

    public void layoutListBarangMasuk(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        BarangMasukFragment barangMasukFragment = new BarangMasukFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayoutBM, barangMasukFragment);
        ft.commit();
    }
}
