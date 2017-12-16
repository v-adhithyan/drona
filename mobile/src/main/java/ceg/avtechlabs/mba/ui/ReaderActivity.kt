package ceg.avtechlabs.mba.ui

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.text.Html
import android.transition.Explode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.widget.Toast
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.models.DronaDBHelper
import ceg.avtechlabs.mba.util.Extractor
import ceg.avtechlabs.mba.util.Globals
import ceg.avtechlabs.mba.util.getReadingTime
import cn.pedant.SweetAlert.SweetAlertDialog
import com.chimbori.crux.articles.Article
import com.crazyhitty.chdev.ks.rssmanager.Channel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_reader.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean


class ReaderActivity : AppCompatActivity() {

    var webview: WebView? = null
    var url = ""
    var i = 0
    var array = arrayOf("one", "two", "three")
    var adLoadCount = 0
    var currentArticle: Article? = null
    var db: DronaDBHelper? = null
    var category = ""
    //var title = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        @TargetApi(21)
        window.exitTransition = Explode()

        db = DronaDBHelper(this)
        category = intent.getStringExtra(DisplayActivity.TOPIC)

        if(savedInstanceState == null) {
            change()
        }

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

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putString(Globals.SAVE_TITLE, textviewTitle.text.toString())
        outState!!.putString(Globals.SAVE_CONTENT, textviewDescription.text.toString())
        outState!!.putString(Globals.SAVE_IMAGE_URL, textviewDescription.tag.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        textviewTitle.text = savedInstanceState!!.getString(Globals.SAVE_TITLE)
        textviewDescription.text = savedInstanceState!!.getString(Globals.SAVE_IMAGE_URL)
        loadImage(savedInstanceState!!.getString(Globals.SAVE_IMAGE_URL))
        super.onRestoreInstanceState(savedInstanceState)
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
        if(i == 0) {
            try {
                Observable.just<String>("")
                        .map { s -> doInBackground(s) }
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { preExecute() }
                        .subscribe { integer -> postExecute(integer!!) }
            } catch (ex: Exception) {
                Toast.makeText(this@ReaderActivity, "Unknown error occured. Please try again", Toast.LENGTH_LONG).show()
                finish()

            }

        } else {
            try {
                Observable.just<Unit>(Unit)
                        .map { checkPrefetcher() }
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe{showProgressDialog()}
                        .subscribe { a-> populateArticle(a) }
            } catch (ex: Exception) {
                Toast.makeText(this@ReaderActivity, "Unknown error occured. Please try again", Toast.LENGTH_LONG).show()
                finish()
            }

        }


    }

    fun open(v: View) {

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
        finish()
    }

    companion object {
        var ITEMS = ArrayList<Channel.Item>()
        var TITLE = ArrayList<String>()
        var fetched = AtomicBoolean()
        var progressDialog:SweetAlertDialog? = null
    }

    fun preExecute() {
        if(i == ITEMS!!.size) {
            Toast.makeText(this, "No more items", Toast.LENGTH_LONG).show()
        } else {
            collapsing_toolbar.title = TITLE[i]
            textviewTitle.text = ITEMS[i].title
            textviewDescription.text = ITEMS[i].description
            textviewDate.text = ITEMS[i].pubDate
           showProgressDialog()

        }
    }

    fun runExtractor(): Array<String> {
        var content = ""
        var title = ""
        if(i < ITEMS.size) {
            try {
                currentArticle = Extractor(ITEMS[i].link).extract()
                content = Html.fromHtml(currentArticle!!.document.text()).toString()
                title = currentArticle!!.title
            } catch(ex: Exception) {
                runOnUiThread {

                    Toast.makeText(this@ReaderActivity, getString(R.string.toast_feeds_not_loaded), Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
        return arrayOf(title, content)
    }

    fun doInBackground(temp: String): Array<String> {
        return runExtractor()
    }

    fun postExecute(array: Array<String>) {
        try{
            db = DronaDBHelper(this@ReaderActivity)
            db!!.markFeedAsRead(ITEMS[i].title, ITEMS[i].description)
        } catch (ex: Exception) {
            db!!.close()
        }

        textviewTitle.text = array[0]
        textviewDescription.text = array[1]
        textviewDescription.tag = currentArticle!!.imageUrl

        loadImage(currentArticle!!.imageUrl)

        hideProgressDialog()

        AnimationUtils.loadAnimation(this, R.anim.slide_right)
        Snackbar.make(coordinatorLayoutReader, getReadingTime(array[1]), Snackbar.LENGTH_LONG).show()

        Toast.makeText(this, "${ITEMS.size - i - 1} unread stories remaining ..", Toast.LENGTH_SHORT).show()
        i = i + 1

        executePrefetcher()
    }

    fun fetchNextArticle(value: String) {
        fetched.set(false)

        if(i < ITEMS.size) {
            try {
                currentArticle = Extractor(ITEMS[i].link).extract()
            } catch(ex: Exception) {
                runOnUiThread {

                    Toast.makeText(this@ReaderActivity, getString(R.string.toast_feeds_not_loaded), Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    fun fetchedNextArticle() {
        fetched.set(true)
    }

    fun checkPrefetcher(): Array<String> {
        while(true) {
            if(!fetched.get()){
                Thread.sleep(500)
            } else {
                break
            }
        }
        try {
            Thread.sleep(100)
        } catch (ex: Exception) {

        }
        val content = Html.fromHtml(currentArticle!!.document.text()).toString()
        val title = currentArticle!!.title
        return arrayOf(title, content)
    }

    fun populateArticle(array: Array<String>) {
        postExecute(array)
        scroll.scrollTo(0,0)
    }

    fun executePrefetcher() {
        Observable.just<String>("")
                .map { s -> fetchNextArticle(s) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe { fetchedNextArticle() }
    }

    fun loadImage(imageUrl: String) {
        Picasso.with(this).load(imageUrl).into(object: com.squareup.picasso.Target {
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
    }

    private fun showProgressDialog() {
        progressDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog!!.progressHelper!!.barColor = R.color.colorAccent
        progressDialog!!.titleText = getString(R.string.loading)
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    private fun hideProgressDialog() {
        progressDialog!!.hide()
    }

}
