package com.example.pakaianbagus;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.pakaianbagus.presentation.mutasibarang.ListBrandMutasiBarangFragment;
import com.example.pakaianbagus.presentation.penjualan.PenjualanListTokoFragment;
import com.example.pakaianbagus.presentation.penjualan.sales.PenjualanListTokoSalesFragment;
import com.example.pakaianbagus.util.MyAlarm;
import com.example.pakaianbagus.util.SessionChecklist;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Calendar;
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
    final Fragment fragmentListBrandMutasi = new ListBrandMutasiBarangFragment();
    final Fragment fragmentInputHarian = new InputHarianFragment();
    final Fragment fragmentPenjualanListBrand = new PenjualanBrandFragment();
    final Fragment fragmentPenjualanListToko = new PenjualanListTokoFragment();
    final Fragment fragmentPenjualanListSalesToko = new PenjualanListTokoSalesFragment();
    final Fragment fragmentMutasiBarang = new MutasiBarangFragment();
    final Fragment katalogFragment = new KatalogBrandFragment();
    final Fragment katalogSpgFragment = new KatalogListBarang();
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
    Fragment active = fragmentHome;
    Dialog dialog;
    String roleId, brandId, tokoId;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private BroadcastReceiver br;
    private SessionChecklist sessionChecklist;

    public class MyReceiver extends BroadcastReceiver {

        public MyReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TAG", "onReceiver");
            try {
                Toast.makeText(MainActivity.this, "onReceiver", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

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
                    Button btnBM = dialog.findViewById(R.id.btnBarangMasuk);
                    btnBM.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constanta.FLAG_MUTASI, Constanta.BARANG_MASUK);
                        setFragments(fragmentListBrandMutasi, "3", bundle);
                        active = fragmentListTokoBM;
                        dialog.dismiss();
                    });
                    Button btnMB = dialog.findViewById(R.id.btnMutasiBarang);
                    btnMB.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constanta.FLAG_MUTASI, Constanta.MUTASI_BARANG);
                        setFragments(fragmentListBrandMutasi, "3", bundle);
                        active = fragmentListTokoBM;
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
                } else if (roleId.equals(SessionManagement.ROLE_SALES)) {
                    setFragments(fragmentPenjualanListSalesToko, "4");
                    active = fragmentPenjualanListSalesToko;
                } else {
                    setFragments(fragmentPenjualanListBrand, "4");
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
            fm.beginTransaction().add(R.id.main_container, fragmentPenjualanListBrand, "4").commit();
            navigation.setSelectedItemId(R.id.navigation_penjualan);
        } else {
            fm.beginTransaction().add(R.id.main_container, fragmentHome, "1").commit();
        }

        // Set the alarm to start at 21:32 PM
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        Log.d("TIME", String.valueOf(calendar.getTimeInMillis()));
//        calendar.set(Calendar.HOUR_OF_DAY, 14);
//        calendar.set(Calendar.MINUTE, 49);

//        setAlarm(calendar.getTimeInMillis());

    }

//    private void setAlarm(long time) {
//        //getting the alarm manager
//        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        //creating a new intent specifying the broadcast receiver
//        Intent i = new Intent(this, MyAlarm.class);
//
//        //creating a pending intent using the intent
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
//
//        //setting the repeating alarm that will be fired every day
//        Objects.requireNonNull(am).setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);
//        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
//    }

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
        sessionChecklist = new SessionChecklist(MainActivity.this);
        sessionChecklist.logoutUser();
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
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            long t = System.currentTimeMillis();
            if (t - backPressedTime > 2000) {    // 2 secs
                backPressedTime = t;
                Toast.makeText(this, "Klik sekali lagi untuk keluar",
                        Toast.LENGTH_SHORT).show();
            } else {    // this guy is serious
                // clean up
                super.onBackPressed();       // bye
            }
        } else {
            getSupportFragmentManager().popBackStack();
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
