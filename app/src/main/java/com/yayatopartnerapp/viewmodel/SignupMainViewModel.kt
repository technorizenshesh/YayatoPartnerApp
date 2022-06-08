package com.yayatopartnerapp.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.widget.Autocomplete
import com.google.firebase.messaging.FirebaseMessaging
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.repository.SignupRepository
import com.yayatopartnerapp.utils.GPSTracker
import com.yayatopartnerapp.utils.ProjectUtil
import com.yayatopartnerapp.utils.RealPathUtil
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.File

open class SignupMainViewModel (application: Application ) : AndroidViewModel(application) ,
    Observable {
    val TAG = "SignupAct";
    var context: Context? = null

    lateinit var tracker: GPSTracker
    var currentLocation: LatLng? = null
    private lateinit var registerId: String



    @Bindable
    val email = MutableLiveData<String>()
    @Bindable
    val password = MutableLiveData<String>()

    @Bindable
    var picImage = MutableLiveData<String>()





    private var AUTOCOMPLETE_REQUEST_CODE: Int = 101
    private val GALLERY = 0;
    private val CAMERA = 1;
    var profileImage: File? = null


    fun init(context: Context) {
        this.context = context
        getFirebaseToken()
    }



    fun getCurrentLocation(){
        tracker = GPSTracker(context!!)
        currentLocation = LatLng(tracker.latitude, tracker.longitude)
    }


    fun getFirebaseToken(){
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                registerId = token
                Log.e(TAG, "token = " + registerId)
            }
        }

    }
















/*
  override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                latLng = place.latLng
                try {
                    val addresses = ProjectUtil.getCompleteAddressString(
                        context, place.latLng!!.latitude, place.latLng!!.longitude
                    )
                    etAdd1.setText(addresses)
                } catch (e: Exception) {
                }
            }
        } else if (requestCode == GALLERY) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val path: String = RealPathUtil.getRealPath(context!!, data!!.data)!!
                profileImage = File(path)
                //profileImageSet.setImageURI(Uri.parse(path))
                picImage = MutableLiveData(path)
            }
        } else if (requestCode == CAMERA) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                try {
                    if (data != null) {
                        val extras = data.extras
                        val bitmapNew = extras!!["data"] as Bitmap
                        val imageBitmap: Bitmap =
                            BITMAP_RE_SIZER(bitmapNew, bitmapNew!!.width, bitmapNew!!.height)!!
                        val tempUri: Uri = ProjectUtil.getImageUri(context!!, imageBitmap)!!
                        val image = RealPathUtil.getRealPath(context!!, tempUri)
                        profileImage = File(image)
                        Log.e("sgfsfdsfs", "profileImage = $profileImage")
                       // profileImageSet.setImageURI(Uri.parse(image))
                        picImage = MutableLiveData(image);
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
*/






    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }





}