package com.example.pakaianbagus;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.pakaianbagus.presentation.barangmasuk.BarangMasukFragment;
import com.example.pakaianbagus.presentation.barangmasuk.ListTokoBMFragment;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.home.stockopname.StockOpnameFragment;
import com.example.pakaianbagus.presentation.katalog.KatalogBrandFragment;
import com.example.pakaianbagus.presentation.katalog.KatalogListBarang;
import com.example.pakaianbagus.presentation.mutasibarang.MutasiBarangFragment;
import com.example.pakaianbagus.presentation.mutasibarang.MutasiBarangSPG;
import com.example.pakaianbagus.presentation.penjualan.InputHarianFragment;
import com.example.pakaianbagus.presentation.penjualan.PenjualanBrandFragment;
import com.example.pakaianbagus.util.Constanta;
import com.example.pakaianbagus.util.DateUtils;
import com.example.pakaianbagus.util.SessionManagement;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;
import com.rezkyatinnov.kyandroid.session.SessionObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime = 0;
    private BottomNavigationView navigation;
    final Fragment fragmentHome = new HomeFragment();
    final Fragment fragmentStockOpname = new StockOpnameFragment();
    final Fragment fragmentBarangMasuk = new BarangMasukFragment();
    final Fragment fragmentListTokoBM = new ListTokoBMFragment();
    final Fragment fragmentMutasiSPG = new MutasiBarangSPG();
    final Fragment fragmentInputHarian = new InputHarianFragment();
    final Fragment fragmentPenjualanListToko = new PenjualanBrandFragment();
    final Fragment fragmentMutasiBarang = new MutasiBarangFragment();
    final Fragment katalogFragment = new KatalogBrandFragment();
    final Fragment katalogSpgFragment = new KatalogListBarang();
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
    Fragment active = fragmentHome;
    Dialog dialog;
    String roleId, brandId, tokoId;

    @SuppressLint("CommitTransaction")
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                setFragments(fragmentHome, "1");
                active = fragmentHome;
                return true;
            case R.id.navigation_katalog:
                if (roleId.equals(SessionManagement.ROLE_SPG)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id_brand", brandId);
                    bundle.putString("id", tokoId);

                    setFragments(katalogSpgFragment, "2", bundle);
                } else {
                    setFragments(katalogFragment, "2");
                    active = fragmentMutasiBarang;
                }
                return true;
            case R.id.navigation_mutasi_barang:
                if (roleId.equals(SessionManagement.ROLE_MANAGER) ||
                        roleId.equals(SessionManagement.ROLE_KOORDINATOR)) {
                    showDialog();
                    Log.d("Masuk", "Role id 3 || 4");
                    Button btnBM = dialog.findViewById(R.id.btnBarangMasuk);
                    btnBM.setOnClickListener(v -> {
                        setFragments(fragmentListTokoBM, "3");
                        active = fragmentListTokoBM;
                        dialog.dismiss();
                    });
                    Button btnMB = dialog.findViewById(R.id.btnMutasiBarang);
                    btnMB.setOnClickListener(v -> {
                        setFragments(fragmentMutasiBarang, "3");
                        active = fragmentMutasiBarang;
                        dialog.dismiss();
                    });
                } else if (roleId.equals(SessionManagement.ROLE_SPG)) {
                    setFragments(fragmentMutasiSPG, "3");
                    active = fragmentListTokoBM;
                } else {
                    setFragments(fragmentListTokoBM, "3");
                    active = fragmentListTokoBM;
                }

                return true;
            case R.id.navigation_penjualan:
                if (roleId.equals(SessionManagement.ROLE_SPG)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("date", new DateUtils().getTodayWithFormat("yyyy-MM-dd"));

                    setFragments(fragmentInputHarian, "4", bundle);
                } else {
                    setFragments(fragmentPenjualanListToko, "4");
                    active = fragmentPenjualanListToko;
                }
                return true;
        }
        return false;
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Objects.requireNonNull(getSupportActionBar()).hide(); //<< this
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        try {
            roleId = Session.get(Constanta.ROLE_ID).getValue();
            if (roleId.equals(SessionManagement.ROLE_SPG)) {
                brandId = Session.get(Constanta.BRAND).getValue();
                tokoId = Session.get(Constanta.TOKO).getValue();
            }
        } catch (SessionNotFoundException e) {
            roleId = "-1";
            e.printStackTrace();
        }

        Intent i = getIntent();
        String data = i.getStringExtra("FromHome");

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (data != null && data.contentEquals("1")) {
            fm.beginTransaction().add(R.id.main_container, fragmentInputHarian, "4").commit();
            navigation.setSelectedItemId(R.id.navigation_penjualan);
        } else if (data != null && data.contentEquals("Penjualan")) {
            fm.beginTransaction().add(R.id.main_container, fragmentPenjualanListToko, "4").commit();
            navigation.setSelectedItemId(R.id.navigation_penjualan);
        } else {
            fm.beginTransaction().add(R.id.main_container, fragmentHome, "1").commit();
        }

    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onResume() {
        super.onResume();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        try {
            if (!Session.get("the_day").getValue().equals(date)) {
                doReset(date);
            }
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
            doReset(date);
        }
    }

    private void doReset(String date) {
        Session.save(new SessionObject("check", SessionManagement.CHECK_OUT));
        Session.save(new SessionObject("the_day", date));
    }

    private void setFragments(Fragment fragment, String string) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        ft.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left);
        ft.replace(R.id.main_container, fragment, string);
        ft.commit();
    }

    private void setFragments(Fragment fragment, String string, Bundle bundle) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        fragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left);
        ft.replace(R.id.main_container, fragment, string);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 secs
            backPressedTime = t;
            Toast.makeText(this, "Klik sekali lagi untuk keluar",
                    Toast.LENGTH_SHORT).show();
        } else {    // this guy is serious
            // clean up
            super.onBackPressed();       // bye
        }
    }

    private void showDialog() {
        dialog = new Dialog(this);
        //set content
        dialog.setContentView(R.layout.dialog_pilih);
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
