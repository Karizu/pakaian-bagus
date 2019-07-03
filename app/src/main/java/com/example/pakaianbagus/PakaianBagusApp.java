package com.example.pakaianbagus;

import android.app.Application;
import android.content.Context;

import com.example.pakaianbagus.api.ApiInterface;

import com.example.pakaianbagus.models.PakaianBagusRealmModule;
import com.rezkyatinnov.kyandroid.Kyandroid;
import com.rezkyatinnov.kyandroid.localdata.KyandroidRealmModule;

public class PakaianBagusApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Kyandroid.init(this, ApiInterface.BASE_URL, ApiInterface.class, "pakaianbagusapp", Context.MODE_PRIVATE
                , "pakaianbagus_db", 1, false, new KyandroidRealmModule(), new PakaianBagusRealmModule());
    }

}
