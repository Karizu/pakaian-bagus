package com.example.pakaianbagus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.pakaianbagus.presentation.barangmasuk.BarangMasukFragment;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.mutasibarang.MutasiBarangFragment;
import com.example.pakaianbagus.presentation.penjualan.InputHarianFragment;
import com.example.pakaianbagus.presentation.stockopname.StockOpnameFragment;
import com.example.pakaianbagus.util.adapter.NoSwipePager;
import com.example.pakaianbagus.util.adapter.ViewPagerAdapter;

import java.util.Objects;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private NoSwipePager viewPager;
    private BottomNavigationView navigation;
    MenuItem prevMenuItem;
    HomeFragment fragmentHome;
    StockOpnameFragment fragmentStockOpname;
    BarangMasukFragment fragmentBarangMasuk;
    InputHarianFragment fragmentInputHarian;
    MutasiBarangFragment fragmentMutasiBarang;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.navigation_stock_opname:
                viewPager.setCurrentItem(1);
                break;
            case R.id.navigation_barang_masuk:
                viewPager.setCurrentItem(2);
                break;
            case R.id.navigation_input_harian:
                viewPager.setCurrentItem(3);
                break;
        }
        return false;
    };

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentHome = new HomeFragment();
        fragmentMutasiBarang = new MutasiBarangFragment();
        fragmentBarangMasuk = new BarangMasukFragment();
        fragmentInputHarian = new InputHarianFragment();
        viewPagerAdapter.addFragment(fragmentHome);
        viewPagerAdapter.addFragment(fragmentStockOpname);
        viewPagerAdapter.addFragment(fragmentBarangMasuk);
        viewPagerAdapter.addFragment(fragmentInputHarian);
        viewPager.setAdapter(viewPagerAdapter);
    }

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

        viewPager = findViewById(R.id.viewpager);

        navigation = findViewById(R.id.navigation);
        navigation.setVisibility(View.VISIBLE);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
    }

}
