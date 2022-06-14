package com.yayatopartnerapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.yayatopartnerapp.R
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.utils.AppConstant
import com.yayatopartnerapp.utils.MyApplication
import com.yayatopartnerapp.utils.ProjectUtil
import com.yayatopartnerapp.utils.retrofit.ApiClient
import com.yayatopartnerapp.utils.retrofit.YayatoApiService
import kotlinx.android.synthetic.main.activity_mobile_login.*
import kotlinx.android.synthetic.main.activity_mobile_login.etPhone
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MobileLogin : AppCompatActivity() {
    var TAG = "SignUpAct"
    var mContext: Context = this@MobileLogin
    lateinit var apiInterface: YayatoApiService
    private lateinit var registerId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiInterface = ApiClient.getClient(mContext)!!.create(YayatoApiService::class.java)
        setContentView(R.layout.activity_mobile_login)
        getFirebaseToken()
        initViews()
    }

    private fun initViews() {
        ivBack1.setOnClickListener { finish() }


        btnSendOtp.setOnClickListener {
            validation()
        }
    }

    private fun validation() {
        if (TextUtils.isEmpty(etPhone.text.toString().trim())) {
            Toast.makeText(mContext, getString(R.string.enter_phone_text), Toast.LENGTH_SHORT)
                .show()
        } else {
            loginwithOtp()
        }
    }

    private fun loginwithOtp() {
        ProjectUtil.showProgressDialog(mContext, false, mContext?.getString(R.string.please_wait))
        var map = HashMap<String, String>()
        map.put("email", "")
        map.put("password", "")
        map.put("lat", "")
        map.put("lon", "")
        map.put("type", AppConstant.PARTNER)
        map.put("register_id", registerId)
        map.put("login_type", "otp")
       // map.put("number", "+91" + etPhone.text.toString())
        map.put("number", "+91" + etPhone.text.toString())
        Log.e(TAG, "Login with OTP Request = $map")
        apiInterface.login_with_otp(map).enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    if (response.body() != null) {
                        val responseString = response.body()!!.string()
                        val jsonObject = JSONObject(responseString)
                        Log.e(TAG, "Login with OTP response = $responseString")
                        // var modelLogin = Gson().fromJson(responseString, ModelLogin::class.java)
                        if (jsonObject.getString("status") == "1") {
                            val oj = jsonObject.getJSONObject("result")
                            startActivity(
                                Intent(mContext, LoginVerifyAct::class.java)
                                    .putExtra("otp", oj.getString("verify_code"))
                                    .putExtra("partner_id", oj.getString("id"))
                            )
                        } else {
                            Toast.makeText(mContext, "invalid phone number", Toast.LENGTH_SHORT).show()

                        }

                    }
                } catch (e: Exception) {
                    Toast.makeText(mContext, "Exception = " + e.message, Toast.LENGTH_SHORT).show()
                    Log.e("Exception", "Exception = " + e.message)
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
            }

        })
    }

    fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                registerId = token
                Log.e(TAG, "token = " + registerId)
            }
        }

    }


}