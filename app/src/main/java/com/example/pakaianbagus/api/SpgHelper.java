package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.BrandResponse;
import com.example.pakaianbagus.models.auth.Group;
import com.example.pakaianbagus.models.auth.Place;
import com.example.pakaianbagus.models.detailspg.AttendanceResponse;
import com.example.pakaianbagus.models.user.User;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.List;

import static com.example.pakaianbagus.api.ApiHeader.getToken;

public class SpgHelper {
    public static void getListBrand(RestCallback<ApiResponse<List<BrandResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListBrand().enqueue(callback);
    }

    public static void getListSpg(String brand_id, String place_id, RestCallback<ApiResponse<List<User>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListSpg("3", brand_id, place_id).enqueue(callback);
    }

    public static void getDetailSpg(String idSpg, RestCallback<ApiResponse<User>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getDetailSpg(idSpg).enqueue(callback);
    }

    public static void getDetailPlace(String place_id, RestCallback<ApiResponse<Place>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getDetailPlace(place_id).enqueue(callback);
    }

    public static void getDetailPlaceSpg(String place_id, RestCallback<ApiResponse<Place>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getDetailPlaceSpg(place_id).enqueue(callback);
    }

    public static void getAttendance(String user_id, RestCallback<ApiResponse<List<AttendanceResponse>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getAttendance(user_id).enqueue(callback);
    }

    public static void getListPlace(RestCallback<ApiResponse<List<Place>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListPlace("S").enqueue(callback);
    }

    public static void getListGroup(RestCallback<ApiResponse<List<Group>>> callback){
        Reztrofit<ApiInterface> service = Reztrofit.getInstance();
        service.getEndpoint().getListGroup().enqueue(callback);
    }
}
