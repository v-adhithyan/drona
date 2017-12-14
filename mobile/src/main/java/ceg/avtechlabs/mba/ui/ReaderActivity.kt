package ceg.avtechlabs.mba.ui

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.text.Html
import android.transition.Explode
import android.view.*
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.widget.ProgressBar
import ceg.avtechlabs.mba.R
import android.widget.Toast
import ceg.avtechlabs.mba.listeners.SwypeListener
import ceg.avtechlabs.mba.models.DronaDBHelper
import ceg.avtechlabs.mba.util.*
import cn.pedant.SweetAlert.SweetAlertDialog
import com.chimbori.crux.articles.Article
import com.crazyhitty.chdev.ks.rssmanager.Channel
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_reader.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action0
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*


class ReaderActivity : AppCompatActivity() {

    var webview: WebView? = null
    var url = ""
    var i = 0
    var array = arrayOf("one", "two", "three")
    var adLoadCount = 0
    var currentArticle: Article? = null
    var nextArticle: Article? = null
    var englishLocale = true //assume default is english
    var db: DronaDBHelper? = null
    var category = ""
    //var title = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        @TargetApi(21)
        window.exitTransition = Explode()



        val context = this as Context
        //englishLocale = isEnglishLocale()
        /*scroll
                .setOnTouchListener(object:SwypeListener(context) {
            override fun onSwipeRight() { change() }
            override fun onSwipeLeft() { change() }
            override fun onSwipeTop() { }
            override fun onSwipeBottom() { }
        });*/
        //changeToEnglishLocale()
        db = DronaDBHelper(this)
        category = intent.getStringExtra(DisplayActivity.TOPIC)
        //title = intent.getStringExtra(DisplayActivity.TITLE)
        change()
        /*url = intent.getStringExtra(DisplayActivity.URL)
        val webview = findViewById(R.id.webview) as WebView
        webview.showContextMenu()
        webview.settings.javaScriptEnabled = true
        webview.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                //progressDialog?.dismiss()
            }
        })
        webview.loadUrl(url)*/
        //collapsing_toolbar.title = "Drona"

        //textviewDescription.setLineSpacing(1.1F, 1.0F)
        //textviewTitle.setLineSpacing(1.2F, 1.0F)


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
        val j = i - 1
        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.text_share, ITEMS[j].link))
        startActivity(Intent.createChooser(intent, resources.getString(R.string.intent_share)))
    }

    fun change() {
        Observable.just<String>("")
                .map { s -> doInBackground(s) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { preExecute() }
                .subscribe { integer -> postExecute(integer!!) }

    }

    fun open(v: View) {
        //Toast.makeText(this, "open", Toast.LENGTH_LONG).show()
        val url = ITEMS[i-1].link
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    fun nextArticle(v: View) {
        adLoadCount++
        if(adLoadCount == 3) {
            loadInterstitialAd()
            adLoadCount = 0
        }
        change()
    }

    fun share(v: View) {
        //Toast.makeText(this, "share", Toast.LENGTH_LONG).show()
        shareLink()
    }

    fun loadInterstitialAd() {
        val interstitial = InterstitialAd(this)
        interstitial.adUnitId = getString(R.string.ad_unit_interstitial)
        val adRequest = AdRequest.Builder()
        interstitial.loadAd(adRequest.build())
        interstitial.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Call displayInterstitial() function
                if(interstitial.isLoaded) {
                    interstitial.show()
                }

            }
        }
    }

    override fun onBackPressed() {
        ITEMS = ArrayList<Channel.Item>()
        TITLE = ArrayList<String>()
        //if(!englishLocale) {
        //resetLocale()
        // }
        finish()
    }

    companion object {
        //var rss: List<RSS>? = null
        var ITEMS = ArrayList<Channel.Item>()
        var TITLE = ArrayList<String>()
        var exed = false
        var progressDialog:SweetAlertDialog? = null
    }

    fun preExecute() {
        if(i == ITEMS!!.size) {
            Toast.makeText(this, "No more items", Toast.LENGTH_LONG).show()
        } else {
            collapsing_toolbar.title = TITLE[i]
            //collapsing_toolbar.title = ITEMS[i].title
            textviewTitle.text = ITEMS[i].title
            textviewDescription.text = ITEMS[i].description
            textviewDate.text = ITEMS[i].pubDate
            //Toast/.makeText(this, "${ITEMS!!.size - i - 1} items left", Toast.LENGTH_SHORT).show()
            //i = i+1
            progressDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            //val progressDialog = ProgressDialog(this)
            progressDialog!!.progressHelper!!.barColor = R.color.colorAccent
            progressDialog!!.titleText = getString(R.string.loading)
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }
    }

    fun doInBackground(temp: String): Int {
        db!!.markFeedAsRead(ITEMS[i].title, ITEMS[i].description)

        if(i < ITEMS.size) {
            try {
                currentArticle = Extractor(ITEMS[i].link).extract()
                //nextArticle = Extractor(ITEMS[i+1].link).extract()
            } catch(ex: Exception) {
                runOnUiThread {

                    Toast.makeText(this@ReaderActivity, getString(R.string.toast_feeds_not_loaded), Toast.LENGTH_LONG).show()
                    finish()
                }
            }

            //Looper.prepare()

        }
        return 0

    }

    fun postExecute(temp: Int) {
        textviewTitle.text = (currentArticle!!.title).toUpperCase()
        textviewTitle.setAllCaps(true)
        var readingTime = ""
        if(currentArticle!!.document.text().length == 0) {
            textviewDescription.text = ITEMS[i].description
            Toast.makeText(this, getString(R.string.read_more), Toast.LENGTH_LONG).show()
        } else {
            readingTime = getReadingTime(currentArticle!!.document.text())
            textviewDescription.text = Html.fromHtml(currentArticle!!.document.text())
        }
        Picasso.with(this).load(currentArticle!!.imageUrl).into(object: com.squareup.picasso.Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {

            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                image.setImageBitmap(bitmap)
                Palette.from(bitmap).generate(object: Palette.PaletteAsyncListener {
                    override fun onGenerated(palette: Palette) {
                        val mutedColor = palette.getMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        val mutedDarkColor = palette.getDarkMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

                        collapsing_toolbar.setContentScrimColor(mutedColor);
                        collapsing_toolbar.setStatusBarScrimColor(mutedDarkColor);
                        @TargetApi(21)
                        window.statusBarColor = palette.getDarkVibrantColor(resources.getColor(R.color.colorPrimaryDark))
                    }
                });
            }
        })


        //progressBar.visibility = ProgressBar.INVISIBLE
        progressDialog!!.dismissWithAnimation()
        AnimationUtils.loadAnimation(this, R.anim.slide_right)
        Snackbar.make(coordinatorLayoutReader, readingTime, Snackbar.LENGTH_LONG).show()
        Toast.makeText(this, "${ITEMS.size - i - 1} unread stories remaining ..", Toast.LENGTH_SHORT).show()
        i = i + 1
    }
}
