package com.yayatopartnerapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yayatopartnerapp.models.ModelVehicalList
import com.yayatopartnerapp.utils.SharedPref
import com.yayatopartnerapp.utils.AppConstant
import com.yayatopartnerapp.R
import com.yayatopartnerapp.activities.DriverRequestsAct
import com.yayatopartnerapp.databinding.AdapterHomeVehicleBinding
import com.yayatopartnerapp.models.BookingModel
import com.yayatopartnerapp.models.ModelCarsRent
import com.yayatopartnerapp.models.ModelLogin

class AdapterHomeVehicle(
    val mContext: Context,
    var transList: ArrayList<BookingModel.Result>?
) : RecyclerView.Adapter<AdapterHomeVehicle.TransViewHolder>() {

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransViewHolder {
        var binding: AdapterHomeVehicleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.adapter_home_vehicle, parent, false
        )
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        return TransViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransViewHolder, position: Int) {

        var data: BookingModel.Result = transList!![position]
        holder.binding.tvStatus.text = data.status

        holder.binding.tvDateTime.text = "Available From\n"+data.start_date+" - "+data.end_date/*+"\n"+data.start_time+" - "+data.end_time*/
        holder.binding.tvCharge.text = "$"+data.rate_pre_km
//        holder.binding.tvVehicleModel.text = data.model_name

//        holder.binding.tvCarNumber.text = data.car_number


        Glide.with(mContext)
            .load("")//data.car_image)
            .error(R.drawable.car)
            .placeholder(R.drawable.car)
            .into(holder.binding.carImage)


//        if ("Finish" == data.status) {
//            holder.binding.tvStatus.text = "Complete"
//            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green))
//        } else if ("Cancel" == data.status) {
//            holder.binding.tvStatus.text = "Canceled"
//            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red))
//        } else {
//            holder.binding.tvStatus.text = data.status
//            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.black))
//        }

        holder.binding.rlLocations.setOnClickListener {
          /*  mContext.startActivity(
                Intent(
                    mContext,
                    DriverRequestsAct::class.java
                ).putExtra("model",data)
            )*/
        }

        holder.binding.btnShowDriversStart.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return if (transList == null) 0 else transList!!.size
    }

    class TransViewHolder(var binding: AdapterHomeVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}