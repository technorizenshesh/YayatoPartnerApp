package com.yayatopartnerapp.utils

import android.net.Uri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_sign_up.*

interface AppConstant {

    companion object {

        var BASE_URL = "https://technorizen.com/yayato_new/Webservice/"

        var BASE_URL11 = "https://technorizen.com/yayato/Webservice/"


        var TYPE = "type"
        var USER = "USER"
        var DRIVER = "DRIVER"
        var PARTNER = "PARTNER"

        var LOGIN_API = "login"
        var POOL = "pool"
        var BOOK = "book"
        var MY_LOCATION = "Your Location"
        var RES_DRIVER = "RES_DRIVER"
        var CURRENCY = "Birr"
        var EBIRR = "EBIRR"
        var SAHAY = "Sahay"
        var HELLO_CASH = "Hello Cash"
        var SH_DRIVER = "SH_DRIVER"
        var STORE_BOOKING_PARAMS = "bookingparam"
        var SIGNUP_API = "signup"
        var SHOP = "SHOP"
        var EURO = "€"
        var UPDATE_PROFILE_API = "update_profile"
        var FORGOT_PASSWORD_API = "forgot_password"
        var CHANGE_PASSWORD_API = "change_password"

        var IS_FILTER = "is_filter"
        var IS_SEARCH = "is_search"
        var SEARCH_DATA = "search_data"
        var DOLLAR = "$"
        var FILTER_SELECTED_DATA = "filter_selected_item"

        var IS_REGISTER = "user_register"
        var USER_DETAILS = "user_details"
        var UPDATE_ORDER_STATUS = "update_order_status"
        var DEV_FOOD = "DEV_FOOD"
        var TAXI_DRIVER = "TAXI_DRIVER"
        var LAST = "last"

    }


    object DataBindingAdapters {

        @BindingAdapter("imageSrc")
        @JvmStatic
        fun setImageResource(imageView: CircleImageView, resource: String) {
           // imageView.setImageResource(resource)
            imageView.setImageURI(Uri.parse(resource))

        }

      /*  @BindingAdapter("visible")
        @JvmStatic
        fun View.setVisible(show: Boolean) {
            visibility = if (show) VISIBLE else GONE
        }*/
    }

}