package com.yayatopartnerapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.yayatopartnerapp.repository.ForgotPassRepository
import okhttp3.ResponseBody

class ForgotPassViewModel(application: Application) : AndroidViewModel(application) {

    var forgotPassRepository: ForgotPassRepository? = null
    var forgotPassLiveData: LiveData<ResponseBody?>? = null
    var context: Context? = null


    fun init(context: Context) {
        this.context = context
        forgotPassRepository = ForgotPassRepository(context)
        forgotPassLiveData = forgotPassRepository!!.getForgotPassUserData()
    }


    fun forgotPassApiCallViewModel(email: String) {
        forgotPassRepository!!.forgotPassUser(email)
    }

    fun getForgotPassDataViewModel(): LiveData<ResponseBody?>? {
        return forgotPassLiveData
    }

}