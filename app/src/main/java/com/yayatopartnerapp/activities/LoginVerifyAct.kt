package com.yayatopartnerapp.activities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.Gson
import com.yayatopartnerapp.R
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.utils.*
import com.yayatopartnerapp.utils.retrofit.ApiClient
import com.yayatopartnerapp.utils.retrofit.YayatoApiService
import com.yayatopartnerapp.viewmodel.SignupViewModel
import kotlinx.android.synthetic.main.activity_verify.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.HashMap
import java.util.concurrent.TimeUnit

class LoginVerifyAct  : AppCompatActivity() {
    var TAG = "LoginVerifyAct"
    lateinit var apiInterface: YayatoApiService
    var mContext: Context = this@LoginVerifyAct
    var otp = ""
    var id: String? = null
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiInterface = ApiClient.getClient(mContext)!!.create(YayatoApiService::class.java)
        setContentView(R.layout.activity_verify)
        initViews()
    }

    private fun initViews() {
        sharedPref = SharedPref(mContext)

        otp = intent.getStringExtra("otp")!!
        id = intent.getStringExtra("partner_id") !!

        ivBack.setOnClickListener { finish() }

        et1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                et2.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        et2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                et3.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        et3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                et4.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        et4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                et5.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        et5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                et6.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        btn_verify.setOnClickListener {
            validation()
        }

    }

    private fun validation() {
        if (TextUtils.isEmpty(et1.text.toString().trim())) {
            Toast.makeText(mContext, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(et2.text.toString().trim())) {
            Toast.makeText(mContext, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(et3.text.toString().trim())) {
            Toast.makeText(mContext, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(et4.text.toString().trim())) {
            Toast.makeText(mContext, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(et5.text.toString().trim())) {
            Toast.makeText(mContext, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(et6.text.toString().trim())) {
            Toast.makeText(mContext, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
        } else {
            val otpFull: String = et1.text.toString().trim() +
                    et2.text.toString().trim() +
                    et3.text.toString().trim() +
                    et4.text.toString().trim() +
                    et5.text.toString().trim() +
                    et6.text.toString().trim()


            if(!otpFull.equals(otp)) Toast.makeText(mContext, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
            else {
                if (InternetConnection.checkConnection(mContext)) partnerVerify()
                else MyApplication.showConnectionDialog(mContext)

            }

        }
    }

    private fun partnerVerify() {
        ProjectUtil.showProgressDialog(mContext, false, mContext?.getString(R.string.please_wait))
        var map = HashMap<String, String>()
        map.put("user_id",id!!)
        map.put("otp",otp)
        Log.e(TAG, "Verify Partner Request = $map")
        apiInterface.verify_with_otp(map).enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    if (response.body() != null) {
                        val responseString = response.body()!!.string()
                        val jsonObject = JSONObject(responseString)
                        Log.e(TAG, "Verify Partner response = $responseString")
                        if (jsonObject.getString("status") == "1") {
                            var modelLogin = Gson().fromJson(responseString, ModelLogin::class.java)
                            Toast.makeText(mContext,R.string.login_sucess,Toast.LENGTH_LONG)
                            sharedPref.setBooleanValue(AppConstant.IS_REGISTER, true)
                            sharedPref.setUserDetails(AppConstant.USER_DETAILS, modelLogin)
                            startActivity(Intent(mContext, HomeAct::class.java))
                            finish()
                        } else {
                            Toast.makeText(mContext, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show()
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


}