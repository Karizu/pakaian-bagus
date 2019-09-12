package com.example.pakaianbagus.api;

import android.util.Log;

import com.example.pakaianbagus.util.Constanta;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;

/**
 * Created by alfianhpratama on 12/09/2019.
 * Organization: UTeam
 */
class ApiHeader {
    static String getToken() {
        try {
            Log.d("getToken: ", Session.get(Constanta.AUTH).getValue());
            return Session.get(Constanta.AUTH).getValue();
        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
