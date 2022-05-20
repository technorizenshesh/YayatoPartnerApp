package com.yayatopartnerapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.yayatopartnerapp.R
import com.yayatopartnerapp.databinding.ActivityForgotPassBinding
import com.yayatopartnerapp.utils.InternetConnection
import com.yayatopartnerapp.utils.MyApplication
import com.yayatopartnerapp.utils.ProjectUtil
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import kotlinx.android.synthetic.main.activity_forgot_pass.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassAct : AppCompatActivity() {

    var mContext: Context = this@ForgotPassAct
    lateinit var binding: ActivityForgotPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_pass)
        itit()
    }

    private fun itit() {
        btSubmit.setOnClickListener {
            if (TextUtils.isEmpty(etEmail.text.toString().trim())) {
                Toast.makeText(mContext, getString(R.string.enter_email_text), Toast.LENGTH_SHORT).show()
            } else {
                if (InternetConnection.checkConnection(mContext)) forgotPassApiCall()
                else MyApplication.showConnectionDialog(mContext)
            }
        }

        ivBack.setOnClickListener {
            finish()
        }

    }

    private fun forgotPassApiCall() {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))

        var params: HashMap<String, String> = HashMap()
        params.put("email", etEmail.text.toString().trim())
        params.put("type", "USER")

        val api = ApiFactory.getClientWithoutHeader(mContext)?.create(Api::class.java)
        val call: Call<ResponseBody> = api!!.forgotPass(params)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                ProjectUtil.pauseProgressDialog()
                try {

                    var stringResponse: String = response.body()!!.string()
                    var jsonObject = JSONObject(stringResponse)

                    if (jsonObject.getString("status") == "1") {
                        finish()
                        Toast.makeText(mContext, getString(R.string.reset_pass_msg), Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(mContext, getString(R.string.email_not_found), Toast.LENGTH_LONG).show()
                    }

                } catch (e: Exception) {}
            }
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
            }
        })


    }


}