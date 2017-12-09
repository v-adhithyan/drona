package ceg.avtechlabs.mba.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
        //notificationManager.notify(Random().nextInt(), buildSummary())
        notificationManager.notify(Random().nextInt(), mBuilder.build())
    }


    fun showNotificationWithURL(title: String, message: String, url: String, date: String, source: String) {
        val intent = Intent(context, ReaderFromNotificationActivity::class.java)
        intent.putExtra(ReaderFromNotificationActivity.INTENT_READ_TITLE, title)
        intent.putExtra(ReaderFromNotificationActivity.INTENT_READ_DESC, message)
        intent.putExtra(ReaderFromNotificationActivity.INTENT_PUB_DATA, date)
        intent.putExtra(ReaderFromNotificationActivity.INTENT_READ_URl, url)
        intent.putExtra(ReaderFromNotificationActivity.INTENT_SOURCE, source)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.stat_notify)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(GROUP_NAME)
                .addAction(R.drawable.next, context.getString(R.string.notification_action_read), pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //notificationManager.notify(GROUP_ID, buildSummary())
        notificationManager.notify(Random().nextInt(), mBuilder.build())
    }

    private fun buildSummary(): Notification {
        return NotificationCompat.Builder(context)
                .setColor(Color.RED)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_summary_text))
                .setSmallIcon(R.drawable.stat_notify)
                .setGroup(GROUP_NAME)
                .setGroupSummary(true)
                .build()
    }

    companion object {
        val GROUP_ID = 3592
        val GROUP_NAME = "DRONA"
    }
}