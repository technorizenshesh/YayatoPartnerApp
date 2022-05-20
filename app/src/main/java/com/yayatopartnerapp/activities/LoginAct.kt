package com.yayatopartnerapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.yayatopartnerapp.R
import com.yayatopartnerapp.databinding.ActivityLoginBinding
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.utils.*
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginAct : AppCompatActivity() {

    var mContext: Context = this@LoginAct
    lateinit var binding: ActivityLoginBinding
    private lateinit var registerId: String
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        sharedPref = SharedPref(mContext)

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                registerId = token
                Log.e("dsfsfsdfdsf", "token = " + registerId)
            }
        }

        itit()
    }

    private fun itit() {

        btnSignin.setOnClickListener {
            if (TextUtils.isEmpty(etEmail.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.enter_email_text))
            } else if (TextUtils.isEmpty(etPassword.text.toString().trim())) {
                MyApplication.showAlert(mContext, getString(R.string.please_enter_pass))
            } else {
                if (InternetConnection.checkConnection(mContext)) {
                    loginApiCall()
                } else {
                    MyApplication.showConnectionDialog(mContext)
                }
            }
        }

        binding.ivForgotPass.setOnClickListener {
            startActivity(Intent(mContext, ForgotPassAct::class.java))
        }

        binding.btSignup.setOnClickListener {
            startActivity(Intent(mContext, SignUpAct::class.java))
        }

    }

    private fun loginApiCall() {

        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))

        var paramHash = HashMap<String, String>()

        paramHash.put("email", etEmail.text.toString().trim())
        paramHash.put("password", etPassword.text.toString().trim())
        paramHash.put("lat", "")
        paramHash.put("lon", "")
        paramHash.put("type", AppConstant.PARTNER)
        paramHash.put("register_id", registerId)

        Log.e("asdfasdfasf", "paramHash = $paramHash")
        var api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        var call: Call<ResponseBody> = api.loginApiCall(paramHash)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val responseString = response.body()!!.string()
                    val jsonObject = JSONObject(responseString)
                    Log.e("responseString", "responseString = $responseString")
                    if (jsonObject.getString("status") == "1") {

                        modelLogin = Gson().fromJson(responseString, ModelLogin::class.java)

                        sharedPref.setBooleanValue(AppConstant.IS_REGISTER, true)
                        sharedPref.setUserDetails(AppConstant.USER_DETAILS, modelLogin)

                        startActivity(Intent(mContext, HomeAct::class.java))
                        finish()

                    } else {
                        MyApplication.showAlert(mContext, getString(R.string.invalid_credentials))
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

}