package com.yayatopartnerapp.listener

import com.yayatopartnerapp.models.ModelCarsRent
import com.yayatopartnerapp.models.ModelVehicalList


interface RentCarRequestListener {
    fun onClick(poolDetails: ModelCarsRent.Result.RequestDetailss, status: String, position: Int)
}