package com.example.pakaianbagus.api;

import com.example.pakaianbagus.util.interceptor.HttpJsonInterceptor;
import com.example.pakaianbagus.util.interceptor.RequestInterceptor;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    public static Reztrofit<ApiInterface> getService() {
        //service.addInterceptor(new RequestInterceptor());
        return (Reztrofit<ApiInterface>) Reztrofit.getInstance();
    }
    /*private static <T> T builder(Class<T> endpoint) {
     *//*HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        HttpJsonInterceptor interceptorJson = new HttpJsonInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);*//*

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor())
                .addNetworkInterceptor(new RequestInterceptor())
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(endpoint);
    }

    public static ApiInterface apiInterface() {
        return builder(ApiInterface.class);
    }*/
}