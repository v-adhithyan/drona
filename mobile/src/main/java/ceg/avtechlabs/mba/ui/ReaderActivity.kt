package ceg.avtechlabs.mba.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.R.id.webview

class ReaderActivity : AppCompatActivity() {
    //var webview: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        val webview = R.id.webview as WebView
        webview.settings.javaScriptEnabled = true
        webview.loadUrl("http://www.iimahd.ernet.in/~jrvarma/blog/index.cgi")

    }
}
