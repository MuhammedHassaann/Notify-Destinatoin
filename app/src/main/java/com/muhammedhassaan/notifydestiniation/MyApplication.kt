package com.muhammedhassaan.notifydestiniation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        //creating the notification channel on the onCreate
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        //From android Oreo onwards we'll create a notification channel
        //it didn't exist before that
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                "Counter",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            //Setting a description for the notification channel
            //that appears on the app settings
            channel.description = "Used to notify the user about reaching the desired destination"

            //creating the notification by getting a reference of the NotificationManager
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}