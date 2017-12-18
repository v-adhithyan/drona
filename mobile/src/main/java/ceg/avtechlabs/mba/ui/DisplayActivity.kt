package ceg.avtechlabs.mba.ui

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.view.View
import android.widget.Toast
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.models.DronaDBHelper
import ceg.avtechlabs.mba.util.FeedUtil
import ceg.avtechlabs.mba.util.internetAvailable
import cn.pedant.SweetAlert.SweetAlertDialog
import com.crazyhitty.chdev.ks.rssmanager.Channel
import com.crazyhitty.chdev.ks.rssmanager.RSS
import com.crazyhitty.chdev.ks.rssmanager.RssReader
import com.google.android.gms.ads.AdRequest
import com.yarolegovich.lovelydialog.LovelyInfoDialog
import kotlinx.android.synthetic.main.activity_display.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

class DisplayActivity : AppCompatActivity(), RssReader.RssCallback {

    //var url:String? = null
    var progressDialog: SweetAlertDialog? = null
    var urlArray = arrayOfNulls<String>(1)
    //var urlArray = emptyArray<String>(1)
    var rssReader: RssReader = RssReader(this)
    var topic = ""
    //var count = 0
    //var rss = ArrayList<RSS>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        @TargetApi(21)
        window.exitTransition = Explode()

        /*recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)*/

        topic = intent.getStringExtra(TOPIC)
        //title = topic + " feeds"

        if(topic.equals("Marketing")) {
            urlArray = resources.getStringArray(R.array.Marketing)
        } else if(topic.equals("Finance")) {
            urlArray = resources.getStringArray(R.array.Finance)
        } else if(topic.equals("Leadership")) {
            urlArray = resources.getStringArray(R.array.Leadership)
            //url = "http://guykawasaki.com/"
        } else if(topic.equals("Economics")) {
            urlArray = resources.getStringArray(R.array.Economics)
            //url = "http://freakonomics.com/blog/"
        } else if(topic.equals("Favorites")) {

            //finish()
        } else{
            urlArray = resources.getStringArray(R.array.Others)
            //url = "http://bobsutton.typepad.com/my_webl/"
        }

        //count = urlArray.size
        //url = "https://faculty.iima.ac.in/~jrvarma/blog/index.cgi/index.rss"

        if(internetAvailable()) {
            /*progressDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            progressDialog?.progressHelper!!.barColor = R.color.colorAccent
            progressDialog?.titleText = "Loading .."
            progressDialog?.setCancelable(false)
            progressDialog?.show()*/
            rssReader.loadFeeds(*urlArray)
        } else {
            LovelyInfoDialog(this)
                    .setTopColorRes(android.R.color.holo_blue_dark)
                    //This will add Don't show again checkbox to the dialog. You can pass any ID as argument
                    .setTitle(R.string.alert_no_internet_title)
                    .setMessage(R.string.alert_no_internet_message)
                    .show();
            finish()
        }

        adview_display.loadAd(AdRequest.Builder().build())
    }

    companion object {
        val TOPIC = "topic"
        val URL = "url"
        val TITLE = "title"
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun unableToReadRssFeeds(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        progressDialog?.dismiss()

        //When unable to load rss feeds, go back to main activity
        Toast.makeText(this, getString(R.string.toast_feeds_not_loaded), Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun rssFeedsLoaded(rssList: List<RSS>) {


        /*Thread {
            Logger.out(rssList[0].channel.items[0].title)
            val link = rssList[0].channel.items[0].link

            val article = Extractor(link).extract()
            Logger.out(article.title)
            Logger.out(article.imageUrl)
            Logger.out(article.document.text())*/

            runOnUiThread {

                val db = DronaDBHelper(this)
                //recycler_view.adapter = FeedsRecyclerViewAdapter(this, rssList)
                val intent = Intent(this, ReaderActivity::class.java)
                val items = ArrayList<Channel.Item>()
                //val title = rssList[0].channel.title
                val itemsTitle = ArrayList<String>();

                for (rss in rssList){
                    items.addAll(rss.channel.items)

                    for ( i in 0 .. rss.channel.items.size) {
                        itemsTitle.add(rss.channel.title)
                    }
                }

                var i = -1
                for(item in items) {
                    i++
                    var title = item.title
                    var description = item.description
                    if(title == null) {
                        continue
                    }
                    if(description == null) {
                        continue
                    }
                    if(db.feedExists(title, description)) {
                        if(!db.isFeedRead(title, description)) {
                            ReaderActivity.ITEMS.add(item)
                            ReaderActivity.TITLE.add(itemsTitle[i])
                        }
                    } else{
                        db.insertToFeeds(title, description, topic, 0)
                        ReaderActivity.ITEMS.add(item)
                        ReaderActivity.TITLE.add(itemsTitle[i])
                    }
                }


                progressDialog?.dismiss()
                if(ReaderActivity.ITEMS.size == 0) {
                    Toast.makeText(this, "No new feeds", Toast.LENGTH_LONG).show()
                    onBackPressed()
                    finish()
                } else {
                    intent.putExtra(TOPIC, topic)
                    //intent.putExtra(TITLE, title)
                    startActivity(intent)
                    finish()

                }

            }
        /*}.start()*/

        val title = rssList[0].channel.items[0].title
        if(!FeedUtil(this).isNewFeed(title, topic)) {
            Snackbar.make(activity_display as View, getString(R.string.snackbar_no_new_feeds), Snackbar.LENGTH_LONG).show()
        }
    }
}
