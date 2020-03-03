package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.BrandResponse;
import com.example.pakaianbagus.models.StockOpnameModel;
import com.example.pakaianbagus.models.api.CategoryResponse;
import com.example.pakaianbagus.models.api.StockCategory;
import com.example.pakaianbagus.models.api.stockopname.StockCategoryResponse;
import com.example.pakaianbagus.models.stock.Category;
import com.example.pakaianbagus.models.stock.Item;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.models.stockopname.StockOpnameModels;
import com.example.pakaianbagus.models.stockopname.response.StockOpnameResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.List;

import static com.example.pakaianbagus.api.ApiHeader.getToken;

/**
 * Created by alfianhpratama on 28/08/2019.
 * Organization: UTeam
 */
public class StockHelper {
    public static void getListBrand(RestCallback<ApiResponse<List<BrandResponse>>> callback) {
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListBrand().enqueue(callback);
    }

    public static void getListItems(RestCallback<ApiResponse<List<Item>>> callback) {
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListItems().enqueue(callback);
    }

    public static void getListCategories(RestCallback<ApiResponse<List<Category>>> callback) {
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListCetegories().enqueue(callback);
    }

    public static void getListStock(String idToko, String idBrand,
                                    RestCallback<ApiResponse<List<StockCategory>>> callback) {
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListStockbyToko(idToko, idBrand).enqueue(callback);
    }

    public static void postStockOpname(StockOpnameModels data, RestCallback<ApiResponse> callback) {
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().postStockOpname(data).enqueue(callback);

    }

    public static void getListStockOpname(String brandId, String placeId, int type,
                                      RestCallback<ApiResponse<List<StockOpnameResponse>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListStockOpname(brandId, placeId, type).enqueue(callback);
    }

    public static void getSelisihStok(String m_place_id, String article_code,
                                    RestCallback<ApiResponse<List<Stock>>> callback) {
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getSelisihStok(m_place_id, article_code).enqueue(callback);
    }
}
