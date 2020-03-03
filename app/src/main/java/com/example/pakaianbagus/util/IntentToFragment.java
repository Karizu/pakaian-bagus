package com.example.pakaianbagus.util;

import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pakaianbagus.MainActivity;
import com.example.pakaianbagus.R;
import com.example.pakaianbagus.presentation.home.HomeFragment;
import com.example.pakaianbagus.presentation.katalog.SearchKatalogFragment;

import java.util.Objects;

public class IntentToFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_to_fragment);
        String keyword = null;
        String store_id = null, brand_id = null;
        try {
            Intent intent = getIntent();
            keyword = intent.getStringExtra("keyword");
            store_id = intent.getExtras().getString("store_id");
            brand_id = intent.getExtras().getString("brand_id");

        } catch (Exception e){
            e.printStackTrace();
        }

        if (keyword != null){
            if (keyword.equals("Home")){
                backHome(keyword);
            } else if (keyword.equals("Penjualan")){
                goToPenjualan();
            } else {
                showFragment(keyword, store_id, brand_id);
            }
        }
    }

    private void showFragment(String keyword, String store_id, String brand_id){
        Bundle bundle = new Bundle();
        bundle.putString("store_id", store_id);
        bundle.putString("brand_id", brand_id);
        bundle.putString("keyword", keyword);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        SearchKatalogFragment katalogFragment = new SearchKatalogFragment();
        katalogFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.layoutKatalog, katalogFragment);
        ft.commit();
    }

    private void backHome(String keyword){
        Bundle bundle = new Bundle();
        bundle.putString("id", "2");
        bundle.putString("keyword", keyword);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fm).beginTransaction();
        HomeFragment katalogFragment = new HomeFragment();
        katalogFragment.setArguments(bundle);
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        ft.replace(R.id.layoutKatalog, katalogFragment);
        ft.commit();
    }

    private void goToPenjualan(){
//        Bundle bundle = new Bundle();
//        bundle.putString("id", "2");
//        bundle.putString("keyword", keyword);


        Intent intent = new Intent(IntentToFragment.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("FromHome", "Penjualan");
        startActivity(intent);
    }
}
