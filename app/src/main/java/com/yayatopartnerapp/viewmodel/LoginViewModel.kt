package com.yayatopartnerapp.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.messaging.FirebaseMessaging
import com.yayatopartnerapp.R
import com.yayatopartnerapp.activities.ForgotPassAct
import com.yayatopartnerapp.activities.MobileLogin
import com.yayatopartnerapp.activities.SignUpAct
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.repository.LoginRepository
import com.yayatopartnerapp.utils.GPSTracker
import com.yayatopartnerapp.utils.InternetConnection
import com.yayatopartnerapp.utils.MyApplication
import kotlinx.android.synthetic.main.activity_login.*

class LoginViewModel(application: Application) : AndroidViewModel(application) ,Observable{
    val TAG = "LoginAct";
    var loginRepository: LoginRepository? = null
    var loginViewModel: LiveData<ModelLogin?>? = null
    var context: Context? = null
    lateinit var tracker: GPSTracker
    var currentLocation: LatLng? = null
    private lateinit var registerId: String
    @Bindable
    val email = MutableLiveData<String>()
    @Bindable
    val password = MutableLiveData<String>()


    fun init(context: Context) {
        this.context = context
        loginRepository  = LoginRepository(context)
        loginViewModel = loginRepository!!.getLognUserData()
        getCurrentLocation()
        getFirebaseToken()
    }


    fun loginApiCallViewModel(email : String,password : String,lat : String,lon : String,registerId : String) {
        loginRepository!!.loginUser(email,password,lat,lon,registerId)
    }

    fun getLoginDataViewModel(): LiveData<ModelLogin?>? {
        return loginViewModel
    }

    fun forgotClick(view: View){
        context!!.startActivity(Intent(context, ForgotPassAct::class.java))
    }

    fun signupClick(view : View){
        context!!.startActivity(Intent(context, SignUpAct::class.java))

    }

    fun signinOtpClick(view : View){
        context!!.startActivity(Intent(context, MobileLogin::class.java))

    }



    fun validation(){
        if (TextUtils.isEmpty(email.value)) {
            MyApplication.showAlert(context!!, context!!.getString(R.string.enter_email_text))
        } else if (TextUtils.isEmpty(password.value)) {
            MyApplication.showAlert(context!!, context!!.getString(R.string.please_enter_pass))
        } else {
            if (InternetConnection.checkConnection(context!!)) {
                loginApiCallViewModel(
                    email.value!!,
                    password.value!!,
                    currentLocation?.latitude.toString(),
                    currentLocation?.latitude.toString(),
                    registerId
                )
            } else {
                MyApplication.showConnectionDialog(context!!)
            }
        }

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

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


}