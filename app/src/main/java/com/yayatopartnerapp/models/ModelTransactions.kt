package com.yayatopartnerapp.models

import java.io.Serializable
import java.util.ArrayList

class ModelTransactions : Serializable {

    private var result: ArrayList<Result>? = null
    private var message: String? = null
    private var status = 0

    fun setResult(result: ArrayList<Result>?) {
        this.result = result
    }

    fun getResult(): ArrayList<Result>? {
        return result
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getMessage(): String? {
        return message
    }

    fun setStatus(status: Int) {
        this.status = status
    }

    fun getStatus(): Int {
        return status
    }

    class Result : Serializable {
        var id: String? = null
        var user_id: String? = null
        var type: String? = null
        var type_id: String? = null
        var amount: String? = null
        var transaction_type: String? = null
        var description: String? = null
        var time_zone: String? = null
        var date_time: String? = null
        var status: String? = null
        var user_name: String? = null
    }


}