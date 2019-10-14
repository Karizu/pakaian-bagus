package com.example.pakaianbagus.api;

import android.content.Context;

import com.example.pakaianbagus.models.AnnouncementResponse;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.ChecklistResponse;
import com.example.pakaianbagus.models.CounterResponse;
import com.example.pakaianbagus.models.Expend;
import com.example.pakaianbagus.models.LoginRequest;
import com.example.pakaianbagus.models.RoleChecklist;
import com.example.pakaianbagus.models.StandHanger;
import com.example.pakaianbagus.models.User;
import com.example.pakaianbagus.models.expenditures.Expenditures;
import com.example.pakaianbagus.models.expenditures.RequestExpenditures;
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
        Reztrofit<ApiInterface> service = Api.getService();
        SessionManagement session = new SessionManagement(context);
        HashMap<String, String> user = session.getUserDetails();
        service.getEndpoint().getRoleChecklist(user.get(SessionManagement.KEY_ROLE_ID)).enqueue(callback);
    }

    public static void getListKoordinator(RestCallback<ApiResponse<List<User>>> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListKoordinator("2").enqueue(callback);
    }

    public static void postCheckIn(RequestBody requestBody, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().postCheckIn(requestBody).enqueue(callback);
    }

    //public static void postCheckOut(Map<String,RequestBody> requestBody, MultipartBody.Part images, RestCallback<ApiResponse> callback){
    public static void postCheckOut(RequestBody requestBody, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().postCheckOut(requestBody).enqueue(callback);
        //service.getEndpoint().postCheckOut(requestBody, images).enqueue(callback);
    }

    public static void getAnnouncement(RestCallback<ApiResponse<List<AnnouncementResponse>>> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getAnnouncement().enqueue(callback);
    }

    public static void postChecklistByUserId(String userId, String date, String checklist, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().postChecklistByUserId(userId, date, checklist).enqueue(callback);
    }

    public static void getListChecklistUser(String user_id, String date, Callback<ApiResponse<List<Checklist>>> apiResponseCallback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListChecklistUser(user_id, date).enqueue(apiResponseCallback);
    }

    public static void postImageFrontView(RequestBody requestBody, Callback<ApiResponse<CounterResponse>> apiResponseCallback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().postImageFrontView(requestBody).enqueue(apiResponseCallback);
    }

    public static void postImageStandHanger(List<MultipartBody.Part> parts, Callback<ApiResponse<CounterResponse>> apiResponseCallback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().postImageStandHanger(parts).enqueue(apiResponseCallback);
    }

    public static void postUpdateImageCounter(String counter_id, RequestBody requestBody, Callback<ApiResponse<CounterResponse>> apiResponseCallback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().postUpdateImageCounter(counter_id, requestBody).enqueue(apiResponseCallback);
    }

    public static void postImageTwoWay(List<MultipartBody.Part> parts, Callback<ApiResponse<CounterResponse>> apiResponseCallback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().postImageTwoWay(parts).enqueue(apiResponseCallback);
    }

    public static void postImageRack(List<MultipartBody.Part> parts, Callback<ApiResponse<CounterResponse>> apiResponseCallback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().postImageRack(parts).enqueue(apiResponseCallback);
    }

    public static void getListExpends(Callback<ApiResponse<List<Expend>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListExpends().enqueue(callback);
    }

    public static void createExpenditures(List<MultipartBody.Part> parts, Callback<ApiResponse<Expenditures>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().createExpenditures(parts).enqueue(callback);
    }

    public static void getListExpenditures(String role_id, String group_id, Callback<ApiResponse<List<Expenditures>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListExpenditures(role_id, group_id).enqueue(callback);
    }

    public static void getListExpendituresByDate(String date, String role_id, String group_id, Callback<ApiResponse<List<Expenditures>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListExpendituresByDate(date, role_id, group_id).enqueue(callback);
    }

    public static void getListMyExpenditures(String user_id, Callback<ApiResponse<List<Expenditures>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListMyExpenditures(user_id).enqueue(callback);
    }

    public static void getListMyExpendituresByDate(String date, String user_id, Callback<ApiResponse<List<Expenditures>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListMyExpendituresByDate(date, user_id).enqueue(callback);
    }

    public static void getDetailKunjungan(String id, Callback<ApiResponse<Expenditures>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getDetailKunjungan(id).enqueue(callback);
    }
}
