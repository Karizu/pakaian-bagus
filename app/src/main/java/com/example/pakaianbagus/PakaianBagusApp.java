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

        String sharedPrefName = "pakaianbagusapp";
        String schemaName = "pakaianbagus_db";
        int schemaVersion = 1;
        boolean enableRx = false;

        Kyandroid.init(this,
                ApiInterface.BASE_URL,
                ApiInterface.class,
                sharedPrefName,
                Context.MODE_PRIVATE,
                schemaName,
                schemaVersion,
                enableRx,
                new KyandroidRealmModule(),
                new PakaianBagusRealmModule());
    }

}
