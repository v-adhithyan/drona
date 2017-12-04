package ceg.avtechlabs.mba.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.NotificationCompat
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.ui.ReaderFromNotificationActivity
import java.util.*

/**
 * Created by Adhithyan V on 30-11-2017.
 */
class NotificationUtil(internal var context: Context) {

    fun showNotification(title: String, message: String) {
        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.stat_notify)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Random().nextInt(), mBuilder.build())
    }


    fun showNotificationWithURL(title: String, message: String, url: String, date: String) {
        val intent = Intent(context, ReaderFromNotificationActivity::class.java)
        intent.putExtra(ReaderFromNotificationActivity.INTENT_READ_TITLE, title)
        intent.putExtra(ReaderFromNotificationActivity.INTENT_READ_DESC, message)
        intent.putExtra(ReaderFromNotificationActivity.INTENT_PUB_DATA, date)
        intent.putExtra(ReaderFromNotificationActivity.INTENT_READ_URl, url)

        val pendingIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, 0)

        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.stat_notify)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.next, context.getString(R.string.notification_action_read), pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Random().nextInt(), mBuilder.build())
    }

}