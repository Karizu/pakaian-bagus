package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.BrandResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.List;

import static com.example.pakaianbagus.api.ApiHeader.getToken;

/**
 * Created by alfianhpratama on 28/08/2019.
 * Organization: UTeam
 */
public class SpgHelper {
    public static void getListBrand(RestCallback<ApiResponse<List<BrandResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListBrand(getToken()).enqueue(callback);
    }
}
