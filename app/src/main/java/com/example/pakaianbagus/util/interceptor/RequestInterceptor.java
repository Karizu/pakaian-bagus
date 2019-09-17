package com.example.pakaianbagus.util.interceptor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.pakaianbagus.util.Constanta;
import com.rezkyatinnov.kyandroid.session.Session;
import com.rezkyatinnov.kyandroid.session.SessionNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by alfianhpratama on 13/09/2019.
 * Organization: UTeam
 */
public class RequestInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        return interceptResponse(chain.proceed(interceptRequest(chain)));
    }

    private Request interceptRequest(Chain chain) {
        Request originalRequest = chain.request();

        try {
            Headers headers = new Headers.Builder()
                    .add("Authorization", Session.get(Constanta.AUTH).getValue())
                    .build();

            Request newRequest = originalRequest.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE) // Sets this request's Cache-Control header, replacing any cache control headers already present.
                    .headers(headers) //Removes all headers on this builder and adds headers.
                    .method(originalRequest.method(), null) // Adds request method and request body
                    .build();

            Log.d("interceptRequest: ", newRequest.toString());

            return newRequest;

        } catch (SessionNotFoundException e) {
            e.printStackTrace();
        }

        return originalRequest;
    }

    private Response interceptResponse(Response response) {
        Log.d("interceptResponse: ", response.body().toString());
        /*try {
            JSONObject jsonObject = new JSONObject();
            if (response.code() == 200) {
                jsonObject.put("code", 200);
                jsonObject.put("status", "OK");
                jsonObject.put("message", new JSONObject(response.body().string()));
            } else {
                jsonObject.put("code", 404);
                jsonObject.put("status", "ERROR");
                jsonObject.put("message", new JSONObject(response.body().string()));
            }
            MediaType contentType = response.body().contentType();
            ResponseBody body = ResponseBody.create(contentType, jsonObject.toString());
            return response.newBuilder().body(body).build();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }*/

        return response;
    }
}
