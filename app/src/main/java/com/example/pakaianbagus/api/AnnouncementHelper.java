package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.AnnouncementDetailResponse;
import com.example.pakaianbagus.models.ApiResponse;
import com.rezkyatinnov.kyandroid.reztrofit.RestCallback;
import com.rezkyatinnov.kyandroid.reztrofit.Reztrofit;

/**
 * Created by alfianhpratama on 18/09/2019.
 * Organization: UTeam
 */
public class AnnouncementHelper {
    public static void getDetailAnnouncement(String id, RestCallback<ApiResponse<AnnouncementDetailResponse>> callback) {
        Reztrofit<ApiInterface> service = Api.getService();
        service.getEndpoint().getDetailAnnouncement(id).enqueue(callback);
    }
}
