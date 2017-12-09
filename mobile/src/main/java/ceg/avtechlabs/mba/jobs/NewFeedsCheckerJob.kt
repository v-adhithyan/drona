package ceg.avtechlabs.mba.jobs

import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.models.DronaDBHelper
import ceg.avtechlabs.mba.notification.NotificationUtil
import ceg.avtechlabs.mba.util.Globals
import ceg.avtechlabs.mba.util.getPreference
import ceg.avtechlabs.mba.util.internetAvailable
import com.crazyhitty.chdev.ks.rssmanager.Channel
import com.crazyhitty.chdev.ks.rssmanager.RSS
import com.crazyhitty.chdev.ks.rssmanager.RssReader
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Adhithyan V on 30-11-2017.
 */

class NewFeedsCheckerJob: Job(), RssReader.RssCallback {
    override fun unableToReadRssFeeds(errorMessage: String?) {
        //NotificationUtil(context).showNotification("Failed", errorMessage!! + "msg")
    }

    override fun rssFeedsLoaded(rssList: MutableList<RSS>) {
        val items = ArrayList<Channel.Item>()
        for(rss in rssList) {
            items.addAll(rss.channel.items)
        }
        //NotificationUtil(context).showNotification("New note", "af")
        //NotificationUtil(context).showNotification(items[0].title, items[0].description)
        val unread = ArrayList<Channel.Item>()
        val db = DronaDBHelper(context)
        for(item in items) {
            if(item.title == null) { continue }
            if(item.description == null) { continue }
            if(!db.feedExists(item.title, item.description)) {
                unread.add(item)
                db.insertToFeeds(item.title, item.description, 0)
            }
        }

        var n = 5
        if(unread.size >= 5) {
            n = 5
        } else {
            n = unread.size
        }

        for( i in 0..n-1) {
            if(unread[i].title == null) { continue }
            if(unread[i].description == null) { continue }
            NotificationUtil(context).showNotificationWithURL(unread[i].title, unread[i].description, unread[i].link, unread[i].pubDate)
        }
    }

    companion object {
        val TAG = "new_feed_checker_job"

        public fun scheduleJob() {
            JobRequest.Builder(NewFeedsCheckerJob.TAG)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                    .setUpdateCurrent(true)
                    .build()
                    .schedule()
        }
    }

    override fun onRunJob(params: Params): Result {
        var array = ArrayList<String>();

        if(context.internetAvailable()) {
            val categories = (context.getPreference(Globals.FEED_PREFERENCES).toString()).split(",")

            for (cat in categories) {
                if(cat.equals(context.getString(R.string.topic_finance))) {
                    array.addAll(context.resources.getStringArray(R.array.Finance))
                } else if(cat.equals(context.getString(R.string.topic_economics))) {
                    array.addAll(context.resources.getStringArray(R.array.Economics))
                } else if(cat.equals(context.getString(R.string.topic_leadership))) {
                    array.addAll(context.resources.getStringArray(R.array.Leadership))
                } else if(cat.equals(context.getString(R.string.topic_marketing))) {
                    array.addAll(context.resources.getStringArray(R.array.Marketing))
                } else if(cat.equals(context.getString(R.string.topic_others))) {
                    array.addAll(context.resources.getStringArray(R.array.Others))
                }
            }

            var urlArray = arrayOfNulls<String>(array.size)
            array.toArray(urlArray)
            RssReader(this).loadFeeds(*urlArray)
        }

        return Result.SUCCESS
    }


}
