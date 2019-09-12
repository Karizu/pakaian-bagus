package com.example.pakaianbagus.util.interceptor;

import android.util.Log;

import com.example.pakaianbagus.util.Constanta;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by ulfi on 29,January,2019
 * Organization: UTeam
 */

public final class HttpJsonInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response, responseChange;

        Log.d("intercept", "start");

        try {
            response = chain.proceed(request);
            responseChange = response;
        } catch (Exception e) {
            Log.d("exception ", "intercept:" + e.getMessage());
            throw e;
        }

        ResponseBody responseBody = responseChange.body();
        String responseText = responseBody.string();
        MediaType contentType = response.body().contentType();
        ResponseBody body = ResponseBody.create(contentType, responseText);

        boolean statusResponse;
        String message;

        try {
            JSONObject responseObj = new JSONObject(responseText);
            statusResponse = responseObj.getBoolean("status");
            message = responseObj.getString("message");
        } catch (Exception e) {
            return response.newBuilder().body(body).build();
        }

        if (!statusResponse) {
            throw new RuntimeException(message);
        }

        return response.newBuilder().body(body).build();
    }
}
