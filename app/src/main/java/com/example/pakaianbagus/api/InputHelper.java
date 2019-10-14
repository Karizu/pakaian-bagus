package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.Discount;
import com.example.pakaianbagus.models.SalesReport;
import com.example.pakaianbagus.models.Transaction;
import com.example.pakaianbagus.models.TransactionModel;
import com.example.pakaianbagus.models.api.penjualankompetitor.Kompetitor;
import com.example.pakaianbagus.models.api.salesreport.SalesReportResponse;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.models.transaction.Member;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.List;

/**
 * Created by alfianhpratama on 05/09/2019.
 * Organization: UTeam
 */
public class InputHelper {

    public static void getDetailStock(String barcode, RestCallback<ApiResponse<List<Stock>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getDetailStockBarcode(barcode).enqueue(callback);
    }

    public static void getDiscount(RestCallback<ApiResponse<List<Discount>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getDiscount().enqueue(callback);
    }

    public static void getSalesReport(String placeID, String date, RestCallback<ApiResponse<List<SalesReportResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getSalesReport(placeID, date).enqueue(callback);
    }

    public static void getPenjualanKompetitor(String placeId, String date, RestCallback<ApiResponse<List<Kompetitor>>> callback){
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

    public static void postUpdatePenjualanKompetitor(String id, Kompetitor data, RestCallback<ApiResponse> callback) {
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postUpdatePenjualanKompetitor(id, data).enqueue(callback);
    }

    public static void postSalesReportJson(TransactionModel transactionModel, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postSalesReportJson(transactionModel).enqueue(callback);
    }

    public static void getMember(RestCallback<ApiResponse<List<Member>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getMember().enqueue(callback);
    }

    public static void getDetailTransaction(String trxId, RestCallback<ApiResponse<Transaction>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getDetailTransaction(trxId).enqueue(callback);
    }
}
