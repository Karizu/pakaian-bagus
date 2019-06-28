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
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pakaianbagus.MainActivity;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.models.News;
import com.example.pakaianbagus.presentation.home.inventaris.InventarisFragment;
import com.example.pakaianbagus.presentation.home.kunjungan.KunjunganFragment;
import com.example.pakaianbagus.presentation.home.notification.NotificationFragment;
import com.example.pakaianbagus.presentation.home.spg.SpgFragment;
import com.example.pakaianbagus.presentation.penjualan.ScanBarcodeActivity;
import com.example.pakaianbagus.presentation.home.stockopname.StockOpnameFragment;
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
    int Flag = 0;

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
        LinearLayout layout = rootView.findViewById(R.id.layoutHeaderButton2);
        layout.setVisibility(View.GONE);
        Button button = rootView.findViewById(R.id.btnTambah);
        button.setVisibility(View.GONE);
    }

    private void hideItemForKoordinatorScreen() {
        LinearLayout layout2 = rootView.findViewById(R.id.layoutHeaderButton2);
        layout2.setVisibility(View.VISIBLE);
        Button button = rootView.findViewById(R.id.btnTambah);
        button.setVisibility(View.VISIBLE);

        LinearLayout layout1 = rootView.findViewById(R.id.layoutBtnCard);
        layout1.setWeightSum(2f);
        LinearLayout layoutTitle = rootView.findViewById(R.id.layoutHeaderTitle1);
        layoutTitle.setWeightSum(2f);
        CardView cardViewPenjualan = rootView.findViewById(R.id.btnPenjualan);
        cardViewPenjualan.setVisibility(View.GONE);
        TextView tvTitlePenjualan = rootView.findViewById(R.id.tvPenjualan);
        tvTitlePenjualan.setVisibility(View.GONE);
    }

    private void hideItemForManagerScreen() {
        LinearLayout layout2 = rootView.findViewById(R.id.layoutHeaderButton2);
        layout2.setVisibility(View.VISIBLE);
        Button button = rootView.findViewById(R.id.btnTambah);
        button.setVisibility(View.VISIBLE);

        LinearLayout layout1 = rootView.findViewById(R.id.layoutBtnCard);
        layout1.setWeightSum(3f);
        LinearLayout layoutTitle = rootView.findViewById(R.id.layoutHeaderTitle1);
        layoutTitle.setWeightSum(3f);
        CardView cardViewPenjualan = rootView.findViewById(R.id.btnPenjualan);
        cardViewPenjualan.setVisibility(View.VISIBLE);
        TextView tvTitlePenjualan = rootView.findViewById(R.id.tvPenjualan);
        tvTitlePenjualan.setVisibility(View.VISIBLE);
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

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.btnPenjualan)
    public void onClickBtnPenjualan (){
        showDialog(R.layout.dialog_ubah_qty);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(v -> dialog.dismiss());
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("MASUKAN QTY");
        EditText etQty = dialog.findViewById(R.id.etDialogNamaBarang);
        etQty.setHint("Jumlah Qty");
        Button btnOK = dialog.findViewById(R.id.btnDialogTambah);
        btnOK.setText("OK");
        btnOK.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
            startActivity(intent);
        });
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        intent.putExtra("FromHome", "1");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
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
        if (Flag == 3){
            showDialog(R.layout.dialog_pilih_kunjungan);
            Button btnKunjunganKoordinator = dialog.findViewById(R.id.btnKunjunganKoordinator);
            btnKunjunganKoordinator.setOnClickListener(v -> {
                dialog.dismiss();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                KunjunganFragment kunjunganFragment = new KunjunganFragment();
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft.replace(R.id.baseLayout, kunjunganFragment);
                ft.commit();
            });

            Button btnKunjunganSaya = dialog.findViewById(R.id.btnKunjunganSaya);
            btnKunjunganSaya.setOnClickListener(v -> {
                dialog.dismiss();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
                KunjunganFragment kunjunganFragment = new KunjunganFragment();
                Bundle bundle=new Bundle();
                bundle.putString("kunjunganSaya", "KUNJUNGAN SAYA");
                kunjunganFragment.setArguments(bundle);
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft.replace(R.id.baseLayout, kunjunganFragment);
                ft.commit();
            });
        } else {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
            KunjunganFragment kunjunganFragment = new KunjunganFragment();
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.replace(R.id.baseLayout, kunjunganFragment);
            ft.commit();
        }
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
//        Objects.requireNonNull(getActivity()).finishAffinity();
//        getActivity().finish();
        View v1 = rootView.findViewById(R.id.toolbar_logout);
        PopupMenu pm = new PopupMenu(Objects.requireNonNull(getActivity()), v1);
        pm.getMenuInflater().inflate(R.menu.menu_switch_account, pm.getMenu());
        switch (Flag){
            case 1:
                pm.getMenu().findItem(R.id.navigation_spg).setVisible(false);
                pm.getMenu().findItem(R.id.navigation_manager).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_koordinator).setVisible(true);
                break;
            case 2:
                pm.getMenu().findItem(R.id.navigation_spg).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_manager).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_koordinator).setVisible(false);
                break;
            case 3:
                pm.getMenu().findItem(R.id.navigation_spg).setVisible(true);
                pm.getMenu().findItem(R.id.navigation_manager).setVisible(false);
                pm.getMenu().findItem(R.id.navigation_koordinator).setVisible(true);
                break;
        }
        pm.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_spg:
                    Flag = 1;
                    hideItemForSPGScreen();
                    break;
                case R.id.navigation_koordinator:
                    Flag = 2;
                    hideItemForKoordinatorScreen();
                    break;
                case R.id.navigation_manager:
                    Flag = 3;
                    hideItemForManagerScreen();
                    break;
            }
            return true;
        });
        pm.show();
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
