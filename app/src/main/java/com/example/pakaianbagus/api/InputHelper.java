package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.Discount;
import com.example.pakaianbagus.models.Kompetitor;
import com.example.pakaianbagus.models.SalesReport;
import com.example.pakaianbagus.models.api.penjualankompetitor.KompetitorResponse;
import com.example.pakaianbagus.models.api.salesreport.SalesReportResponse;
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

    public static void getSalesReport(String userId, String date, RestCallback<ApiResponse<List<SalesReportResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getSalesReport(userId, date).enqueue(callback);
    }

    public static void getPenjualanKompetitor(String placeId, String date, RestCallback<ApiResponse<List<KompetitorResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getPenjualanKompetitor(placeId, date).enqueue(callback);
    }

    public static void postSalesReport(SalesReport salesReport, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postSalesReport(salesReport).enqueue(callback);
    }

    public static void postPenjualanKompetitor(Kompetitor data, RestCallback<ApiResponse> callback) {
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postPenjualanKompetitor(data).enqueue(callback);

    }
}
