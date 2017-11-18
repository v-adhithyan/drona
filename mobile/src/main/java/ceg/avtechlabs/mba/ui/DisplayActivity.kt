package ceg.avtechlabs.mba.ui

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.transition.Explode
import android.util.Log
import android.view.View
import android.widget.Toast
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.adapters.FeedsRecyclerViewAdapter
import ceg.avtechlabs.mba.models.MbaDbHelper
import ceg.avtechlabs.mba.util.Extractor
import ceg.avtechlabs.mba.util.Logger
import ceg.avtechlabs.mba.util.internetAvailable
import cn.pedant.SweetAlert.SweetAlertDialog
import com.crazyhitty.chdev.ks.rssmanager.RSS
import com.crazyhitty.chdev.ks.rssmanager.RssReader
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_display.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.Reader

class DisplayActivity : AppCompatActivity(), RssReader.RssCallback {

    //var url:String? = null
    var progressDialog: SweetAlertDialog? = null
    var urlArray = arrayOfNulls<String>(1)
    var rssReader: RssReader = RssReader(this)
    var topic = ""
    //var count = 0
    //var rss = ArrayList<RSS>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        @TargetApi(21)
        window.exitTransition = Explode()

        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)

        topic = intent.getStringExtra(TOPIC)
        title = topic + " feeds"

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
        } else {
            urlArray = resources.getStringArray(R.array.Others)
            //url = "http://bobsutton.typepad.com/my_webl/"
        }

        //count = urlArray.size
        //url = "https://faculty.iima.ac.in/~jrvarma/blog/index.cgi/index.rss"

        if(internetAvailable()) {
            progressDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            progressDialog?.progressHelper!!.barColor = R.color.colorAccent
            progressDialog?.titleText = "Loading .."
            progressDialog?.setCancelable(false)
            progressDialog?.show()
            rssReader.loadFeeds(*urlArray)
        } else {
            val alert = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            alert.setTitle("Oops")
            alert.setContentText(getString(R.string.alert_no_internet))
            alert.show()
        }

        adview_display.loadAd(AdRequest.Builder().build())
    }

    companion object {
        val TOPIC = "topic"
        val URL = "url"

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun unableToReadRssFeeds(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        progressDialog?.dismiss()

        //When unable to load rss feeds, go back to main activity
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


                //recycler_view.adapter = FeedsRecyclerViewAdapter(this, rssList)
                val intent = Intent(this, ReaderActivity::class.java)
                for (rss in rssList) {
                    ReaderActivity.ITEMS.addAll(rss.channel.items)
                }
                progressDialog?.dismiss()
                startActivity(intent)
            }
        /*}.start()*/


        val db = MbaDbHelper(this)
        val insert = db.insert(rssList[0].channel.items[0].title, topic)
        if(!insert) {
            Snackbar.make(activity_display as View, getString(R.string.snackbar_no_new_feeds), Snackbar.LENGTH_LONG).show()
        }
    }

}
