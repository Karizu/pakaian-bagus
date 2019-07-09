package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.PenerimaanBarangResponse;
import com.example.pakaianbagus.models.StokToko;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.List;

public class BarangHelper {

    public static void getListBarangMasuk(String id_store, int limit, int offset, RestCallback<ApiResponse<List<PenerimaanBarangResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListBarangMasuk(id_store, limit, offset).enqueue(callback);
    }

    public static void getDetailBarangMasuk(String id, RestCallback<ApiResponse<PenerimaanBarangResponse>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getDetailBarangMasuk(id).enqueue(callback);
    }
}
