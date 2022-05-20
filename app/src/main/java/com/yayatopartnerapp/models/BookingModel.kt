package com.yayatopartnerapp.models

import java.io.Serializable
import java.util.ArrayList

class BookingModel: Serializable {
    private var result: ArrayList<Result>? = null
    private var status: String? = null
    private var message: String? = null

    fun setResult(result: ArrayList<Result>?) {
        this.result = result
    }

    fun getResult(): ArrayList<Result>? {
        return result
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun getStatus(): String? {
        return status
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getMessage(): String? {
        return message
    }

    class Result : Serializable {
        var id: String? = null
        var car_detail_id: String? = null
        var driver_id: String? = null
        var partner_id: String? = null
        var status: String? = null
        var rate_pre_km: String? = null
        var start_date: String? = null
        var end_date: String? = null


       /* var driver_details: ArrayList<BookingModel.Result.driverDetails>? = null

        class driverDetails : Serializable {
            var id: String? = null
            var car_type_id: String? = null
            var car_detail_id: String? = null
            var user_name: String? = null
            var first_name: String? = null
            var last_name: String? = null
            var email: String? = null
            var mobile: String? = null
            var image: String? = null
        }*/

    }
}