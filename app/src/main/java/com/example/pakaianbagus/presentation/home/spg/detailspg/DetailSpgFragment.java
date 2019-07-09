package com.example.pakaianbagus.presentation.home.spg.detailspg;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.home.spg.SpgFragment;
import com.example.pakaianbagus.presentation.home.spg.detailspg.adapter.DetailSpgAdapter;
import com.example.pakaianbagus.presentation.home.spg.detailspg.model.DetailSpgModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailSpgFragment extends Fragment {

    Dialog dialog;
    private List<DetailSpgModel> detailSpgModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public DetailSpgFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_spg_fragment, container, false);
        ButterKnife.bind(this, rootView);

        TextView toolbarTitle = rootView.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("DARNADI SANTOSO");

        detailSpgModels = new ArrayList<>();
        setRecylerView();

        return rootView;
    }

    private void setRecylerView(){
        for (int i = 0; i < 20; i++){
            detailSpgModels.add(new DetailSpgModel("2019 June 25", "07:30", "17:30"));
        }

        DetailSpgAdapter detailSpgAdapter = new DetailSpgAdapter(detailSpgModels, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false));
        recyclerView.setAdapter(detailSpgAdapter);
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        SpgFragment spgFragment = new SpgFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutDetailSpg, spgFragment);
        ft.commit();
    }

    private void settingFilterSpinner() {
        // Spinner Drop down elements
        List<String> filter = new ArrayList<>();
        filter.add("1 BULAN");
        filter.add("2 BULAN");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.spinner_item, filter);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_stock_dropdown_item);

        // attaching data adapter to spinner
//        spgSpinner.setAdapter(dataAdapter);
    }

    @OnClick(R.id.toolbar_mutasi)
    public void onClickToolbarMutasi(){
        showDialog();
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_mutasi_spg);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
