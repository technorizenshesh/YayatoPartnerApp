package com.yayatopartnerapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.yayatopartnerapp.R
import com.yayatopartnerapp.models.ModelLogin
import com.yayatopartnerapp.utils.AppConstant
import com.yayatopartnerapp.utils.SharedPref
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashAct : AppCompatActivity() {

    var mContext: Context = this@SplashAct
    var SPLASH_TIMEOUT: Int = 3000;
    lateinit var sharedPref: SharedPref
    lateinit var modelLogin: ModelLogin
    var PERMISSION_ID = 44

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        printHashKey(mContext)
        sharedPref = SharedPref(mContext)
        modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
    }

    @SuppressLint("PackageManagerGetSignatures")
    fun printHashKey(pContext: Context) {
        Log.i("dsadadsdad", "printHashKey() Hash Key: aaya ander")
        try {
            val info = pContext.packageManager.getPackageInfo(
                pContext.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("dsadadsdad", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("dsadadsdad", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("dsadadsdad", "printHashKey()", e)
        }
    }

    override fun onResume() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                processNextActivity()
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions(this@SplashAct)
        }
        super.onResume()
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun processNextActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (sharedPref.getBooleanValue(AppConstant.IS_REGISTER)) {
                modelLogin = sharedPref.getUserDetails(AppConstant.USER_DETAILS)
                if (modelLogin.getResult()!!.step.equals("1")) {
                    startActivity(Intent(mContext, UploadDocAct::class.java))
                    finish()
                } else if (modelLogin.getResult()!!.step.equals("2")) {
                    startActivity(Intent(mContext, HomeAct::class.java))
                    finish()
                }
            } else {
                startActivity(Intent(mContext, LoginAct::class.java))
                finish()
            }
        }, 2000)
    }

    companion object {
        private fun requestPermissions(splashAct: SplashAct) {
            ActivityCompat.requestPermissions(
                splashAct, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                splashAct.PERMISSION_ID
            )
        }
    }


}