package com.yayatopartnerapp.activities

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.yayatopartnerapp.R
import com.yayatopartnerapp.adapters.AdapterTransactions
import com.yayatopartnerapp.databinding.ActivityWalletBinding
import com.yayatopartnerapp.databinding.AddMoneyDialogBinding
import com.yayatopartnerapp.databinding.SendMoneyDialogNewBinding
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.models.ModelTransactions
import com.yayatopartnerapp.utils.AppConstant
import com.yayatopartnerapp.utils.MyApplication.Companion.showAlert
import com.yayatopartnerapp.utils.ProjectUtil
import com.yayatopartnerapp.utils.SharedPref
import com.yayatotaxi.utils.retrofit.Api
import com.yayatotaxi.utils.retrofit.ApiFactory
import com.yayatotaxi.utils.retrofit.MyApplication
import kotlinx.android.synthetic.main.activity_wallet.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class WalletAct : AppCompatActivity() {

    var mContext: Context = this@WalletAct
    lateinit var binding: ActivityWalletBinding
    var walletTmpAmt: Double = 0.0

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet)


        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)

        itit()
    }


    private fun itit() {

        ivBack.setOnClickListener {
            finish()
        }

        getAllTransactionsApi()

        swipLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { getAllTransactionsApi() })

        cvAddMoney.setOnClickListener { addMoneyDialog() }

        cvTransfer.setOnClickListener { tranferMOneyDialog() }

    }

    private fun addMoneyDialog() {

        val dialog = Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding: AddMoneyDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.add_money_dialog, null, false
        )

        dialog.setContentView(dialogBinding.getRoot())
        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) dialog.dismiss()
            false
        }

        dialogBinding.etMoney.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!(s == null || s == "")) {
                    try {
                        walletTmpAmt = s.toString().toDouble()
                    } catch (e: Exception) {
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        dialogBinding.ivMinus.setOnClickListener { v ->
            if (!(dialogBinding.etMoney.getText().toString().trim()
                    .equals("") || dialogBinding.etMoney.getText().toString().trim().equals("0"))
            ) {
                walletTmpAmt = dialogBinding.etMoney.getText().toString().trim().toDouble() - 1
                dialogBinding.etMoney.setText(walletTmpAmt.toString())
            }
        }

        dialogBinding.ivPlus.setOnClickListener { v ->
            if (TextUtils.isEmpty(dialogBinding.etMoney.getText().toString().trim())) {
                dialogBinding.etMoney.setText("0")
                walletTmpAmt = dialogBinding.etMoney.getText().toString().trim().toDouble() + 1
                dialogBinding.etMoney.setText(walletTmpAmt.toString())
            } else {
                walletTmpAmt = dialogBinding.etMoney.getText().toString().trim().toDouble() + 1
                dialogBinding.etMoney.setText(walletTmpAmt.toString())
            }
        }

        dialogBinding.tv699.setOnClickListener { v ->
            dialogBinding.etMoney.setText("699")
            walletTmpAmt = dialogBinding.etMoney.getText().toString().trim().toDouble()
            dialogBinding.etMoney.setText(walletTmpAmt.toString())
        }

        dialogBinding.tv799.setOnClickListener { v ->
            dialogBinding.etMoney.setText("799")
            walletTmpAmt = dialogBinding.etMoney.getText().toString().trim().toDouble()
            dialogBinding.etMoney.setText(walletTmpAmt.toString())
        }

        dialogBinding.tv899.setOnClickListener { v ->
            dialogBinding.etMoney.setText("899")
            walletTmpAmt = dialogBinding.etMoney.getText().toString().trim().toDouble()
            dialogBinding.etMoney.setText(walletTmpAmt.toString())
        }

        dialogBinding.btDone.setOnClickListener { v ->
            if (TextUtils.isEmpty(dialogBinding.etMoney.getText().toString().trim())) {
                Toast.makeText(mContext, "Please enter amount", Toast.LENGTH_SHORT).show()
            } else if (walletTmpAmt == 0.0) {
                Toast.makeText(mContext, "Please enter valid amount", Toast.LENGTH_SHORT).show()
            } else addWallet(walletTmpAmt.toString(), dialog)
        }

        dialogBinding.tvCancel.setOnClickListener { v -> dialog.dismiss() }
        val window = dialog.window
        val wlp = window!!.attributes
        dialog.window!!.setBackgroundDrawableResource(R.color.translucent_black)
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        dialog.show()

    }

    private fun tranferMOneyDialog() {
        val dialog = Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding: SendMoneyDialogNewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.send_money_dialog_new, null, false
        )
        dialog.setContentView(dialogBinding.getRoot())
        dialogBinding.btDone.setOnClickListener { v ->
            if (TextUtils.isEmpty(dialogBinding.etEmail.text.toString().trim())) {
                Toast.makeText(mContext, getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(dialogBinding.etEnterAmount.text.toString().trim())) {
                Toast.makeText(
                    mContext,
                    getString(R.string.please_enter_amount),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!ProjectUtil.isValidEmail(dialogBinding.etEmail.text.toString().trim())) {
                Toast.makeText(mContext, getString(R.string.invalid_email), Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (dialogBinding.etEnterAmount.text.toString().toString().equals("0")) {
                    MyApplication.showAlert(mContext, getString(R.string.please_enter_valid_amount))
                }
                sendMoneyAPiCall(
                    dialog,
                    dialogBinding.etEmail.text.toString().trim(),
                    dialogBinding.etEnterAmount.text.toString().trim(),
                    "DRIVER"
                )
            }
        }
        dialogBinding.tvCancel.setOnClickListener { v -> dialog.dismiss() }
        val window = dialog.window
        val wlp = window!!.attributes
        dialog.window!!.setBackgroundDrawableResource(R.color.translucent_black)
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        dialog.show()
    }

    private fun addWallet(amount: String, dialog: Dialog) {

        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("user_id", modelLogin.getResult()!!.id!!)
        hashMap.put("wallet", amount)

        var api: Api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        var call: Call<ResponseBody> = api.addWalletApiCall(hashMap)
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val stringResponse = response.body()!!.string()

                    Log.e("asdfasdfasdfas", "stringResponse = $stringResponse")

                    val jsonObject = JSONObject(stringResponse)

                    if (jsonObject.getString("status") == "1") {
                        dialog.dismiss()
                        getAllTransactionsApi()
                        getProfileApiCall()
                    }

                } catch (e: Exception) {
                }

            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
            }

        })

    }

    override fun onResume() {
        super.onResume()
        getProfileApiCall();
    }

    private fun getProfileApiCall() {

        // ProjectUtil.showProgressDialog(mContext,false,getString(R.string.please_wait));
        val paramHash = java.util.HashMap<String, String>()
        paramHash["user_id"] = modelLogin.getResult()?.id!!

        val api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call = api.getProfileCall(paramHash)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val stringResponse = response.body()!!.string()
                    try {
                        val jsonObject = JSONObject(stringResponse)
                        if (jsonObject.getString("status") == "1") {
                            Log.e("getProfileApiCallnew", "getProfileApiCall = $stringResponse")
                            modelLogin = Gson().fromJson(stringResponse, ModelLogin::class.java)
                            tvWalletAmount.text =
                                AppConstant.CURRENCY + " " + modelLogin.getResult()?.wallet
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("JSONException", "JSONException = " + e.message)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
            }
        })

    }

    private fun getAllTransactionsApi() {
        val paramHash = java.util.HashMap<String, String>()
        paramHash["user_id"] = modelLogin.getResult()?.id!!

        Log.e("paramHashparamHash", "paramHash = $paramHash")

        val api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call: Call<ResponseBody> = api.getTransactionApiCall(paramHash)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                swipLayout.isRefreshing = false
                try {
                    val stringResponse = response.body()!!.string()
                    try {
                        val jsonObject = JSONObject(stringResponse)
                        Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = $stringResponse")
                        if (jsonObject.getString("status") == "1") {
                            Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = $stringResponse")
                            val modelTransactions: ModelTransactions =
                                Gson().fromJson(stringResponse, ModelTransactions::class.java)
                            val adapterTransactions =
                                AdapterTransactions(mContext, modelTransactions.getResult())
                            rvTransactions.adapter = adapterTransactions
                        } else {
                            val adapterTransactions = AdapterTransactions(mContext, null)
                            rvTransactions.adapter = adapterTransactions
                            // Toast.makeText(mContext, jsonObject.getString("result"), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("JSONException", "JSONException = " + e.message)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
                swipLayout.isRefreshing = false
            }
        })
    }

    private fun sendMoneyAPiCall(dialog: Dialog, email: String, amount: String, type: String) {
        ProjectUtil.showProgressDialog(mContext, false, getString(R.string.please_wait))

        val paramHash = java.util.HashMap<String, String>()
        paramHash["user_id"] = modelLogin.getResult()?.id!!
        paramHash["amount"] = amount
        paramHash["email"] = email
        paramHash["type"] = type

        Log.e("paramHashparamHash", "paramHash = $paramHash")

        val api = ApiFactory.getClientWithoutHeader(mContext)!!.create(Api::class.java)
        val call = api.walletTransferApiCall(paramHash)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                ProjectUtil.pauseProgressDialog()
                try {
                    val stringResponse = response.body()!!.string()
                    try {
                        val jsonObject = JSONObject(stringResponse)
                        Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = $stringResponse")
                        if (jsonObject.getString("status") == "1") {
                            Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = $stringResponse")
                            getAllTransactionsApi()
                            getProfileApiCall()
                            dialog.dismiss()
                        } else {
                            MyApplication.showAlert(mContext, jsonObject.getString("result"))
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("JSONException", "JSONException = " + e.message)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                ProjectUtil.pauseProgressDialog()
            }
        })
    }

}