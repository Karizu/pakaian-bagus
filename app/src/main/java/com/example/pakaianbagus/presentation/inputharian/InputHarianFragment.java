package com.example.pakaianbagus.presentation.inputharian;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.home.spg.SpgFragment;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputHarianFragment extends Fragment {

    View rootView;

    public InputHarianFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.input_harian_fragment, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.tabPenjualan)
    public void toolbarBack() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        PenjualanKompetitorFragment penjualanFragment = new PenjualanKompetitorFragment();
        ft.replace(R.id.baseLayoutInputHarian, penjualanFragment);
        ft.commit();
    }

    @OnClick(R.id.btnMore)
    public void btnMore() {
        View v = rootView.findViewById(R.id.btnMore);
        PopupMenu pm = new PopupMenu(Objects.requireNonNull(getActivity()), v);
        pm.getMenuInflater().inflate(R.menu.menu_options, pm.getMenu());
        pm.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_ubah:
                    Toast.makeText(getActivity(), String.valueOf(menuItem.getTitle()), Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });
        pm.show();
    }
}
