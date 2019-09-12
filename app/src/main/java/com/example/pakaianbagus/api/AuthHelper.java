package com.example.pakaianbagus.api;

import android.content.Context;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.LoginRequest;
import com.example.pakaianbagus.models.User;
import com.example.pakaianbagus.models.auth.Auth;
import com.example.pakaianbagus.util.SessionManagement;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class AuthHelper {
//
//    public static void register(RequestBody registerRequest, RestCallback<ApiResponse> callback){
//        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
//        service.getEndpoint().postRegister(registerRequest).enqueue(callback);
//    }

    //public static void login(LoginRequest loginRequest, Callback<ApiResponse<User>> callback){
    public static void login(LoginRequest loginRequest, Callback<Auth> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postLogin(loginRequest).enqueue(callback);
    }

    public static void doLogin(String username, String password, Callback<ResponseBody> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().loginRequest(username, password).enqueue(callback);
    }

//    public static void registerUserDevice(RequestBody userDeviceRequest, RestCallback<ApiResponse> callback){
//        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
//        service.getEndpoint().postUserDevice(userDeviceRequest).enqueue(callback);
//    }
//
//    public static void removeUserDevice(String deviceId, RestCallback<ApiResponse> callback){
//        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
//        service.getEndpoint().removeUserDevice(deviceId).enqueue(callback);
//    }
//
//    public static void updateProfile(RequestBody profile, RestCallback<ApiResponse> callback){
//        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
//        service.getEndpoint().updateProfile(profile).enqueue(callback);
//    }
//
//    public static void myProfile(RestCallback<ApiResponse<User>> callback){
//        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
//        service.getEndpoint().myProfile().enqueue(callback);
//    }
//
//    public static void generateToken(String userId, RestCallback<ApiResponse<Token>> callback){
//        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
//        service.getEndpoint().generateToken(userId).enqueue(callback);
//    }
//
//    public static void forgotPassword(String email, RestCallback<ApiResponse> callback){
//        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
//        service.getEndpoint().postForgotPassword(email).enqueue(callback);
//    }
//
//    public static void recoverPassword(String email, String number, String password, RestCallback<ApiResponse> callback){
//        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
//        service.getEndpoint().postRecoverPassword(email, number, password).enqueue(callback);
//    }


}
