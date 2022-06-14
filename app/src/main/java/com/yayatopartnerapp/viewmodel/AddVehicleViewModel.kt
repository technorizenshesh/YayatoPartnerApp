package com.yayatopartnerapp.viewmodel

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.*
import com.yayatopartnerapp.models.AddVehicleModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.MutableLiveData





class AddVehicleViewModel : ViewModel(),Observable {
    var context: Context? = null

    val startDate: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


     var model: MutableLiveData<AddVehicleModel?> = MutableLiveData()


    fun init(context: Context) {
        this.context = context
        startDate!!.value = "Start Date"

        // model = MutableLiveData<AddVehicleModel?>()
        //  loginRepository  = LoginRepository(context)
        // loginViewModel = loginRepository!!.getLognUserData()
        // getFirebaseToken()
    }




    fun onStartDateClick(view: View) {
        var cal = Calendar.getInstance()
        // create an OnDateSetListener
        Log.e("Chala==", "click challl===")
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
//                    datecurr=sdf.format(cal.getTime())
                // binding.etStartDate!!.setText(sdf.format(cal.getTime()))

                startDate!!.value = sdf.format(cal.getTime())
                Log.e("Date====",startDate!!.value.toString())


                // model.value!!.setStartDate(sdf.format(cal.getTime()))

            }
        }

        DatePickerDialog(
            view.context,
            dateSetListener,
            // set DatePickerDialog to point to today's date when it loads up
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }









    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


}










