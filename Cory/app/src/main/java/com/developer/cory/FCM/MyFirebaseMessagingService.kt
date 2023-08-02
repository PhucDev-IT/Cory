package com.developer.cory.FCM


import com.developer.cory.Model.pushNotification
import com.developer.cory.data_local.DataLocalManager

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notification: RemoteMessage.Notification = message.notification ?: return

        val strTitle:String = notification.title?:""
        val strMessenge = notification.body?:""

        pushNotification().sendNotification(this,strTitle,strMessenge)


    }

        override fun onNewToken(token: String) {
            super.onNewToken(token)
            DataLocalManager.saveTokenMessing(token)
        }


}