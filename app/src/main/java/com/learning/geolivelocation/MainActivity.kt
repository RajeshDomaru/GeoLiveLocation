package com.learning.geolivelocation

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.learning.geolivelocation.databinding.ActivityMainBinding
import com.learning.geolivelocation.service.LocationService
import com.learning.geolivelocation.util.Util

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mLocationService: LocationService = LocationService()

    private lateinit var mServiceIntent: Intent

    private lateinit var mActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mActivity = this@MainActivity

        if (!Util.isLocationEnabledOrNot(mActivity)) {
            Util.showAlertLocation(
                mActivity,
                getString(R.string.gps_enable),
                getString(R.string.please_turn_on_gps),
                getString(R.string.ok)
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissionsSafely(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.FOREGROUND_SERVICE
                ), 200
            )
        } else {
            requestPermissionsSafely(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 200)
        }

        binding.btnStartService.setOnClickListener {
            mLocationService = LocationService()
            mServiceIntent = Intent(this, mLocationService.javaClass)
            if (Util.isMyServiceRunning(mLocationService.javaClass, mActivity)) {
                Toast.makeText(
                    mActivity,
                    getString(R.string.service_already_running),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    startForegroundService(mServiceIntent)
                else startService(mServiceIntent)
                Toast.makeText(
                    mActivity,
                    getString(R.string.service_start_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(
        permissions: Array<String>,
        requestCode: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    override fun onDestroy() {
        if (::mServiceIntent.isInitialized) {
            stopService(mServiceIntent)
        }
        super.onDestroy()
    }

}