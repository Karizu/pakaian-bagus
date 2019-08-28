package com.example.pakaianbagus.presentation.home.spg;

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

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.SpgModel;
import com.example.pakaianbagus.presentation.home.spg.adapter.SpgMutasiAdapter;
import com.example.pakaianbagus.presentation.home.spg.detailspg.DetailSpgFragment;
import com.example.pakaianbagus.util.IOnBackPressed;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpgListMutasiFragment extends Fragment implements IOnBackPressed {
    private List<SpgModel> spgModels;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.spg_list_mutasi_fragment, container, false);
        ButterKnife.bind(this, rootView);

//        String id = Objects.requireNonNull(getArguments()).getString("id");

        spgModels = new ArrayList<>();

        setRecylerView();

        return rootView;
    }

    @Override
    public boolean onBackPressed() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        SpgListFragment spgListFragment = new SpgListFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayout, spgListFragment);
        ft.commit();

        return false;
    }

    private void setRecylerView() {
        for (int i = 0; i < 20; i++) {
            spgModels.add(new SpgModel("Darnadi Santoso", "Toko Adiguna"));
        }

        SpgMutasiAdapter spgMutasiAdapter = new SpgMutasiAdapter(spgModels, getContext(), SpgListMutasiFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayout.VERTICAL,
                false));
        recyclerView.setAdapter(spgMutasiAdapter);
    }

    public void onClickLayoutListSPG() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        DetailSpgFragment detailSpgFragment = new DetailSpgFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutSpg, detailSpgFragment);
        ft.commit();
    }
}
