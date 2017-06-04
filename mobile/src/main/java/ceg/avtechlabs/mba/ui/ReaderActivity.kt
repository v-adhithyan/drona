package ceg.avtechlabs.mba.ui

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import ceg.avtechlabs.mba.R
import android.widget.Toast
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class ReaderActivity : AppCompatActivity() {
    var webview: WebView? = null
    var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        @TargetApi(21)
        window.exitTransition = Explode()

        url = intent.getStringExtra(DisplayActivity.URL)

        val webview = findViewById(R.id.webview) as WebView
        webview.showContextMenu()
        webview.settings.javaScriptEnabled = true
        webview.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                //progressDialog?.dismiss()
            }
        })
        webview.loadUrl(url)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_reader, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_reader_share -> { shareLink() }
            else -> { }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun shareLink() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.text_share, url))
        startActivity(Intent.createChooser(intent, resources.getString(R.string.intent_share)))
    }
}
