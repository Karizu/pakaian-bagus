package com.example.pakaianbagus.presentation.katalog;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.KatalogTokoModel;
import com.example.pakaianbagus.presentation.katalog.adapter.KatalogTokoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KatalogFragment extends Fragment {

    Dialog dialog;
    View rootView;
    private List<KatalogTokoModel> katalogTokoModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public KatalogFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.katalog_fragment, container, false);
        ButterKnife.bind(this, rootView);

        ImageView imgBack = rootView.findViewById(R.id.toolbar_back);
        imgBack.setVisibility(View.GONE);

        katalogTokoModels = new ArrayList<>();

        setRecylerView();

        return rootView;
    }

    private void setRecylerView(){
        for (int i = 0; i < 20; i++){
            katalogTokoModels.add(new KatalogTokoModel("Toko Adil Makmur", "Jl. Asia Afrika"));
        }

        KatalogTokoAdapter katalogTokoAdapter = new KatalogTokoAdapter(katalogTokoModels, getContext(), KatalogFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false));
        recyclerView.setAdapter(katalogTokoAdapter);

    }

    public void onClickItem(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        KatalogListBarang katalogListBarang = new KatalogListBarang();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.layoutKatalog, katalogListBarang);
        ft.commit();
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.toolbar_search)
    public void toolbarSearch(){
        showDialog();
        TextView toolbar = dialog.findViewById(R.id.tvToolbar);
        toolbar.setText("SEARCH KATALOG");
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void showDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(R.layout.dialog_search_stockopname);
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
