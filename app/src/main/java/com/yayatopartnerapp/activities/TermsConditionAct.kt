package com.yayatopartnerapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.yayatopartnerapp.R
import com.yayatopartnerapp.databinding.ActivityTermsConditionBinding

class TermsConditionAct : AppCompatActivity() {

    var mContext: Context = this@TermsConditionAct
    lateinit var binding: ActivityTermsConditionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_terms_condition)
        itit()
    }

    private fun itit() {
        binding.ivBack.setOnClickListener { finish() }
    }

}