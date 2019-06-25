package com.example.pakaianbagus.presentation.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.News;
import com.example.pakaianbagus.presentation.home.inventaris.InventarisFragment;
import com.example.pakaianbagus.presentation.home.kunjungan.KunjunganFragment;
import com.example.pakaianbagus.presentation.home.notification.NotificationFragment;
import com.example.pakaianbagus.presentation.home.spg.SpgFragment;
import com.example.pakaianbagus.presentation.penjualan.InputHarianFragment;
import com.example.pakaianbagus.presentation.penjualan.ScanBarcodeActivity;
import com.example.pakaianbagus.presentation.stockopname.StockOpnameFragment;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {

    @BindView(R.id.carousel)
    CarouselView customCarouselView;
    Dialog dialog;
    List<News> newsPager = new ArrayList<>();
    View rootView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, rootView);

//        hideItemForSPGScreen();
        TextView toolbarTitle = rootView.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Hallo Rizal");

        newsPager.add(new News("Title", "Lorem Ipsum Lorem Ipsum", "https://d1csarkz8obe9u.cloudfront.net/posterpreviews/purple-ramadan-charity-event-invitation-banner-design-template-c8a1a4d8e5747a5a4f75d56b7974d233_screen.jpg?ts=1556628102"));
        initSlider(rootView);
        return rootView;
    }

    private void initSlider(View v) {
        //customCarouselView.setPageCount(sampleTitles.length);
        customCarouselView.setViewListener(viewListener);
        customCarouselView.setPageCount(newsPager.size());
        customCarouselView.setSlideInterval(4000);
    }

    // To set custom views
    ViewListener viewListener = position -> {
        @SuppressLint("InflateParams") View customView = getLayoutInflater().inflate(R.layout.item_carousel_banner, null);
        TextView tvLabelCarousel = customView.findViewById(R.id.tv_label_carousel);
        ImageView ivCarousel = customView.findViewById(R.id.iv_carousel);

        Glide.with(Objects.requireNonNull(getActivity()))
                .load(newsPager.get(position).getBanner())
                .into(ivCarousel);
        tvLabelCarousel.setText(newsPager.get(position).getTitle());

//            ivCarousel.setOnClickListener(new NewsClickListener(newsPager.get(position)));
        return customView;
    };

    private void hideItemForSPGScreen() {
        LinearLayout layout = rootView.findViewById(R.id.layoutHeaderButton);
        layout.setVisibility(View.GONE);

        Button button = rootView.findViewById(R.id.btnTambah);
        button.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnStock)
    public void onClickBtnStock (){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        StockOpnameFragment stockFragment = new StockOpnameFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayout, stockFragment);
        ft.commit();
    }

    @OnClick(R.id.btnPenjualan)
    public void onClickBtnPenjualan (){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        InputHarianFragment inputFragment = new InputHarianFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayout, inputFragment);
        ft.commit();
    }

    @OnClick(R.id.btnCheck)
    public void onClickBtnCheck (){
        startActivity(new Intent(getActivity(), ScanBarcodeActivity.class));
    }

    @OnClick(R.id.btnSPG)
    public void onClickBtnSPG (){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        SpgFragment spgFragment = new SpgFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayout, spgFragment);
        ft.commit();
    }

    @OnClick(R.id.btnKunjungan)
    public void onClickBtnKunjungan (){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        KunjunganFragment kunjunganFragment = new KunjunganFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayout, kunjunganFragment);
        ft.commit();
    }

    @OnClick(R.id.btnInventaris)
    public void onClickBtnInventaris (){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        InventarisFragment inventarisFragment = new InventarisFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayout, inventarisFragment);
        ft.commit();
    }

    @OnClick(R.id.btnTambah)
    public void btnTambah(){
//        showDialog(R.layout.dialog_tambah_barang);
//        ImageView imgClose = dialog.findViewById(R.id.imgClose);
//        imgClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void showDialog(int layout) {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        //set content
        dialog.setContentView(layout);
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

    @OnClick(R.id.toolbar_logout)
    public void onClickToolbar(){
        Objects.requireNonNull(getActivity()).finishAffinity();
        getActivity().finish();
    }

    @OnClick(R.id.toolbar_notif)
    public void onClickToolbarNotif(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        NotificationFragment notifFragment = new NotificationFragment();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.baseLayout, notifFragment);
        ft.commit();
    }

}
