package com.yayatopartnerapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
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
import com.yayatopartnerapp.databinding.AdapterVehicleRequestBinding
import com.yayatopartnerapp.listener.RentCarRequestListener
import com.yayatopartnerapp.models.ModelCarsRent
//import com.yayatopartnerapp.databinding.AdapterRideHistoryBinding
import com.yayatopartnerapp.models.ModelLogin

class AdapterVehicleRequest(
    val mContext: Context,
    var transList: ArrayList<ModelCarsRent.Result.RequestDetailss>?
) : RecyclerView.Adapter<AdapterVehicleRequest.TransViewHolder>() {

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    lateinit var rentCarRequestListener: RentCarRequestListener

    fun selListener(rentCarRequestListener: RentCarRequestListener){
        this.rentCarRequestListener=rentCarRequestListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransViewHolder {
        var binding: AdapterVehicleRequestBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.adapter_vehicle_request, parent, false
        )
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        return TransViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransViewHolder, position: Int) {

        var data: ModelCarsRent.Result.RequestDetailss = transList!![position]
        holder.binding.tvPickup.text = data.driver_details!![0].address

        holder.binding.tvName.text = data.driver_details!![0].user_name
        holder.binding.tvEmail.text = data.driver_details!![0].email

        holder.binding.tvNumber.text = data.driver_details!![0].mobile
        holder.binding.tvStatus.text = data.status



        Glide.with(mContext)
            .load(data.driver_details!![0].image)
            .error(R.drawable.car)
            .placeholder(R.drawable.car)
            .into(holder.binding.ivDriverimag)

//        btnAccept
//        btnCancel

        if ("Finish" == data.status) {
            holder.binding.tvStatus.text = "Complete"
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green_spalsh))
            holder.binding.btnAccept.visibility=View.GONE
            holder.binding.btnCancel.visibility=View.GONE
        } else if ("Cancel" == data.status) {
            holder.binding.tvStatus.text = "Canceled"
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red))
            holder.binding.btnAccept.visibility=View.GONE
            holder.binding.btnCancel.visibility=View.GONE
        } else if ("Accept" == data.status) {
            holder.binding.tvStatus.text = "Accepted"
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.black))
            holder.binding.btnAccept.visibility=View.GONE
            holder.binding.btnCancel.visibility=View.VISIBLE
        } else {
            holder.binding.btnAccept.visibility=View.VISIBLE
            holder.binding.btnCancel.visibility=View.VISIBLE
            holder.binding.tvStatus.text = data.status
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.black))
        }

//        holder.binding.btnShowDrivers.setOnClickListener {
//            mContext.startActivity(
//                Intent(
//                    mContext,
//                    DriverRequestsAct::class.java
//                ).putExtra("model",data)
//            )
//        }

        holder.binding.btnAccept.setOnClickListener {
            rentCarRequestListener.onClick(data,"Accept",position)
        }
        holder.binding.btnCancel.setOnClickListener {
            rentCarRequestListener.onClick(data,"Cancel",position)
        }

    }

    override fun getItemCount(): Int {
        return if (transList == null) 0 else transList!!.size
    }

    class TransViewHolder(var binding: AdapterVehicleRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {}




}