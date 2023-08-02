package com.developer.cory.Model

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

import com.developer.cory.MyApplication
import com.developer.cory.MyApplication.Companion.CHANNEL_ID
import com.developer.cory.R

class pushNotification {
    fun sendNotification(context:Context,title:String,description:String){
        val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo)
        // Create the notification
        val notificationBuilder = context.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(description)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.icons8_notification_35)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        // Notify the user
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())

    }
}