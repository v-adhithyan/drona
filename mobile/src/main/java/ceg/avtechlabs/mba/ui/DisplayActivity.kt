package ceg.avtechlabs.mba.ui

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.adapters.FeedsRecyclerViewAdapter
import ceg.avtechlabs.mba.models.AdapterObject
import ceg.avtechlabs.mba.util.Logger
import ceg.avtechlabs.mba.util.internetAvailable
import cn.pedant.SweetAlert.SweetAlertDialog
import com.crazyhitty.chdev.ks.rssmanager.RSS
import com.crazyhitty.chdev.ks.rssmanager.RssReader
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_display.*
import okhttp3.*
import java.io.IOException
import java.util.*

class DisplayActivity : AppCompatActivity(), RssReader.RssCallback {

    //var url:String? = null
    var progressDialog: ProgressDialog? = null
    var urlArray = arrayOfNulls<String>(1)
    var rssReader: RssReader = RssReader(this)
    //var count = 0
    //var rss = ArrayList<RSS>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)

        val topic = intent.getStringExtra(TOPIC)
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
            progressDialog = ProgressDialog(this)
            progressDialog?.setMessage("Please wait ..")
            progressDialog?.show()
            rssReader.loadFeeds(*urlArray)
        } else {
            val alert = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            alert.setTitle("Oops")
            alert.setContentText(getString(R.string.alert_no_internet))
            alert.show()
        }

    }

    companion object {
        val TOPIC = "topic"
        val URL = "url"
    }


    override fun unableToReadRssFeeds(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun rssFeedsLoaded(rssList: List<RSS>) {
        runOnUiThread {
            progressDialog?.dismiss()
            recycler_view.adapter = FeedsRecyclerViewAdapter(this, rssList)
        }
    }
}
