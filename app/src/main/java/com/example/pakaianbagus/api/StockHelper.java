package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.BrandResponse;
import com.example.pakaianbagus.models.StockOpnameModel;
import com.example.pakaianbagus.models.stock.Category;
import com.example.pakaianbagus.models.stock.Item;
import com.example.pakaianbagus.models.stock.Stock;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.List;

import static com.example.pakaianbagus.api.ApiHeader.getToken;

/**
 * Created by alfianhpratama on 28/08/2019.
 * Organization: UTeam
 */
public class StockHelper {
    public static void getListBrand(RestCallback<ApiResponse<List<BrandResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListBrand(getToken()).enqueue(callback);
    }

    public static void getListItems(RestCallback<ApiResponse<List<Item>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListItems(getToken()).enqueue(callback);
    }

    public static void getListCategories(RestCallback<ApiResponse<List<Category>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListCetegories(getToken()).enqueue(callback);
    }

    public static void getListStock(String idToko, RestCallback<ApiResponse<List<Stock>>> callback) {
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListStockbyToko(getToken(), idToko).enqueue(callback);
    }

    public static void postStockOpname(StockOpnameModel data, RestCallback<ApiResponse> callback) {
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postStockOpname(getToken(), data).enqueue(callback);

    }
}
