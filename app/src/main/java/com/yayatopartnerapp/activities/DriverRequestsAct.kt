package com.yayatopartnerapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.yayatopartnerapp.R
import com.yayatopartnerapp.adapters.AdapterVehicleRequest
import com.yayatopartnerapp.databinding.ActivityDriverRequestsBinding
import com.yayatopartnerapp.listener.RentCarRequestListener
import com.yayatopartnerapp.models.ModelCarsRent
import com.yayatopartnerapp.utils.ProjectUtil
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverRequestsAct : AppCompatActivity(), RentCarRequestListener {

    var mContext: Context = this@DriverRequestsAct
    lateinit var binding: ActivityDriverRequestsBinding
    lateinit var data: ModelCarsRent.Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_requests)
        data = intent.getSerializableExtra("model") as ModelCarsRent.Result


        val adapterVehicleRequest =
            AdapterVehicleRequest(mContext, data.request_detailss)
        adapterVehicleRequest.selListener(this)

        binding.recyclerView.adapter = adapterVehicleRequest

        if (data.request_detailss?.size!! > 0) {
            binding.tvNotFound.visibility= View.GONE

        } else {
            binding.tvNotFound.visibility= View.VISIBLE

        }

        inIt()
    }

    private fun inIt() {

        binding.ivBack.setOnClickListener { finish() }

//        binding.btnAccept.setOnClickListener { finish() }
//
//        binding.btnCancel.setOnClickListener { finish() }


    }

    private fun update_car_request_statusApi(id:String,status:String) {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.update_car_request_status(
            id,
            status
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("CurrentTaxiBooking", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
//                        val modelVehicalList: ModelVehicalList =
//                            Gson().fromJson(responseString, ModelVehicalList::class.java)
//                        val adapterAllVehicle =
//                            AdapterAllVehicle(mContext, modelVehicalList.getResult())
//                        binding.chlidDashboard.recyclerView.adapter = adapterAllVehicle
                        finish();

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

    override fun onClick(
        poolDetails: ModelCarsRent.Result.RequestDetailss,
        status: String,
        position: Int
    ) {
        update_car_request_statusApi(poolDetails?.id!!,status)
    }


}