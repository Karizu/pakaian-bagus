package com.example.pakaianbagus.api;

import android.content.Context;

import com.example.pakaianbagus.models.AnnouncementResponse;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.ChecklistResponse;
import com.example.pakaianbagus.models.LoginRequest;
import com.example.pakaianbagus.models.RoleChecklist;
import com.example.pakaianbagus.models.User;
import com.example.pakaianbagus.models.user.Checklist;
import com.example.pakaianbagus.util.SessionManagement;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;

import static com.example.pakaianbagus.api.ApiHeader.getToken;

public class HomeHelper {

    public static void getListChecklist(Context context, Callback<ApiResponse<List<ChecklistResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        SessionManagement session = new SessionManagement(context);
        HashMap<String, String> user = session.getUserDetails();
        service.getEndpoint().getRoleChecklist(user.get(SessionManagement.KEY_ROLE_ID), user.get(SessionManagement.KEY_BEARER_TOKEN)).enqueue(callback);
    }

    public static void getListKoordinator(RestCallback<ApiResponse<List<User>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListKoordinator(getToken(), "2").enqueue(callback);
    }

    public static void postCheckIn(RequestBody requestBody, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postCheckIn(getToken(), requestBody).enqueue(callback);
    }

    //public static void postCheckOut(Map<String,RequestBody> requestBody, MultipartBody.Part images, RestCallback<ApiResponse> callback){
    public static void postCheckOut(RequestBody requestBody, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postCheckOut(getToken(), requestBody).enqueue(callback);
        //service.getEndpoint().postCheckOut(requestBody, images).enqueue(callback);
    }

    public static void getAnnouncement(RestCallback<ApiResponse<List<AnnouncementResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getAnnouncement(getToken()).enqueue(callback);
    }

    public static void postChecklistByUserId(String userId, String date, String checklist, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postChecklistByUserId(getToken(), userId, date, checklist).enqueue(callback);
    }

    public static void getListChecklistUser(String user_id, String date, Callback<ApiResponse<List<Checklist>>> apiResponseCallback) {
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListChecklistUser(getToken(), user_id, date).enqueue(apiResponseCallback);
    }
}
