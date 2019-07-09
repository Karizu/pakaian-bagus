package com.example.pakaianbagus.api;

import android.content.Context;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.StokToko;
import com.example.pakaianbagus.models.Toko;
import com.example.pakaianbagus.models.TokoResponse;
import com.example.pakaianbagus.util.SessionManagement;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;

public class KatalogHelper {

    public static void getListToko(Context context, Callback<ApiResponse<List<TokoResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        SessionManagement session = new SessionManagement(context);
        HashMap<String, String> user = session.getUserDetails();
        service.getEndpoint().getListToko(user.get(SessionManagement.KEY_BEARER_TOKEN)).enqueue(callback);
    }

    public static void getListStokToko(String id, int limit, int offset, RestCallback<ApiResponse<List<StokToko>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListStokToko(id, limit, offset).enqueue(callback);
    }

    public static void searchKatalog(String id_store, String keyword, int limit, int offset, RestCallback<ApiResponse<List<StokToko>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().searchKatalog(id_store, "false", "false", keyword, limit, offset, "nama_barang", "desc").enqueue(callback);
    }

}
