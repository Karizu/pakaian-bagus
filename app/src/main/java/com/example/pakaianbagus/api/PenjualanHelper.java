package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.AccountsReceivable;
import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.BrandResponse;
import com.example.pakaianbagus.models.PenjualanResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.List;

import okhttp3.RequestBody;

public class PenjualanHelper {

    public static void getPenjualan(String idStore, int limit, int offset, RestCallback<ApiResponse<List<PenjualanResponse>>> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getPenjualan(idStore, false, limit, offset, "t_transaksi.no_faktur_penjualan", "asc").enqueue(callback);
    }

    public static void getListBrand(RestCallback<ApiResponse<List<BrandResponse>>> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListBrand().enqueue(callback);
    }

    public static void getPiutang(String member_id, RestCallback<ApiResponse<List<AccountsReceivable>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getPiutang(member_id).enqueue(callback);
    }

    public static void postPembayaranPiutang(RequestBody requestBody, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postPembayaranPiutang(requestBody).enqueue(callback);
    }

    /*public static void getDetailBarangMasuk(int mutationId, RestCallback<ApiResponse<List<MutasiDetail>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getDetailMutation(mutationId).enqueue(callback);
    }*/
}
