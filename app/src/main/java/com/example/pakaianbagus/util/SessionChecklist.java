package com.example.pakaianbagus.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.pakaianbagus.MainActivity;
import com.example.pakaianbagus.models.Checklist;
import com.example.pakaianbagus.models.RoleChecklist;
import com.example.pakaianbagus.presentation.auth.SignInActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SessionChecklist {
    // Context
    private Context _context;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "PakaianBagusPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static String CHECKLIST = "checklist";
    private static String POST_CHECKLIST = "post_checklist";
    public static final String KEY_USER_ID = "id";
    public static final String KEY_ROLE_ID = "roleId";
    public static final String KEY_BEARER_TOKEN = "token";
    public static final String ROLE_ADMIN = "4";
    public static final String ROLE_KOORDINATOR = "3";
    public static final String ROLE_SPG = "2";
    public static final String ROLE_SALES = "1";
    public static final String CHECK_IN = "CHECK IN";
    public static final String CHECK_OUT = "CHECK OUT";

    @SuppressLint("CommitPrefEdits")
    public SessionChecklist(Context context) {
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void createLoginSession(String id, String roleId,  String token){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_ROLE_ID, roleId);
        editor.putString(KEY_BEARER_TOKEN, token);
        // commit changes
        editor.commit();
    }

    public void setChecklist(String checklistId, int notification) {
        editor.putInt(checklistId, notification);
        CHECKLIST = checklistId;
        editor.commit();
    }

    public int getChecklist(String checklistId) {
        int checklist = 0;
        if (CHECKLIST.equals(checklistId)){
            checklist = pref.getInt(checklistId, 0);
        }
        return checklist;
    }

    public void setArraylistChecklist(List<Checklist> data){
        Gson gson = new Gson();
        List<Checklist> textList = new ArrayList<>(data);
        String jsonText = gson.toJson(textList);
        editor.putString("key", jsonText);
        editor.apply();
    }

    public String getArrayListChecklist(){
        Gson gson = new Gson();

        return pref.getString("key", null);
    }

    public void checkLogin(){
        // Check login status
        if(this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Staring Login Activity
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_ROLE_ID, pref.getString(KEY_ROLE_ID, null));
        user.put(KEY_BEARER_TOKEN, pref.getString(KEY_BEARER_TOKEN, null));
        // return user
        return user;
    }

    public void logoutUser(){

        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    private boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}


