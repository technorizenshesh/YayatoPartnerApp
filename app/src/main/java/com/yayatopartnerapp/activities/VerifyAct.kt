package com.yayatopartnerapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.Gson
import com.yayatopartnerapp.R
import com.yayatopartnerapp.databinding.ActivityVerifyBinding
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.utils.*
import com.yayatopartnerapp.viewmodel.SignupViewModel
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_verify.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.HashMap
import java.util.concurrent.TimeUnit

class VerifyAct : AppCompatActivity() {

    var mContext: Context = this@VerifyAct
    lateinit var binding: ActivityVerifyBinding
    var type = ""
    var mobile = ""
    var id: String? = null
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    private var mAuth: FirebaseAuth? = null
    var fileHashMap = HashMap<String, File>()
    var paramHash = HashMap<String, String>()
    var signupViewModel : SignupViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupViewModel = ViewModelProviders.of(this).get(SignupViewModel::class.java)
        signupViewModel!!.init(mContext)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_verify)


        signupViewModel!!.getSignupDataViewModel()!!.observe(this,{
            if (it != null) {
                modelLogin =  it;
                Toast.makeText(mContext,R.string.signup_sucess,Toast.LENGTH_LONG)
                sharedPref.setBooleanValue(AppConstant.IS_REGISTER, true)
                sharedPref.setUserDetails(AppConstant.USER_DETAILS, modelLogin)
                startActivity(Intent(mContext, HomeAct::class.java))
                finish()

            } else {
                MyApplication.showAlert(mContext, modelLogin.getMessage()!!)

            }
        })

        itit()

    }

    private fun itit() {

        sharedPref = SharedPref(mContext)
        mAuth = FirebaseAuth.getInstance()

        mobile = intent.getStringExtra("mobile")!!
        paramHash = intent.getSerializableExtra("resgisterHashmap") as HashMap<String, String>
        fileHashMap = intent.getSerializableExtra("fileHashMap") as HashMap<String, File>

        if (InternetConnection.checkConnection(mContext)) sendVerificationCode()
        else MyApplication.showConnectionDialog(mContext)


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
                ProjectUtil.showProgressDialog(mContext, true, getString(R.string.please_wait))
                val credential = PhoneAuthProvider.getCredential(id!!, otpFull)
                signInWithPhoneAuthCredential(credential)
            }
        }

    }

    private fun sendVerificationCode() {
        tvVerifyText.text = "We have send you an SMS on $mobile with 6 digit verification code."
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvResend.text = "" + millisUntilFinished / 1000
                tvResend.isEnabled = false
            }

            override fun onFinish() {
                tvResend.text = mContext.getString(R.string.resend)
                tvResend.isEnabled = true
            }
        }.start()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mobile.replace(" ", ""),  // Phone number to verify
            60,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            this,  // Activity (for callback binding)
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(id: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                    this@VerifyAct.id = id
                    Toast.makeText(mContext, getString(R.string.enter_6_digit_code), Toast.LENGTH_SHORT).show()
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    ProjectUtil.pauseProgressDialog()
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    ProjectUtil.pauseProgressDialog()
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    ProjectUtil.pauseProgressDialog()
                    // Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result.user
                    if (InternetConnection.checkConnection(mContext)) signupViewModel!!.signupApiCallViewModel(paramHash["first_name"]!!,paramHash["last_name"]!!
                        ,paramHash["email"]!!,paramHash["mobile"]!!,paramHash["address"]!!,paramHash["register_id"]!!,paramHash["lat"]!!
                        , paramHash["lon"]!!,paramHash["password"]!!,paramHash["type"]!!,"1",fileHashMap["image"]!!)
                    else MyApplication.showConnectionDialog(mContext)
                } else {
                    ProjectUtil.pauseProgressDialog()
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        task.exception
                    }
                }
            }
    }



}