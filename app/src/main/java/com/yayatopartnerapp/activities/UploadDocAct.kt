package com.yayatopartnerapp.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.yayatopartnerapp.R
import com.yayatopartnerapp.databinding.ActivityUploadDocBinding
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.utils.*
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_upload_doc.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadDocAct : AppCompatActivity() {

    private var imageCapturedCode: Int = 0
    var mContext: Context = this@UploadDocAct
    lateinit var binding: ActivityUploadDocBinding
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    private val GALLERY = 0
    private val CAMERA = 1
    var lisenceFile: File? = null
    var registrationFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_doc)
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        itit()
    }

    private fun itit() {

        binding.btnSubmit.setOnClickListener {
            if (lisenceFile == null) {
                MyApplication.showAlert(mContext, "Please upload your driving lisence copy")
            } else if (registrationFile == null) {
                MyApplication.showAlert(mContext, "Please upload CNI of vehicle")
            } else {
                if(InternetConnection.checkConnection(mContext)) {
                    uploadVehicleApi()
                } else {
                    MyApplication.showConnectionDialog(mContext)
                }
            }
        }

        ivDriverLicense.setOnClickListener {
            imageCapturedCode = 1
            if (ProjectUtil.checkPermissions(mContext)) {
                showPictureDialog()
            } else {
                ProjectUtil.requestPermissions(mContext)
            }
        }

        ivRegistrationCertify.setOnClickListener {
            imageCapturedCode = 2
            if (ProjectUtil.checkPermissions(mContext)) {
                showPictureDialog()
            } else {
                ProjectUtil.requestPermissions(mContext)
            }
        }

    }

    private fun uploadVehicleApi() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))
        val drivingLisencePart: MultipartBody.Part
        val registPart: MultipartBody.Part

        val userId = RequestBody.create(MediaType.parse("text/plain"), modelLogin.getResult()!!.id)

        drivingLisencePart = MultipartBody.Part.createFormData(
            "driver_lisence",
            lisenceFile!!.name,
            RequestBody.create(MediaType.parse("car_document/*"), lisenceFile)
        )

        registPart = MultipartBody.Part.createFormData(
            "cni_image",
            lisenceFile!!.name,
            RequestBody.create(MediaType.parse("car_document/*"), registrationFile)
        )

        val api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.uploadDriverDocCallApi(userId, drivingLisencePart, registPart)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("driversignup", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {
                        modelLogin = Gson().fromJson(responseString, ModelLogin::class.java)
                        sharedPref.setBooleanValue(AppConstant.IS_REGISTER, true)
                        modelLogin.getResult()!!.step = "2"
                        sharedPref.setUserDetails(AppConstant.USER_DETAILS, modelLogin)
                        startActivity(Intent(mContext, HomeAct::class.java))
                        finish()
                    } else {
                        MyApplication.showAlert(mContext, getString(R.string.user_already_exits))
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

    private fun setImageFromCameraGallery(file: File) {
        Log.e("setImageFromCameraGallery", "file = " + file.path)
        if (imageCapturedCode == 1) {
            lisenceFile = file
            binding.ivDriverLicense.setImageURI(Uri.parse(file.path))
        } else if (imageCapturedCode == 2) {
            registrationFile = file
            binding.ivRegistrationCertify.setImageURI(Uri.parse(file.path))
        }
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(mContext)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (resultCode == RESULT_OK) {
                val path: String = RealPathUtil.getRealPath(mContext, data!!.data)!!
                setImageFromCameraGallery(File(path));
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
                        setImageFromCameraGallery(File(image));
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