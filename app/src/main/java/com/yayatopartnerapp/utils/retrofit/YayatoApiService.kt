package com.yayatopartnerapp.utils.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface YayatoApiService {



    @Multipart
    @POST("signup")
    fun signUpUserCallApi(
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("address") address: RequestBody,
        @Part("register_id") register_id: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("password") password: RequestBody,
        @Part("type") type: RequestBody,
        @Part("step") step: RequestBody,
        @Part file1: MultipartBody.Part
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("login")
    fun loginApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>



    @FormUrlEncoded
    @POST("forgot_password")
    fun forgotPass(@FieldMap params: Map<String, String>): Call<ResponseBody>


    @Multipart
    @POST("update_profile")
    fun updateDriverCallApi(
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("address") address: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("type") type: RequestBody,
        @Part("id") id: RequestBody,
        @Part file1: MultipartBody.Part
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("get_all_vehicle")
    fun get_all_vehicle(@Field("user_id") user_id: String): Call<ResponseBody>


    @FormUrlEncoded
    @POST("add_car_patnar_request")
    fun add_car_patnar_request(
        @Field("car_id") car_id: String,
        @Field("base_fire") base_fire: String,
        @Field("rate_pre_km") rate_pre_km: String,
        @Field("start_date") start_date: String,
        @Field("start_time") start_time: String,
        @Field("end_date") end_date: String,
        @Field("end_time") end_time: String,
        @Field("user_id") user_id: String,
        @Field("status") status: String
    ): Call<ResponseBody>



    @FormUrlEncoded
    @POST("update_lat_lon")
    fun update_lat_lon( @Field("user_id") user_id: String,
                        @Field("lat") lat: String,
                        @Field("lon") lon: String):  Call<ResponseBody>



    @FormUrlEncoded
    @POST("login_partner")
    fun login_with_otp(@FieldMap params: Map<String, String>): Call<ResponseBody>


    @FormUrlEncoded
    @POST("otp_verify")
    fun verify_with_otp(@FieldMap params: Map<String, String>): Call<ResponseBody>

}