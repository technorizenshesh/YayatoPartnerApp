package com.yayatopartnerapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//import com.yayatopartnerapp.models.ModelTaxiRequest
import com.yayatopartnerapp.models.ModelVehicalList
import com.yayatopartnerapp.utils.SharedPref
import com.yayatopartnerapp.utils.AppConstant
//import com.yayatopartnerapp.utils.SharedPref
import com.yayatopartnerapp.R
import com.yayatopartnerapp.activities.DriverRequestsAct
//import com.yayatopartnerapp.activities.RideDetailsAct
import com.yayatopartnerapp.databinding.AdapterAllVehicleBinding
//import com.yayatopartnerapp.databinding.AdapterRideHistoryBinding
import com.yayatopartnerapp.models.ModelLogin

class AdapterAllVehicle(
    val mContext: Context,
    var transList: ArrayList<ModelVehicalList.Result>?
) : RecyclerView.Adapter<AdapterAllVehicle.TransViewHolder>() {

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransViewHolder {
        var binding: AdapterAllVehicleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.adapter_all_vehicle, parent, false
        )
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        return TransViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransViewHolder, position: Int) {

        var data: ModelVehicalList.Result = transList!![position]
        holder.binding.tvPickup.text = data.address

        holder.binding.tvDateTime.text = data.car_name//"Available From\n"+data.start_time+" - "+data.end_time
        holder.binding.tvCharge.text = data.car_name
        holder.binding.tvVehicleModel.text = data.model_name

        holder.binding.tvCarNumber.text = data.car_number


        Glide.with(mContext)
            .load(data.car_image)
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

        holder.binding.btnShowDrivers.setOnClickListener {
            mContext.startActivity(
                Intent(
                    mContext,
                    DriverRequestsAct::class.java
                ).putExtra("model",data)
            )
        }

    }

    override fun getItemCount(): Int {
        return if (transList == null) 0 else transList!!.size
    }

    class TransViewHolder(var binding: AdapterAllVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}