package ceg.avtechlabs.mba.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.support.v7.app.NotificationCompat
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.ui.MainActivity
import ceg.avtechlabs.mba.util.Logger


class MessagingService : FirebaseMessagingService() {

    /*override fun onMessageReceived(message: RemoteMessage?) {
        message?.notification!!.body?.let {
            sendNotification(it)
            Logger.out(it)
        }
    }

    private fun sendNotification(message: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Drona")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }*/
}
