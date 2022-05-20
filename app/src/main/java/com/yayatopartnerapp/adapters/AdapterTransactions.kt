package com.yayatopartnerapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yayatopartnerapp.utils.AppConstant
import com.yayatopartnerapp.utils.SharedPref
import com.yayatopartnerapp.R
import com.yayatopartnerapp.databinding.AdapterTransactionsBinding
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.models.ModelTransactions

class AdapterTransactions(
    val mContext: Context,
    var transList: ArrayList<ModelTransactions.Result>?
  ) : RecyclerView.Adapter<AdapterTransactions.TransViewHolder>() {

    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransViewHolder {
        var binding: AdapterTransactionsBinding = DataBindingUtil.inflate (
            LayoutInflater.from(mContext), R.layout.adapter_transactions, parent, false
        )
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
        return TransViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransViewHolder, position: Int) {

        var data: ModelTransactions.Result = transList!!.get(position)
        holder.binding.tvDate.text = data.date_time!!.split(" ")[0].trim()
        holder.binding.tvAmount.text = AppConstant.CURRENCY + " " + data.amount

        if ("Credit" == data.transaction_type) {
            holder.binding.tvDebitCredit.text = "Credit"
            holder.binding.ivSendRecive.setImageResource(R.drawable.credit_icon)
            holder.binding.tvDebitCredit.setTextColor(ContextCompat.getColor(mContext, R.color.green_spalsh))
        } else {
            holder.binding.ivSendRecive.setImageResource(R.drawable.debit_icon)
            holder.binding.tvDebitCredit.text = "Debit"
            holder.binding.tvDebitCredit.setTextColor(ContextCompat.getColor(mContext, R.color.red))
        }

    }

    override fun getItemCount(): Int {
        return if (transList == null) 0 else transList!!.size
    }

    class TransViewHolder(var binding: AdapterTransactionsBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}