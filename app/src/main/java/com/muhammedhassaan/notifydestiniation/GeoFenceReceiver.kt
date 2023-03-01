package com.muhammedhassaan.notifydestiniation

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeoFenceReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val address = intent.getStringExtra("Address")
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                Log.e("TAG", "Geofencing error: ${geofencingEvent.errorCode}")
                return
            }
        }
        // Get the geofence transition type
        val geofenceTransition = geofencingEvent?.geofenceTransition

        val mapsIntent = Intent(context,MapsActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            mapsIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        // Check if the user has entered the geofence
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_location)
                .setContentTitle("Arrived Destination")
                .setContentText("You have arrived to $address")
                .setContentIntent(activityPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true) // set the notification to cancel when clicked
                .build()

            notificationManager.notify(0, notification)
        }
    }
}