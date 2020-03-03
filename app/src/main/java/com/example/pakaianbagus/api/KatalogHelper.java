package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.BrandResponse;
import com.example.pakaianbagus.models.TokoResponse;
import com.example.pakaianbagus.models.stock.Stock;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.List;

import retrofit2.Callback;

import static com.example.pakaianbagus.api.ApiHeader.getToken;

public class KatalogHelper {

    public static void getListToko(String type, Callback<ApiResponse<List<TokoResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListToko(type).enqueue(callback);
        //SessionManagement session = new SessionManagement(context);
        //HashMap<String, String> user = session.getUserDetails();
        //service.getEndpoint().getListBrand(user.get(SessionManagement.KEY_BEARER_TOKEN)).enqueue(callback);
    }

    public static void getListStokToko(String id, String idBrand, int limit, int offset, RestCallback<ApiResponse<List<Stock>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListStokToko(id, idBrand, limit, offset).enqueue(callback);
    }

    public static void searchKatalog(String id_store, String brand_id, String keyword, int limit, int offset, RestCallback<ApiResponse<List<Stock>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().searchKatalog(id_store, brand_id, keyword, limit, offset).enqueue(callback);
    }

    public static void searchKatalogByCategory(String id_store, String brand_id, String m_category_id, int limit, int offset, RestCallback<ApiResponse<List<Stock>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().searchKatalogByCategory(id_store, brand_id, m_category_id, limit, offset).enqueue(callback);
    }

    public static void getListBrand(RestCallback<ApiResponse<List<BrandResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListBrand().enqueue(callback);
    }

    public static void searchBarangPenjualan(String keyword, RestCallback<ApiResponse<List<Stock>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().searchBarangPenjualan(keyword).enqueue(callback);
    }
}
