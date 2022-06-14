package com.yayatopartnerapp.models

class AddVehicleModel {
    private var startDate: String? = null
    private var endDate: String? = null
    private var startTime: String? = null
    private var endTime: String? = null

    fun setStartDate(startDate: String?) {
        this.startDate = startDate
    }

    fun getStartDate(): String? {
        return startDate
    }


    fun setEndDate(endDate: String?) {
        this.endDate = endDate
    }

    fun getEndDate(): String? {
        return endDate
    }

    fun setStartTime(startTime: String?) {
        this.startTime = startTime
    }

    fun getStartTime(): String? {
        return startTime
    }

    fun setEndTime(endTime: String?) {
        this.endTime = endTime
    }

    fun getEndTime(): String? {
        return endTime
    }

}