package com.yayatopartnerapp.viewmodel

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import java.text.SimpleDateFormat
import java.util.*


class AddVehicleViewModel(application: Application) : AndroidViewModel(application), Observable {
    // var loginViewModel: LiveData<ModelLogin?>? = null
    var context: Context? = null
    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()


    var startDate = MutableLiveData<String>()


    fun init(context: Context) {
        this.context = context
        //  loginRepository  = LoginRepository(context)
        // loginViewModel = loginRepository!!.getLognUserData()
        // getFirebaseToken()
    }

    fun onStartDateClick(view: View) {
        var cal = Calendar.getInstance()
        // create an OnDateSetListener
        Log.e("Chala==","click challl===")
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

                setStartDate(sdf.format(cal.getTime()))

            }
        }

        DatePickerDialog(view.context,
            dateSetListener,
            // set DatePickerDialog to point to today's date when it loads up
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()

    }


    @Bindable
    fun getStartDate(): LiveData<String?>? {
        return startDate
    }


    fun setStartDate(value: String) {
        startDate.value = value
       // notifyPropertyChanged(BR.)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }


    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies observers that a specific property has changed. The getter for the
     * property that changes should be marked with the @Bindable annotation to
     * generate a field in the BR class to be used as the fieldId parameter.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }


}










