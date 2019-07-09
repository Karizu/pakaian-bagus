package com.example.pakaianbagus.presentation.home.notification;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.home.HomeFragment;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationFragment extends Fragment {

    View rootView;

    public NotificationFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.notification_fragment, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.toolbar_back)
    public void toolbarBack(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.baseLayoutNotif, homeFragment);
        ft.commit();
    }
}
