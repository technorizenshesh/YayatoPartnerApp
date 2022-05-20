package com.yayatopartnerapp.notification_manager

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PushNotificationModel : Serializable {

    @SerializedName("key")
    @Expose
    var key: String? = null

    @SerializedName("request_id")
    @Expose
    var request_id: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null




}