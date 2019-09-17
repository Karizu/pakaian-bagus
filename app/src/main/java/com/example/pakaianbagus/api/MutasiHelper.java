package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.MutationRequest;
import com.example.pakaianbagus.models.api.mutation.Mutation;
import com.example.pakaianbagus.models.api.mutation.detail.MutationDetail;
import com.example.pakaianbagus.presentation.mutasibarang.MutasiDetail;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

import java.util.ArrayList;
import java.util.List;

import static com.example.pakaianbagus.api.ApiHeader.getToken;

/**
 * Created by alfianhpratama on 13/09/2019.
 * Organization: UTeam
 */
public class MutasiHelper {

    public static void getListMutation(List<Integer> statusList, String id_brand, String id_toko,
                                       RestCallback<ApiResponse<List<Mutation>>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getListMutation(statusList, id_brand, id_toko).enqueue(callback);
    }

    public static void getDetailMutation(int id, RestCallback<ApiResponse<MutationDetail>> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getDetailMutation(id).enqueue(callback);
    }

    public static void postMutasi(MutationRequest mutationRequest, RestCallback<ApiResponse> callback){
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().postDetailMutation(mutationRequest).enqueue(callback);
    }
}
