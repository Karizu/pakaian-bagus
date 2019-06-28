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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.pakaianbagus.presentation.barangmasuk.BarangMasukFragment;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.katalog.KatalogFragment;
import com.example.pakaianbagus.presentation.mutasibarang.MutasiBarangFragment;
import com.example.pakaianbagus.presentation.penjualan.InputHarianFragment;
import com.example.pakaianbagus.presentation.home.stockopname.StockOpnameFragment;

import java.util.Objects;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime = 0;
    private BottomNavigationView navigation;
    final Fragment fragmentHome = new HomeFragment();
    final Fragment fragmentStockOpname = new StockOpnameFragment();
    final Fragment fragmentBarangMasuk = new BarangMasukFragment();
    final Fragment fragmentInputHarian = new InputHarianFragment();
    final Fragment fragmentMutasiBarang = new MutasiBarangFragment();
    final Fragment katalogFragment = new KatalogFragment();
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
    Fragment active = fragmentHome;
    Dialog dialog;

    @SuppressLint("CommitTransaction")
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                setFragments(fragmentHome, "1");
                active = fragmentHome;
                return true;
            case R.id.navigation_katalog:
                setFragments(katalogFragment, "2");
                active = fragmentMutasiBarang;
                return true;
            case R.id.navigation_mutasi_barang:
                showDialog();
                Button btnBM = dialog.findViewById(R.id.btnBarangMasuk);
                btnBM.setOnClickListener(v -> {
                    setFragments(fragmentBarangMasuk, "3");
                    active = fragmentBarangMasuk;
                    dialog.dismiss();
                });
                Button btnMB = dialog.findViewById(R.id.btnMutasiBarang);
                btnMB.setOnClickListener(v -> {
                    setFragments(fragmentMutasiBarang, "3");
                    active = fragmentMutasiBarang;
                    dialog.dismiss();
                });
                return true;
            case R.id.navigation_penjualan:
                setFragments(fragmentInputHarian, "4");
                active = fragmentInputHarian;
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
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Intent i = getIntent();
        String data = i.getStringExtra("FromHome");

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (data != null && data.contentEquals("1")) {
            fm.beginTransaction().add(R.id.main_container, fragmentInputHarian, "4").commit();
            navigation.setSelectedItemId(R.id.navigation_penjualan);
        } else {
            fm.beginTransaction().add(R.id.main_container, fragmentHome, "1").commit();
        }


    }

    private void setFragments(Fragment fragment, String string){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
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
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
