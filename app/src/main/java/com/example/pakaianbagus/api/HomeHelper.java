package com.example.pakaianbagus.api;

import android.content.Context;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.LoginRequest;
import com.example.pakaianbagus.models.RoleChecklist;
import com.example.pakaianbagus.models.User;
import com.example.pakaianbagus.util.SessionManagement;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;

public class HomeHelper {

    public static void getListChecklist(Context context, Callback<ApiResponse<List<RoleChecklist>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        SessionManagement session = new SessionManagement(context);
        HashMap<String, String> user = session.getUserDetails();
        service.getEndpoint().getRoleChecklist(user.get(SessionManagement.KEY_ROLE_ID), user.get(SessionManagement.KEY_BEARER_TOKEN)).enqueue(callback);
    }

    public static void getListKoordinator(RestCallback<ApiResponse<List<User>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListKoordinator("2").enqueue(callback);
    }

}
