package com.yayatopartnerapp.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.yayatopartnerapp.R
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.models.ModelVehicalList
import com.yayatopartnerapp.utils.AppConstant
import com.yayatopartnerapp.utils.ProjectUtil
import com.yayatopartnerapp.utils.retrofit.ApiClient
import com.yayatopartnerapp.utils.retrofit.YayatoApiService
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddVehicleRepository {
    val TAG = "AddVehicleRepository"
    var context : Context? = null
    var apiInterface : YayatoApiService
    private var vehicleListMutableLiveData: MutableLiveData<ModelVehicalList>? = null
    private var aaddVehicleMutableLiveData: MutableLiveData<ResponseBody>? = null



    constructor(context: Context){
        this.context = context
        apiInterface = ApiClient.getClient(context)!!.create(YayatoApiService::class.java)
        vehicleListMutableLiveData = MutableLiveData()
        aaddVehicleMutableLiveData = MutableLiveData()

    }


    fun vehicleListRepo(userId : String){
        ProjectUtil.showProgressDialog(context, false, context?.getString(R.string.please_wait))
        var map = HashMap<String, String>()
        map.put("user_id",userId)
        Log.e(TAG, "Vehicle List Request = $map")
        apiInterface.get_all_vehicle(userId).enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    if (response.body() != null) {
                        val responseString = response.body()!!.string()
                        val jsonObject = JSONObject(responseString)
                        var vehicleModel = Gson().fromJson(responseString, ModelVehicalList::class.java)
                        Log.e(TAG, "Vehicle List response = $responseString")
                        if (jsonObject.getString("status") == "1") {
                            vehicleListMutableLiveData!!.postValue(vehicleModel)
                        } else {
                            vehicleListMutableLiveData!!.postValue(vehicleModel)
                        }

                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Exception = " + e.message, Toast.LENGTH_SHORT).show()
                    Log.e("Exception", "Exception = " + e.message)
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
            }

        })

    }

    fun getVehicleListRepo(): LiveData<ModelVehicalList?>? {
        return vehicleListMutableLiveData
    }

    fun addVehicleRepo(carId : String,basePrice : String,priceKm : String,startDate : String,startTime : String,
                       endDate : String,endTime : String,userId : String,status : String){
        ProjectUtil.showProgressDialog(context, false, context?.getString(R.string.please_wait))
        var map = HashMap<String, String>()
        map.put("user_id",userId)
        Log.e(TAG, "Add Vehicle Request = $map")
        apiInterface.add_car_patnar_request(carId,basePrice,priceKm,startDate,startTime,
            endDate,endTime,userId,status).enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    if (response.body() != null) {
                        val responseString = response.body()!!.string()
                        val jsonObject = JSONObject(responseString)
                        Log.e(TAG, "Add Vehicle response = $responseString")
                        if (jsonObject.getString("status") == "1") {
                            aaddVehicleMutableLiveData!!.postValue(response.body())
                        } else {
                            aaddVehicleMutableLiveData!!.postValue(response.body())
                        }

                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Exception = " + e.message, Toast.LENGTH_SHORT).show()
                    Log.e("Exception", "Exception = " + e.message)
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
            }

        })

    }

    fun getAddVehicleRepo(): LiveData<ResponseBody?>? {
        return aaddVehicleMutableLiveData
    }



}