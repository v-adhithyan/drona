package ceg.avtechlabs.mba.ui

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import ceg.avtechlabs.mba.R
import android.widget.Toast



class ReaderActivity : AppCompatActivity() {
    var webview: WebView? = null
    var url:String? = null
    var urlArray: Array<String> = arrayOf("")
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        val topic = intent.getStringExtra(TOPIC)
        title = topic + " feeds"

        if(topic.equals("Marketing")) {
            urlArray = resources.getStringArray(R.string.topic_marketing)
            url = "http://madovermarketing.com/"
        } else if(topic.equals("Finance")) {
            urlArray = resources.getStringArray(R.string.topic_finance)
            url = "http://www.iimahd.ernet.in/~jrvarma/blog/index.cgi"
        } else if(topic.equals("Leadership")) {
            urlArray = resources.getStringArray(R.string.topic_leadership)
            url = "http://guykawasaki.com/"
        } else if(topic.equals("Economics")) {
            urlArray = resources.getStringArray(R.string.topic_economics)
            url = "http://freakonomics.com/blog/"
        } else {
            urlArray = resources.getStringArray(R.string.topic_others)
            url = "http://bobsutton.typepad.com/my_webl/"
        }

        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Please wait ..")

        progressDialog?.show()
        val webview = findViewById(R.id.webview) as WebView
        webview.showContextMenu()
        webview.settings.javaScriptEnabled = true
        webview.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressDialog?.dismiss()
            }
        })
        webview.loadUrl(url)

    }

    companion object {
        val TOPIC = "topic"
    }

}
