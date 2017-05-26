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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        val url = intent.getStringExtra(DisplayActivity.URL)

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
}
