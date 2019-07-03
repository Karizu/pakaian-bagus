package com.example.pakaianbagus.api;

import com.example.pakaianbagus.models.ApiResponse;
import com.example.pakaianbagus.models.LoginRequest;
import com.example.pakaianbagus.models.RoleChecklist;
import com.example.pakaianbagus.models.Toko;
import com.example.pakaianbagus.models.User;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

//    String BASE_URL = "http://37.72.172.144/rumah-cemara-api/public/api/";

    String BASE_URL = "http://192.168.1.15/api/";

//    @GET("userLocation/nearMe")
//    Call<ApiResponse<List<OutreachNearMeResponse>>> getOutreachListNearMe(@Query("lat") Double latitude, @Query("long") Double longitude, @Query("radius") int radius);
//
//    @GET("groupLocation/nearMe")
//    Call<ApiResponse<List<ProviderNearMeResponse>>> getProviderListNearMe(@Query("lat") Double latitude, @Query("long") Double longitude, @Query("radius") int radius, @Query("service_type") String serviceType);
//
//    @POST("register")
//    Call<ApiResponse> postRegister(@Body RequestBody registerRequest);
//
    @POST("login")
    Call<ApiResponse<User>> postLogin(@Body LoginRequest loginRequest);

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> loginRequest(@Field("login") String username,
                                    @Field("password") String password);

    @GET("public/roleChecklists")
    Call<ApiResponse<List<RoleChecklist>>> getRoleChecklist(@Query("role_id") String role_id,
                                                            @Header("Authorization") String token);

    @GET("public/store/all")
    Call<ApiResponse<List<Toko>>> getListToko(@Header("Authorization") String token);
//
//    @GET("group")
//    Call<ApiResponse<List<Treatment>>> getAllInstitution(@Query("type") String type);
//
//    @POST("userDevice")
//    Call<ApiResponse> postUserDevice(@Body RequestBody userDeviceRequest);
//
//    @DELETE("userDevice/{device_id}")
//    Call<ApiResponse> removeUserDevice(@Path("device_id") String device_id);
//
//    @POST("profile")
//    Call<ApiResponse> updateProfile(@Body RequestBody profile);
//
//    @GET("profile")
//    Call<ApiResponse<User>> myProfile();
//
//    @POST("serviceTransaction")
//    Call<ApiResponse> createBiomedicalAppointmentOutreach(@Body RequestBody appointment);
//
//    @POST("topic")
//    Call<ApiResponse> createNewTopic(@Body RequestBody newTopicRequest);
//
//    @GET("topic")
//    Call<ApiResponse<List<Topic>>> getAllTopic();
//
//    @GET("topic/{topic_id}")
//    Call<ApiResponse<Topic>> getTopicDetail(@Path("topic_id") String topicId);
//
//    @GET("serviceTransaction/myAppointment")
//    Call<ApiResponse<List<GeneralDataResponse>>> getMyAppointmentList(@Query("user_id") String userId);
//
//    @POST("message")
//    Call<ApiResponse> sendMessage(@Body Datum chat);
//
//    @GET("generateToken")
//    Call<ApiResponse<Token>> generateToken(@Query("user_id") String userId);
//
//    @GET("serviceTransaction/reportStatus")
//    Call<ApiResponse<List<GeneralDataResponse>>> getMyReport(@Query("user_id") String userId);
//
//    @POST("forgot")
//    @FormUrlEncoded
//    Call<ApiResponse> postForgotPassword(@Field("email") String email);
//
//    @POST("recover")
//    @FormUrlEncoded
//    Call<ApiResponse> postRecoverPassword(@Field("email") String email, @Field("number") String number, @Field("password") String password);
//
//    /***************** News API *********************/
//    @GET("news")
//    Call<ApiResponse<List<News>>> getNews(@Query("status") int status);
//
//    @GET("news")
//    Call<ApiResponse<List<News>>> getNewsWithCategory(@Query("news_category_id") String newsCategoryId, @Query("status") int status);
//    @GET("newsCategories")
//    Call<ApiResponse<List<CategoryModel>>> getNewsCategories();
//
//    @GET("news/{news_id}")
//    Call<ApiResponse<News>> getNewsDetail(@Path("news_id") String id);
//
//    /***************** Article API *********************/
//    @GET("article")
//    Call<ApiResponse<List<Article>>> getArticle(@Query("status") int status);
//
//    @GET("article")
//    Call<ApiResponse<List<Article>>> getArticleWithCategory(@Query("article_category_id") String articleCategoryId, @Query("status") int status);
//
//    @GET("article/{article_id}")
//    Call<ApiResponse<Article>> getArticleDetail(@Path("article_id") String id);
//
//    @GET("articleCategories")
//    Call<ApiResponse<List<CategoryModel>>> getArticleCategories();
//
//    /***************** Event API *********************/
//    @GET("event")
//    Call<ApiResponse<List<Event>>> getEvent(@Query("status") int status);
//
//    @GET("event")
//    Call<ApiResponse<List<Event>>> getEventWithCategory(@Query("event_category_id") String eventCategoryId, @Query("status") int status);
//
//    @GET("event/{event_id}")
//    Call<ApiResponse<Event>> getEventDetail(@Path("event_id") String id);
//
//    @GET("eventCategories")
//    Call<ApiResponse<List<CategoryModel>>> getEventCategories();
//
//    /***************** List API *********************/
//    @GET("userList")
//    Call<ApiResponse<List<ListReminder>>> getListReminder(@Query("user_id") String userId, @Query("type") String type);
//    @GET("userList")
//    Call<ApiResponse<List<ListSaved>>> getListSaved(@Query("user_id") String userId, @Query("type") String type);
//
//    /***************** List Appointment History API *********************/
//
//    @GET("serviceTransaction/myHistory")
//    Call<ApiResponse<List<HistoryListResponse>>> getHistoryAppointment(@Query("user_id") String userId);
//
//    @GET("serviceTransaction/myHistory")
//    Call<ApiResponse<List<HistoryList>>> getHistoryAppointmentProvider(@Query("user_id") String userId);
//
//    @POST("rating")
//    Call<ApiResponse> createRating(@Body RequestBody rating);
//
//    @FormUrlEncoded
//    @POST("userList")
//    Call<ApiResponse> postCreateUserList(@Field("user_id") String userId, @Field("type") String type, @Field("type_id") String typeId, @Field("datetime") String datetime);
//
//    @DELETE("userList/{id}")
//    Call<ApiResponse> removeBookmark(@Path("id") String id);
//
//    @GET("messageHistory")
//    Call<ApiResponse<List<Datum>>> chatHistory(@Query("channel") String channel);
}
