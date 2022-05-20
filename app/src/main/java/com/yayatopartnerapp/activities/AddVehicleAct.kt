package com.yayatopartnerapp.activities

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.yayatopartnerapp.R
import com.yayatopartnerapp.databinding.ActivityAddVehicleBinding
import com.yayatopartnerapp.models.ModelCarsType
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.models.ModelMake
import com.yayatopartnerapp.utils.*
import com.yayatopartnerapp.utils.ProjectUtil.Companion.checkPermissions
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddVehicleAct : AppCompatActivity() {

    var mContext: Context = this@AddVehicleAct
    lateinit var binding: ActivityAddVehicleBinding
    var PERMISSION_ID: Int = 101
    private var AUTOCOMPLETE_REQUEST_CODE: Int = 1010
    private var latLng: LatLng? = null

    private val GALLERY = 0
    private val CAMERA = 1
    var sharedPref: SharedPref? = null
    var modelLogin: ModelLogin? = null
    private var str_image_path: String? = null
    var taxiNamesList = ArrayList<String>()
    var taxiIdsList = ArrayList<String>()
    var makeNameList = ArrayList<String>()
    var makeIdList = ArrayList<String>()
    var modelNameList = ArrayList<String>()
    var modelIdList = ArrayList<String>()
    private var carId: String? = null
    private var makeId: String = ""
    private var modelId: String = ""
    var vehicleImage: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_vehicle)
        // Setting up the flag programmatically so that the
        // Device screen should be always on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref!!.getUserDetails(AppConstant.USER_DETAILS)
        Log.e("gfsfasfasfas", "modelLogin.getResult().getId() = " + modelLogin!!.getResult()!!.id)
        itit()
    }

    private fun itit() {

        getCars()

        etAdd1.setOnClickListener {
            val fields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
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

        binding.ivUploadImage.setOnClickListener {
            if (checkPermissions(mContext)) {
                showPictureDialog()
            } else {
                requestPermissions()
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (carId == null) {
                MyApplication.showAlert(mContext, getString(R.string.select_vehicle_type))
            } else if (TextUtils.isEmpty(binding.etNumberPlate.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.enter_number_plate))
            } else if (vehicleImage == null) {
                MyApplication.showAlert(mContext, getString(R.string.upload_vehicle_text))
            } else if (binding.spYear.selectedItemPosition === 0) {
                MyApplication.showAlert(mContext, getString(R.string.add_year_vehicle_text))
//            } else if (TextUtils.isEmpty(binding.etAdd1.text.toString().trim())) {
//                MyApplication.showAlert(mContext, getString(R.string.enter_address1_text))
//            } else if (TextUtils.isEmpty(binding.etStartTime.text.toString().trim())) {
//                MyApplication.showAlert(mContext, getString(R.string.enter_start_time))
//            } else if (TextUtils.isEmpty(binding.etEndTime.text.toString().trim())) {
//                MyApplication.showAlert(mContext, getString(R.string.enter_end_time))
            } else {
                if (InternetConnection.checkConnection(mContext)) {
                    addVehicle()
                } else {
                    MyApplication.showConnectionDialog(mContext)
                }
            }
        }

    }

    private fun addVehicle() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))

        val vehicleFilePart: MultipartBody.Part

        val userId: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), modelLogin!!.getResult()!!.id)
        val carType = RequestBody.create(MediaType.parse("text/plain"), carId)
        val carBrand = RequestBody.create(MediaType.parse("text/plain"), makeId)
        val carModel = RequestBody.create(MediaType.parse("text/plain"), modelId)
        val startTime = RequestBody.create(
            MediaType.parse("text/plain"),
            binding.etStartTime.text.toString().trim()
        )
        val endTime = RequestBody.create(
            MediaType.parse("text/plain"),
            binding.etEndTime.text.toString().trim()
        )
        val address = RequestBody.create(
            MediaType.parse("text/plain"),
            binding.etAdd1.text.toString().trim()
        )
        val lat = RequestBody.create(
            MediaType.parse("text/plain"),
            latLng?.latitude.toString().trim()
        )
        val lon = RequestBody.create(
            MediaType.parse("text/plain"),
            latLng?.longitude.toString().trim()
        )
        val year = RequestBody.create(
            MediaType.parse("text/plain"),
            binding.spYear.selectedItem.toString().trim()
        )
        val color: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")

        val carNumber = RequestBody.create(MediaType.parse("text/plain"), binding.etNumberPlate.text.toString().trim())

        vehicleFilePart = MultipartBody.Part.createFormData("car_image", vehicleImage!!.name,
            RequestBody.create(MediaType.parse("car_document/*"), vehicleImage)
        )

        val api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.addMultiVehicleApiCall(
            userId, carType, carBrand, carModel,

            carNumber, year, color, startTime, endTime, address,lat,lon,vehicleFilePart)
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

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(mContext)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> {
                    val galleryIntent = Intent(
                        Intent.ACTION_GET_CONTENT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    galleryIntent.type = "image/*"
                    startActivityForResult(galleryIntent, GALLERY)
                }
                1 -> {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (cameraIntent.resolveActivity(mContext.packageManager) != null)
                        startActivityForResult(cameraIntent, CAMERA)
                }
            }
        }
        pictureDialog.show()
    }

    private fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PERMISSION_ID
        )
    }

    private fun getCars() {
        ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait))
        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.getCarList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val stringResponse = response.body()!!.string()
                    try {
                        val jsonObject = JSONObject(stringResponse)
                        if (jsonObject.getString("status") == "1") {
                            val modelCarsType: ModelCarsType =
                                Gson().fromJson(stringResponse, ModelCarsType::class.java)
                            for (item in modelCarsType.getResult()!!) {
                                taxiNamesList.add(item.car_name!!)
                                taxiIdsList.add(item.id!!)
                            }
                            val arrayAdapter = ArrayAdapter(
                                mContext,
                                R.layout.support_simple_spinner_dropdown_item,
                                taxiNamesList
                            )
                            binding.spinnerServiceType.setAdapter(arrayAdapter)
                            carId = taxiNamesList[0]
                            getMake()
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

    private fun getMake() {
        val api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.getCarMakeList()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val stringResponse = response.body()!!.string()
                    try {
                        val jsonObject = JSONObject(stringResponse)
                        if (jsonObject.getString("status") == "1") {
                            val modelCarsType: ModelMake =
                                Gson().fromJson(stringResponse, ModelMake::class.java)
                            for (item in modelCarsType.getResult()!!) {
                                makeNameList.add(item.title!!)
                                makeIdList.add(item.id!!)
                            }
                            val arrayAdapter = ArrayAdapter(
                                mContext,
                                R.layout.support_simple_spinner_dropdown_item,
                                makeNameList
                            )
                            binding.spMakeType.setAdapter(arrayAdapter)
                            makeId = makeIdList[0]
                            binding.spMakeType.setOnItemSelectedListener(object :
                                AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View,
                                    position: Int,
                                    id: Long
                                ) {
                                    try {
                                        makeId = makeIdList[position]
                                        getModels(makeId)
                                        Log.e("getMake", "makeId = $makeId")
                                    } catch (e: Exception) {
                                        Log.e("ExceptionException", "Exception = " + e.message)
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            })

                            // getModels(makeId);
                            Log.e("getMake", "response getMake = $stringResponse")
                            Log.e("getMake", "makeId getMake = $makeId")
                        } else {
                            Toast.makeText(
                                mContext,
                                getString(R.string.no_data_found),
                                Toast.LENGTH_LONG
                            ).show()
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

    private fun getModels(makeId: String) {
        // ProjectUtil.showProgressDialog(mContext, true, getString(R.string.please_wait));
        val api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val param = HashMap<String, String>()
        param["make_id"] = makeId
        val call: Call<ResponseBody> = api.getCarModelCall(param)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val stringResponse = response.body()!!.string()
                    try {
                        val jsonObject = JSONObject(stringResponse)
                        if (jsonObject.getString("status") == "1") {
                            val modelCarsType: ModelMake =
                                Gson().fromJson(stringResponse, ModelMake::class.java)
                            modelNameList = ArrayList()
                            modelIdList = ArrayList()
                            for (item in modelCarsType.getResult()!!) {
                                modelNameList.add(item.title!!)
                                modelIdList.add(item.id!!)
                            }
                            val arrayAdapter = ArrayAdapter(
                                mContext,
                                R.layout.support_simple_spinner_dropdown_item,
                                modelNameList
                            )
                            binding.spModelType.adapter = arrayAdapter
                            modelId = modelIdList[0]
                            binding.spModelType.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View,
                                        position: Int,
                                        id: Long
                                    ) {
                                        try {
                                            modelId = modelIdList[position]
                                            Log.e("getCarsgetCars", "modelId = $modelId")
                                        } catch (e: Exception) {
                                            Log.e("ExceptionException", "Exception = " + e.message)
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                                }
                            Log.e("getModels", "response = $stringResponse")
                            Log.e("getModels", "modelId = $modelId")
                        } else {
                            Toast.makeText(
                                mContext,
                                getString(R.string.no_data_found),
                                Toast.LENGTH_LONG
                            ).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                latLng = place.latLng
                try {
                    val addresses = ProjectUtil.getCompleteAddressString(
                        mContext, place.latLng!!.latitude, place.latLng!!.longitude
                    )
                    etAdd1.setText(addresses)
                } catch (e: Exception) {
                }
            }
        } else if (requestCode == GALLERY) {
            if (resultCode == RESULT_OK) {
                val path: String = RealPathUtil.getRealPath(mContext, data!!.data)!!
                Log.e("pathpathpathpath","path = " + path);
                vehicleImage = File(path)
                binding.ivUploadImage.setImageURI(Uri.parse(path))
            }
        } else if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                try {
                    if (data != null) {
                        val extras = data.extras
                        val bitmapNew = extras!!["data"] as Bitmap
                        val imageBitmap: Bitmap =
                            BITMAP_RE_SIZER(bitmapNew, bitmapNew!!.width, bitmapNew!!.height)!!
                        val tempUri: Uri = ProjectUtil.getImageUri(mContext, imageBitmap)!!
                        val image = RealPathUtil.getRealPath(mContext, tempUri)
                        vehicleImage = File(image)

                        Log.e("sgfsfdsfs", "image = $image")
                        binding.ivUploadImage.setImageURI(Uri.parse(image))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun BITMAP_RE_SIZER(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
        val ratioX = newWidth / bitmap.width.toFloat()
        val ratioY = newHeight / bitmap.height.toFloat()
        val middleX = newWidth / 2.0f
        val middleY = newHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bitmap,
            middleX - bitmap.width / 2,
            middleY - bitmap.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )
        return scaledBitmap
    }

}
