package com.example.pakaianbagus.api;

import android.content.Context;

import com.example.pakaianbagus.models.AnnouncementResponse;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.LoginRequest;
import com.example.pakaianbagus.models.RoleChecklist;
import com.example.pakaianbagus.models.User;
import com.example.pakaianbagus.util.SessionManagement;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
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

    public static void postCheckIn(RequestBody requestBody, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postCheckIn(requestBody).enqueue(callback);
    }

    public static void postCheckOut(RequestBody requestBody, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postCheckOut(requestBody).enqueue(callback);
    }

    public static void getAnnouncement(RestCallback<ApiResponse<List<AnnouncementResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getAnnouncement().enqueue(callback);
    }

    public static void postChecklistByUserId(String userId, String date, String checklist, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postChecklistByUserId(userId, date, checklist).enqueue(callback);
    }
}
