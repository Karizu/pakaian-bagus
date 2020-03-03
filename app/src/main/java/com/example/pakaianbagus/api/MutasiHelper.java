package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.MutationRequest;
import com.example.pakaianbagus.models.api.mutation.Mutation;
import com.example.pakaianbagus.models.api.mutation.detail.Expedition;
import com.example.pakaianbagus.models.api.mutation.detail.MutationDetail;
import com.example.pakaianbagus.models.mutation.MutationResponse;
import com.example.pakaianbagus.models.stock.Stock;
import com.example.pakaianbagus.models.usermutation.UserMutationResponse;
import com.example.pakaianbagus.presentation.mutasibarang.MutasiDetail;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Callback;

import static com.example.pakaianbagus.api.ApiHeader.getToken;

public class MutasiHelper {

    public static void getListMutation(List<Integer> statusList, String id_brand, String id_toko,
                                       RestCallback<ApiResponse<List<Mutation>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListMutation(statusList, id_brand, id_toko).enqueue(callback);
    }

    public static void getListMutationByDate(List<Integer> statusList, String id_brand, String id_toko, String date,
                                       RestCallback<ApiResponse<List<Mutation>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListMutationByDate(statusList, id_brand, id_toko, date).enqueue(callback);
    }

    public static void getDetailMutation(int id, RestCallback<ApiResponse<MutationDetail>> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getDetailMutation(id).enqueue(callback);
    }

    public static void postMutasi(MutationRequest mutationRequest, RestCallback<ApiResponse<MutationResponse>> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().postDetailMutation(mutationRequest).enqueue(callback);
    }

    public static void verifyMutation(String mutation_id, RequestBody requestBody,RestCallback<ApiResponse<MutationResponse>> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().verifyMutation(mutation_id, requestBody).enqueue(callback);
    }

    public static void deleteMutation(String mutation_id,RestCallback<ApiResponse<MutationResponse>> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().deleteMutation(mutation_id).enqueue(callback);
    }

    public static void getUserMutation(String group_id, String brand_id, RestCallback<ApiResponse<List<UserMutationResponse>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getUserMutation(group_id, brand_id).enqueue(callback);
    }

    public static void getListExpeditions(RestCallback<ApiResponse<List<Expedition>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListExpeditions().enqueue(callback);
    }

    public static void createUserMutation(RequestBody requestBody, RestCallback<ApiResponse<UserMutationResponse>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().createUserMutation(requestBody).enqueue(callback);
    }

    public static void getListStock(String store_id, Callback<ApiResponse<List<Stock>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListStock(store_id).enqueue(callback);
    }

}
