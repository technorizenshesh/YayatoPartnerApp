package com.yayatotaxi.utils.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Api {


    @FormUrlEncoded
    @POST("get_model")
    fun getCarModelCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @POST("car_list")
    fun getCarList(): Call<ResponseBody>

    @POST("get_make")
    fun getCarMakeList(): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_my_transaction")
    fun getTransactionApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>


    @FormUrlEncoded
    @POST("wallet_transfer")
    fun walletTransferApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("add_wallet")
    fun addWalletApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>


    @FormUrlEncoded
    @POST("forgot_password")
    fun forgotPass(@FieldMap params: Map<String, String>): Call<ResponseBody>


    @FormUrlEncoded
    @POST("get_all_vehicle")
    fun get_all_vehicle(@Field("user_id") user_id: String): Call<ResponseBody>


    @Multipart
    @POST("signup")
    fun signUpDriverCallApi(
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

    @Multipart
    @POST("update_parter_doc")
    fun uploadDriverDocCallApi(
        @Part("id") id: RequestBody,
        @Part driverlisence: MultipartBody.Part,
        @Part cni_image: MultipartBody.Part
    ): Call<ResponseBody>

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

    @Multipart
    @POST("add_vehicle")
    fun addMultiVehicleApiCall(
        @Part("user_id") user_id: RequestBody,
        @Part("car_type_id") car_type: RequestBody,
        @Part("brand") car_brand: RequestBody,
        @Part("car_model") car_model: RequestBody,
        @Part("car_number") carNumber: RequestBody,
        @Part("year_of_manufacture") year_of_manufacture: RequestBody,
        @Part("car_color") car_color: RequestBody,
        @Part("start_time") start_time: RequestBody,
        @Part("end_time") end_time: RequestBody,
        @Part("address") address: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part file1: MultipartBody.Part
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("update_car_request_status")
    fun update_car_request_status(
        @Field("id") id: String,
        @Field("status") status: String
    ): Call<ResponseBody>

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
    @POST("get_car_patnar_request")
    fun get_car_patnar_request(
        @Field("user_id") user_id: String ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("get_profile")
    fun getProfileCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("login")
    fun loginApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>


    @FormUrlEncoded
    @POST("get_car_detail_request")
    fun get_car_booking_request(
        @Field("partner_id") partner_id: String ): Call<ResponseBody>





    @FormUrlEncoded
    @POST("update_lat_lon")
    fun updateLocation(@FieldMap params: Map<String, String>): Call<Map<String, String>>


    @FormUrlEncoded
    @POST("get_booking_history")
    fun getTaxiHistory(@FieldMap params: Map<String, String>): Call<ResponseBody>



    @FormUrlEncoded
    @POST("change_password")
    fun changePass(@FieldMap params: Map<String, String>): Call<ResponseBody>


    @Multipart
    @POST("add_vehicle")
    fun addDriverVehicle(
        @Part("user_id") user_id: RequestBody,
        @Part("car_type_id") car_type: RequestBody,
        @Part("brand") car_brand: RequestBody,
        @Part("car_model") car_model: RequestBody,
        @Part("car_number") carNumber: RequestBody,
        @Part("year_of_manufacture") year_of_manufacture: RequestBody,
        @Part file1: MultipartBody.Part
    ): Call<ResponseBody>


    @FormUrlEncoded
    @POST("update_online_status")
    fun updateOnOffApi(@FieldMap params: Map<String, String>): Call<ResponseBody>



    @FormUrlEncoded
    @POST("available_car_driver")
    fun getAvailableDrivers(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_available_driver")
    fun getAvailableCarDriversHome(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_car_type_list")
    fun getCarTypeListApi(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_dev_order")
    fun getDevOrdersApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("social_login")
    fun socialLogin(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("add_bank_account")
    fun addBankAccount(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("driver_accept_and_Cancel_request")
    fun acceptCancelOrderCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_current_booking")
    fun getCurrentTaxiBooking(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("booking_request")
    fun bookingRequestApi(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("driver_accept_and_Cancel_request")
    fun acceptCancelOrderCallTaxi(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("place_order")
    fun placeDevOrderApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("signup")
    fun signUpApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("available_car_driver")
    fun getAvailableCarCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("add_to_cart")
    fun updateOrderStatusApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_count_cart")
    fun getCartCountApiCall(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_current_booking")
    fun getCurrentBooking(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("get_lat_lon")
    fun getLatLonDriver(@FieldMap params: Map<String, String>): Call<ResponseBody>

    @Multipart
    @POST("add_document")
    fun addDriverDocumentApiCall(
        @Part("user_id") user_id: RequestBody,
        @Part file1: MultipartBody.Part,
        @Part file2: MultipartBody.Part,
        @Part file3: MultipartBody.Part
    ): Call<ResponseBody>










}