package com.yayatopartnerapp.models

import java.io.Serializable
import java.util.ArrayList

class ModelMake : Serializable {

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
        var code: String? = null
        var title: String? = null
    }

}