package com.madlab.servicesdemo.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.madlab.servicesdemo.R

class MyService : Service() {

    private var notificationManager: NotificationManager? = null
    private val notificationId = 1
    private val myBinder = MyBinder()

    override fun onBind(intent: Intent?): IBinder? {
        Toast.makeText(this, "service starting OnBind", Toast.LENGTH_SHORT).show()
        return myBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting Start id:- $startId", Toast.LENGTH_SHORT).show()

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        createNotification()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() {

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId =
            "${applicationContext.packageName}-${applicationContext.getString(R.string.app_name)}"

        val notificationChannel = NotificationChannel(
            channelId,
            "Service Notification",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.WHITE

        notificationChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(notificationChannel)

        val pendingIntent: PendingIntent =
            Intent(this, MyService::class.java).let {
                PendingIntent.getActivity(this, 0, it, 0)
            }

        val notification =
            Notification.Builder(this, channelId)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_message))
                .setSmallIcon(android.R.drawable.ic_secure)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.ticker_text))
                .build()

        startForeground(notificationId, notification)

    }

    override fun onDestroy() {
        Toast.makeText(this, "Service Destroy", Toast.LENGTH_LONG).show()
        notificationManager?.cancel(notificationId)
    }

    inner class MyBinder : Binder() {
        fun getService(): MyService = MyService()
    }
}