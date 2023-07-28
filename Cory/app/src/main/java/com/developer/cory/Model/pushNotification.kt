package com.developer.cory.Model

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

import com.developer.cory.MyApplication
import com.developer.cory.MyApplication.Companion.CHANNEL_ID
import com.developer.cory.R

class pushNotification {
    fun sendNotification(context:Context){
        // Create the notification
        val notificationBuilder = context.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentTitle("Xin chòa")
                .setContentText("Thông báo đây")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        // Notify the user
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())

    }
}