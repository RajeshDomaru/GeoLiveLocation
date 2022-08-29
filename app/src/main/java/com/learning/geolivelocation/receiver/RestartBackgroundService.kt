package com.learning.geolivelocation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.learning.geolivelocation.service.LocationService
import com.learning.geolivelocation.util.Util

class RestartBackgroundService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        if (intent?.action.equals("RestartService")) {

            Log.i("Broadcast Listened", "Service tried to stop")

            Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show()

            val serviceIntent = Intent(context, LocationService::class.java)

            if (Util.isMyServiceRunning(serviceIntent.javaClass, context))
                context.stopService(serviceIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(serviceIntent)
            else context.startService(serviceIntent)

        }

    }

}