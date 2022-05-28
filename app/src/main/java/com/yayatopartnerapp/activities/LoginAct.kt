package com.yayatopartnerapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.yayatopartnerapp.R
import com.yayatopartnerapp.databinding.ActivityLoginBinding
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.utils.*
import com.yayatopartnerapp.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginAct : AppCompatActivity() {
    val TAG = "LoginAct";
    var mContext: Context = this@LoginAct
    lateinit var binding: ActivityLoginBinding
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    var loginViewModel: LoginViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginViewModel = loginViewModel

        loginViewModel!!.init(mContext)
        sharedPref = SharedPref(mContext)

        loginViewModel!!.getLoginDataViewModel()!!.observe(this, {
            if (it != null) {
                modelLogin = it;
                Log.e("Login data===", Gson()!!.toJson(modelLogin))
                Toast.makeText(mContext,R.string.login_sucess,Toast.LENGTH_LONG)
                sharedPref.setBooleanValue(AppConstant.IS_REGISTER, true)
                sharedPref.setUserDetails(AppConstant.USER_DETAILS, modelLogin)
                startActivity(Intent(mContext, HomeAct::class.java))
                finish()

            } else {
                MyApplication.showAlert(mContext, getString(R.string.invalid_credentials))

            }
        })

    }

}