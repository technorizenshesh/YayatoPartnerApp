package com.yayatopartnerapp.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.yayatopartnerapp.R
import com.yayatopartnerapp.databinding.ActivityAddVehicalOnRentBinding
import com.yayatopartnerapp.databinding.ActivityAddVehicleBinding
import com.yayatopartnerapp.models.ModelCarsType
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.models.ModelVehicalList
import com.yayatopartnerapp.utils.*
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddVehicalOnRentActivity : AppCompatActivity() {
    var mContext: Context = this@AddVehicalOnRentActivity
    lateinit var binding: ActivityAddVehicalOnRentBinding
    private var carId: String = ""

    var sharedPref: SharedPref? = null
    var modelLogin: ModelLogin? = null

    var taxiNamesList = ArrayList<String>()
    var taxiIdsList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_vehical_on_rent)
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref!!.getUserDetails(AppConstant.USER_DETAILS)
        binding.etStartDate.setOnClickListener {
            var cal = Calendar.getInstance()
            // create an OnDateSetListener
            val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                       dayOfMonth: Int) {
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "yyyy-MM-dd" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
//                    datecurr=sdf.format(cal.getTime())
                    binding.etStartDate!!.setText(sdf.format(cal.getTime()))

                }
            }
            DatePickerDialog(this,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()

        }
        binding.etEndDate.setOnClickListener {
            var cal = Calendar.getInstance()
            // create an OnDateSetListener
            val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                       dayOfMonth: Int) {
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "yyyy-MM-dd" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
//                    datecurr=sdf.format(cal.getTime())
                    binding.etEndDate!!.setText(sdf.format(cal.getTime()) )

                }
            }
            DatePickerDialog(this,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()

        }

        binding.etStartTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker = TimePickerDialog(
                this,
                { view, hourOfDay, minute ->
                    mcurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mcurrentTime.set(Calendar.MINUTE, minute);
                    var simpleDateFormat = SimpleDateFormat("hh:mm a")
                    var finaldate = simpleDateFormat.format(mcurrentTime.time)
                    binding.etStartTime.setText(finaldate)
                }, hour, minute, false
            )
            mTimePicker.show()
        }

        binding.etEndTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker = TimePickerDialog(this, { view, hourOfDay, minute ->
                mcurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mcurrentTime.set(Calendar.MINUTE, minute);
                var simpleDateFormat = SimpleDateFormat("hh:mm a")
                var finaldate = simpleDateFormat.format(mcurrentTime.time)
                binding.etEndTime.setText(finaldate)
            }, hour, minute, false
            )
            mTimePicker.show()
        }

        binding.ivBack.setOnClickListener { finish() }

        binding.spinnerServiceType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    try {
                        carId = taxiIdsList[position]
                        Log.e("getCarsgetCars", "carId = $carId")
                    } catch (e: Exception) {
                        Log.e("ExceptionException", "Exception = " + e.message)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.btnSubmit.setOnClickListener {
            if(carId.equals("")){
                MyApplication.showAlert(mContext, "Please add vehicle")
            } else if (TextUtils.isEmpty(binding.etNumberPlate.text.toString().trim())) {
                MyApplication.showAlert(mContext, "Please add rate per km")
            } else if (TextUtils.isEmpty(binding.etStartDate.text.toString().trim())) {
                MyApplication.showAlert(mContext, "please select start date")
//            } else if (TextUtils.isEmpty(binding.etEndDate.text.toString().trim())) {
//                MyApplication.showAlert(mContext, "please select start date")
//            } else if (TextUtils.isEmpty(binding.etStartTime.text.toString().trim())) {
//                MyApplication.showAlert(mContext, getString(R.string.enter_start_time))
//            } else if (TextUtils.isEmpty(binding.etEndTime.text.toString().trim())) {
//                MyApplication.showAlert(mContext, getString(R.string.enter_end_time))
            }else{
                if (InternetConnection.checkConnection(mContext)) {
                    addVehicle()
                } else {
                    MyApplication.showConnectionDialog(mContext)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getCars()
    }

    private fun addVehicle() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))

//        val vehicleFilePart: MultipartBody.Part
//
//        val userId: RequestBody =
//            RequestBody.create(MediaType.parse("text/plain"), modelLogin!!.getResult()!!.id)
//        val carType = RequestBody.create(MediaType.parse("text/plain"), carId)
//        val carBrand = RequestBody.create(MediaType.parse("text/plain"), makeId)
//        val carModel = RequestBody.create(MediaType.parse("text/plain"), modelId)
//        val startTime = RequestBody.create(
//            MediaType.parse("text/plain"),
//            binding.etStartTime.text.toString().trim()
//        )
//        val endTime = RequestBody.create(
//            MediaType.parse("text/plain"),
//            binding.etEndTime.text.toString().trim()
//        )
//        val address = RequestBody.create(
//            MediaType.parse("text/plain"),
//            binding.etAdd1.text.toString().trim()
//        )
//        val lat = RequestBody.create(
//            MediaType.parse("text/plain"),
//            latLng?.latitude.toString().trim()
//        )
//        val lon = RequestBody.create(
//            MediaType.parse("text/plain"),
//            latLng?.longitude.toString().trim()
//        )
//        val year = RequestBody.create(
//            MediaType.parse("text/plain"),
//            binding.spYear.selectedItem.toString().trim()
//        )
//        val color: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")
//
//        val carNumber = RequestBody.create(MediaType.parse("text/plain"), binding.etNumberPlate.text.toString().trim())
//
//        vehicleFilePart = MultipartBody.Part.createFormData("car_image", vehicleImage!!.name,
//            RequestBody.create(MediaType.parse("car_document/*"), vehicleImage)
//        )

        val api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.add_car_patnar_request(
            carId, "", binding.etNumberPlate.text.toString(), binding.etStartDate.text.toString(),

            binding.etStartTime.text.toString(), binding.etEndDate.text.toString(), binding.etEndTime.text.toString(), modelLogin?.getResult()?.id.toString(),"Pending")
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    Log.e("vehicleData", "responseString = $responseString")
                    val jsonObject = JSONObject(responseString)
                    Log.e("vehicleData", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
                        finish()
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


    private fun getCars() {
        ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.get_all_vehicle(modelLogin?.getResult()?.id.toString())
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val stringResponse = response.body()!!.string()
                    try {
                        val jsonObject = JSONObject(stringResponse)
                        if (jsonObject.getString("status") == "1") {
                            val modelVehicalList: ModelVehicalList =
                                Gson().fromJson(stringResponse, ModelVehicalList::class.java)
                            for (item in modelVehicalList.getResult()!!) {
                                taxiNamesList.add(item.car_name!!+" "+item.brand_name!!)
                                taxiIdsList.add(item.id!!)
                            }
                            val arrayAdapter = ArrayAdapter(
                                mContext,
                                R.layout.support_simple_spinner_dropdown_item,
                                taxiNamesList
                            )
                            binding.spinnerServiceType.setAdapter(arrayAdapter)
                            carId = taxiNamesList[0]
                            Log.e("getCarsgetCars", "response = $stringResponse")
                            Log.e("getCarsgetCars", "carId = $carId")
                        } else {
                            Toast.makeText(mContext, getString(R.string.no_data_found), Toast.LENGTH_LONG).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
            }
        })

    }
}