package com.yayatopartnerapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.yayatopartnerapp.R
import com.yayatopartnerapp.adapters.AdapterAllVehicle
import com.yayatopartnerapp.databinding.ActivityVehicalListBinding
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.models.ModelVehicalList
import com.yayatopartnerapp.utils.AppConstant
import com.yayatopartnerapp.utils.ProjectUtil
import com.yayatopartnerapp.utils.SharedPref
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VehicalListActivity : AppCompatActivity() {

    var mContext: Context = this@VehicalListActivity
    lateinit var binding: ActivityVehicalListBinding
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehical_list)
        binding.ivMenu.setOnClickListener { finish() }
        binding.btnUploadedVehicles.setOnClickListener {
            startActivity(Intent(mContext, AddVehicleAct::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        get_all_vehicleApi()
    }



    private fun get_all_vehicleApi() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.get_all_vehicle(
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
                        val modelVehicalList: ModelVehicalList =
                            Gson().fromJson(responseString, ModelVehicalList::class.java)
                        val adapterAllVehicle =
                            AdapterAllVehicle(mContext, modelVehicalList.getResult())
                        binding.recyclerView.adapter = adapterAllVehicle

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
}