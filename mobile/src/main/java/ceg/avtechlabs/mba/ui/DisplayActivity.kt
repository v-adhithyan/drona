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
import ceg.avtechlabs.mba.adapters.RecyclerViewAdapter
import ceg.avtechlabs.mba.models.AdapterObject
import ceg.avtechlabs.mba.util.Logger
import com.crazyhitty.chdev.ks.rssmanager.RSS
import com.crazyhitty.chdev.ks.rssmanager.RssReader
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_display.*
import okhttp3.*
import java.io.IOException
import java.util.*

class DisplayActivity : AppCompatActivity(), RssReader.RssCallback {

    var url:String? = null
    var urlArray: Array<String> = arrayOf("")
    var progressDialog: ProgressDialog? = null
    var layoutManager: LinearLayoutManager? = null

    var rssReader: RssReader = RssReader(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = layoutManager

        val topic = intent.getStringExtra(TOPIC)
        title = topic + " feeds"

        if(topic.equals("Marketing")) {
            urlArray = resources.getStringArray(R.array.Marketing)
            url = "http://madovermarketing.com/"
        } else if(topic.equals("Finance")) {
            urlArray = resources.getStringArray(R.array.Finance)
            url = urlArray[0]
        } else if(topic.equals("Leadership")) {
            urlArray = resources.getStringArray(R.array.Leadership)
            url = "http://guykawasaki.com/"
        } else if(topic.equals("Economics")) {
            urlArray = resources.getStringArray(R.array.Economics)
            url = "http://freakonomics.com/blog/"
        } else {
            urlArray = resources.getStringArray(R.array.Others)
            url = "http://bobsutton.typepad.com/my_webl/"
        }

        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Please wait ..")
        //progressDialog?.show()
        //rssReader.loadFeeds(url)

        val array = ArrayList<AdapterObject>()
        array.add(AdapterObject("hello", "1"))
        array.add(AdapterObject("afdadf", "2"))
        recycler_view.adapter = RecyclerViewAdapter(array)
    }

    companion object {
        val TOPIC = "topic"
    }


    override fun unableToReadRssFeeds(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun rssFeedsLoaded(rssList: MutableList<RSS>?) {

    }
}
