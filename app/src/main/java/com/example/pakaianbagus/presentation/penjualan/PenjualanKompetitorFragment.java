package com.example.pakaianbagus.presentation.penjualan;

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
import com.example.pakaianbagus.presentation.penjualan.adapter.PenjualanAdapter;
import com.example.pakaianbagus.models.PenjualanModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PenjualanKompetitorFragment extends Fragment {

    View rootView;
    private List<PenjualanModel> penjualanModels;
    private String id;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvDate)
    TextView tvDate;

    public PenjualanKompetitorFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.penjualan_kompetitor_fragment, container, false);
        ButterKnife.bind(this, rootView);

        id = Objects.requireNonNull(getArguments()).getString("id");

        penjualanModels = new ArrayList<>();
        setRecylerView();
        getCurrentDateChecklist();

        return rootView;
    }

    private void setRecylerView(){
        for (int i = 0; i < 20; i++){
            penjualanModels.add(new PenjualanModel("Celana Jeans", "2 pcs", "250000", "2019 JUNE 29"));
        }

        PenjualanAdapter penjualanAdapter = new PenjualanAdapter(penjualanModels, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false));
        recyclerView.setAdapter(penjualanAdapter);
    }

    private void getCurrentDateChecklist() {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy MMMM dd");
        String formattedDate = df.format(c);
        tvDate.setText(formattedDate);
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        PenjualanListTokoFragment penjualanListTokoFragment = new PenjualanListTokoFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutInputHarianPenjualan, penjualanListTokoFragment);
        ft.commit();
    }

    @OnClick(R.id.tabSalesReport)
    public void tabSalesReport() {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        InputHarianFragment inputHarianFragment = new InputHarianFragment();
        inputHarianFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutInputHarianPenjualan, inputHarianFragment);
        ft.commit();
    }

}
