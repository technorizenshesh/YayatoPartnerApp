package com.yayatopartnerapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.yayatopartnerapp.R
import com.yayatopartnerapp.adapters.AdapterAllVehicle
import com.yayatopartnerapp.adapters.AdapterHomeVehicle
import com.yayatopartnerapp.databinding.ActivityHomeBinding
import com.yayatopartnerapp.models.BookingModel
import com.yayatopartnerapp.models.ModelCarsRent
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.models.ModelVehicalList
import com.yayatopartnerapp.utils.AppConstant
import com.yayatopartnerapp.utils.GPSTracker
import com.yayatopartnerapp.utils.ProjectUtil
import com.yayatopartnerapp.utils.SharedPref
import com.yayatopartnerapp.utils.retrofit.ApiClient
import com.yayatopartnerapp.utils.retrofit.YayatoApiService
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class HomeAct : AppCompatActivity() {

    var mContext: Context = this@HomeAct
    lateinit var binding: ActivityHomeBinding
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    var currentLocation: LatLng? = null
    lateinit var tracker: GPSTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        itit()
    }


    private fun itit() {

        binding.childNavDrawer.btnEditProfile.setOnClickListener {
            startActivity(Intent(mContext, UpdateProfielAct::class.java))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.chlidDashboard.ivMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.childNavDrawer.btnHistory.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)

        }


//        binding.chlidDashboard.btnShowDrivers.setOnClickListener {
//            startActivity(Intent(mContext, DriverRequestsAct::class.java))
//            binding.drawerLayout.closeDrawer(GravityCompat.START)
//        }

        binding.childNavDrawer.btnSignout.setOnClickListener {
            ProjectUtil.logoutAppDialog(mContext)
        }


        binding.chlidDashboard.btnUploadedVehicles.setOnClickListener {
            startActivity(Intent(mContext, AddVehicalOnRentActivity::class.java))
        }
/*
        binding.childNavDrawer.btnWallet.setOnClickListener {
            startActivity(Intent(mContext, WalletAct::class.java))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.childNavDrawer.btnUploadedVehicles.setOnClickListener {
            startActivity(Intent(mContext, VehicalListActivity::class.java))

            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
*/

    }

    override fun onResume() {
        super.onResume()
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        tracker = GPSTracker(mContext)
        currentLocation = LatLng(tracker.latitude, tracker.longitude)
        Glide.with(mContext).load(modelLogin.getResult()?.image)
            .error(R.drawable.user_ic)
            .placeholder(R.drawable.user_ic)
            .into(binding.childNavDrawer.userImg)
        binding.childNavDrawer.tvUsername.setText(modelLogin.getResult()?.user_name)
        binding.childNavDrawer.tvEmail.setText(modelLogin.getResult()?.email)
        Log.e("sfasdasdas","modelLogin.getResult()?.image = " + modelLogin.getResult()?.image)


        get_all_vehicleApi()
        update_lat_lonApi()
    }



    private fun get_all_vehicleApi() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.get_car_booking_request(
            modelLogin.getResult()?.id.toString()
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("CurrentTaxiBooking", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
                        binding.chlidDashboard.tvNotFound.visibility = View.GONE
                        val modelCarsRent: BookingModel =
                            Gson().fromJson(responseString, BookingModel::class.java)
                        val adapterHomeVehicle =
                            AdapterHomeVehicle(mContext, modelCarsRent.getResult())
                        binding.chlidDashboard.recyclerView.adapter = adapterHomeVehicle

                    }
                    else{
                        binding.chlidDashboard.tvNotFound.visibility = View.VISIBLE
                        val adapterHomeVehicle =
                            AdapterHomeVehicle(mContext, null)
                        binding.chlidDashboard.recyclerView.adapter = adapterHomeVehicle

                    }

                } catch (e: Exception) {
                    Toast.makeText(mContext, "Exception = " + e.message, Toast.LENGTH_SHORT).show()
                    Log.e("Exception", "Exception = " + e.message)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
                Log.e("Exception", "Throwable = " + t.message)
            }

        })
    }


    private fun update_lat_lonApi() {
        val call: Call<ResponseBody> = ApiClient.getClient(mContext)!!.create(YayatoApiService::class.java).update_lat_lon(
            modelLogin.getResult()?.id.toString(),
            currentLocation?.latitude.toString(),
            currentLocation?.longitude.toString()
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("update_lat_lon", "responseString = $responseString")
//                    if (jsonObject.getString("status") == "1") {
//                        Toast.makeText(applicationContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
//                    }

                } catch (e: Exception) {
                    Log.e("Exception", "Exception = " + e.message)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Throwable", "Throwable = " + t.message)
            }
        })
    }





}