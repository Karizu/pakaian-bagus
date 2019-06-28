package com.example.pakaianbagus.presentation.home.inventaris;

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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.inventaris.adapter.InventarisAdapter;
import com.example.pakaianbagus.presentation.home.inventaris.model.InventarisModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InventarisFragment extends Fragment {

    View rootView;
    Dialog dialog;
    private List<InventarisModel> inventarisModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    public InventarisFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.inventaris_fragment, container, false);
        ButterKnife.bind(this, rootView);

        inventarisModels = new ArrayList<>();
        setRecylerView();

        return rootView;
    }

    private void setRecylerView(){
        for (int i = 0; i < 20; i++){
            inventarisModels.add(new InventarisModel("Sapu", "2 pcs"));
        }

        InventarisAdapter inventarisAdapter = new InventarisAdapter(inventarisModels, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false));
        recyclerView.setAdapter(inventarisAdapter);
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayout, homeFragment);
        ft.commit();
    }
}

