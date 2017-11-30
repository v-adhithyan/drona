package ceg.avtechlabs.mba.notification

import android.app.NotificationManager
import android.content.Context
import android.support.v7.app.NotificationCompat
import ceg.avtechlabs.mba.R

/**
 * Created by Adhithyan V on 30-11-2017.
 */
class NotificationUtil(internal var context: Context) {

    fun showNotification(title: String, message: String) {
        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.stat_notify)
                .setContentTitle(title)
                .setContentText(message)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(100, mBuilder.build())
    }

}