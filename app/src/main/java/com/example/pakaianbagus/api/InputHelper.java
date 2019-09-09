package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.Discount;
import com.example.pakaianbagus.models.stock.StokToko;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.List;

import okhttp3.RequestBody;

/**
 * Created by alfianhpratama on 05/09/2019.
 * Organization: UTeam
 */
public class InputHelper {

    public static void getDetailStock(String barcode, RestCallback<ApiResponse<List<StokToko>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getDetailStockBarcode(barcode).enqueue(callback);
    }

    public static void getDiscount(RestCallback<ApiResponse<List<Discount>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getDiscount().enqueue(callback);
    }

    public static void postSalesReport(RequestBody requestBody, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postSalesReport(requestBody).enqueue(callback);
    }
}
