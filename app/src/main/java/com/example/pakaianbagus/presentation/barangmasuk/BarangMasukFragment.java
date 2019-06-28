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
import com.example.pakaianbagus.presentation.barangmasuk.adapter.BarangMasukAdapter;
import com.example.pakaianbagus.presentation.barangmasuk.detailbm.DetailBarangMasuk;
import com.example.pakaianbagus.presentation.barangmasuk.model.BarangMasukModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BarangMasukFragment extends Fragment {

    private List<BarangMasukModel> barangMasukModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

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

        barangMasukModels = new ArrayList<>();
        setRecylerView();

        return rootView;
    }

    private void setRecylerView(){
        for (int i = 0; i < 20; i++){
            barangMasukModels.add(new BarangMasukModel("BRG-A1-23", "1pcs", "1"));
        }

        BarangMasukAdapter barangMasukAdapter = new BarangMasukAdapter(barangMasukModels, getContext(), BarangMasukFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false));
        recyclerView.setAdapter(barangMasukAdapter);
    }

    public void layoutListBarangMasuk(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        DetailBarangMasuk detailBMFragment = new DetailBarangMasuk();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayoutBM, detailBMFragment);
        ft.commit();
    }

}
