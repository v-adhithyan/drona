package ceg.avtechlabs.mba.ui

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.transition.Explode
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.fragments.ReaderFragment
import ceg.avtechlabs.mba.models.DronaDBHelper
import ceg.avtechlabs.mba.util.Extractor
import ceg.avtechlabs.mba.util.Globals
import cn.pedant.SweetAlert.SweetAlertDialog
import com.chimbori.crux.articles.Article
import com.crazyhitty.chdev.ks.rssmanager.Channel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean


class TempActivity : AppCompatActivity() {

    var webview: WebView? = null
    var url = ""
    var i = 0
    //var array = arrayOf("one", "two", "three")
    var adLoadCount = 0
    var currentArticle: Article? = null
    var db: DronaDBHelper? = null
    var category = ""
    //var title = ""

    private val handler = object : Handler() {

        override fun handleMessage(msg: Message) {
            hideProgressDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        @TargetApi(21)
        window.exitTransition = Explode()

        db = DronaDBHelper(this)
        category = intent.getStringExtra(DisplayActivity.TOPIC)

        change()
        /*if(savedInstanceState == null) {
            change()
        } else {
            Log.d("SHOWPROGRESS", "Esle")
            showProgressDialog()
        }*/

    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_reader, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_reader_share -> { shareLink() }
            else -> { }
        }
        return super.onOptionsItemSelected(item)
    }*/



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
                Toast.makeText(this@TempActivity, "Unknown error occured. Please try again", Toast.LENGTH_LONG).show()
                onBackPressed()

            }

        } else if( i < ITEMS!!.size){
            try {
                Observable.just<Unit>(Unit)
                        .map { checkPrefetcher() }
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe{showProgressDialog()}
                        .subscribe { a-> populateArticle(a) }
            } catch (ex: Exception) {
                Toast.makeText(this@TempActivity, "Unknown error occured. Please try again", Toast.LENGTH_LONG).show()
                onBackPressed()
            }

        } else {
            Toast.makeText(this, getString(R.string.toast_no_stories_left), Toast.LENGTH_LONG).show()
            onBackPressed()
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
        //onBackPressed()
    }

    companion object {
        var ITEMS = ArrayList<Channel.Item>()
        var TITLE = ArrayList<String>()
        var fetched = AtomicBoolean()
        var CONTENT = ""
        var progressDialog:SweetAlertDialog? = null
    }

    fun preExecute() {
        if(i == ITEMS!!.size) {
            Toast.makeText(this, "No more items", Toast.LENGTH_LONG).show()
        } else {
            /*collapsing_toolbar.title = TITLE[i]
            textviewTitle.text = ITEMS[i].title
            textviewDescription.text = ITEMS[i].description
            textviewDate.text = ITEMS[i].pubDate*/
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
                CONTENT = content
                //Looper.prepare()
                //handler.sendMessage(handler.obtainMessage())
                //Looper.loop()
                title = currentArticle!!.title
            } catch(ex: Exception) {
                runOnUiThread {

                    Toast.makeText(this@TempActivity, getString(R.string.toast_feeds_not_loaded), Toast.LENGTH_LONG).show()
                    onBackPressed()
                }
            }
        }
        val imageUrl = currentArticle!!.imageUrl
        val bundle = Bundle()
        bundle.putString(Globals.SAVE_TITLE, currentArticle!!.title)
        bundle.putString(Globals.SAVE_CONTENT, CONTENT)
        bundle.putString(Globals.SAVE_IMAGE_URL, imageUrl)


        val fragment = ReaderFragment()
        fragment.arguments = bundle
        handler.sendMessage(handler.obtainMessage())
        //fragmentManager.beginTransaction().add(R.id.frame, fragment).commit()
        return arrayOf(title, content)
    }

    fun doInBackground(temp: String): Array<String> {
        return runExtractor()
    }

    fun postExecute(array: Array<String>) {
        Thread{
            try{
                db = DronaDBHelper(this@TempActivity)
                db!!.markFeedAsRead(ITEMS[i].title, ITEMS[i].description)
            } catch (ex: Exception) {
                db!!.close()
            }
        }.start()

        //textviewTitle.text = array[0]
        //textviewDescription.text = array[1]
        //textviewDescription.movementMethod = ScrollingMovementMethod



        handler.sendMessage(handler.obtainMessage())

        /*loadImage(currentArticle!!.imageUrl)
        scroll.scrollTo(0,0)
        hideProgressDialog()

        AnimationUtils.loadAnimation(this, R.anim.slide_right)*/
        //Snackbar.make(coordinatorLayoutReader, getReadingTime(CONTENT), Snackbar.LENGTH_LONG).show()

        Toast.makeText(this, "${ITEMS.size - i - 1} unread stories remaining ..", Toast.LENGTH_SHORT).show()
        i = i + 1

        executePrefetcher()
    }

    fun fetchNextArticle(value: String) {
        fetched.set(false)

        if(i < ITEMS.size) {
            try {
                currentArticle = Extractor(ITEMS[i].link).extract()
                CONTENT = Html.fromHtml(currentArticle!!.document.text()).toString()
            } catch(ex: Exception) {
                runOnUiThread {

                    Toast.makeText(this@TempActivity, getString(R.string.toast_feeds_not_loaded), Toast.LENGTH_LONG).show()
                    onBackPressed()
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
            Thread.sleep(500)
        } catch (ex: Exception) {

        }
        //handler.sendMessage(handler.obtainMessage())
        val title = currentArticle!!.title
        val imageUrl = currentArticle!!.imageUrl
        val bundle = Bundle()
        bundle.putString(Globals.SAVE_TITLE, title)
        bundle.putString(Globals.SAVE_CONTENT, CONTENT)
        bundle.putString(Globals.SAVE_IMAGE_URL, imageUrl)

        val fragment = ReaderFragment()
        fragment.arguments = bundle
        //fragmentManager.beginTransaction().add(R.id.frame, fragment).commit()
        return arrayOf(title, CONTENT)
    }

    fun populateArticle(array: Array<String>) {
        postExecute(array)
        //scroll.scrollTo(0,0)
    }

    fun executePrefetcher() {
        Observable.just<String>("")
                .map { s -> fetchNextArticle(s) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe { fetchedNextArticle() }
    }

    /*fun loadImage(imageUrl: String?) {

        if(imageUrl == null) {
            return;
        } else if(imageUrl.equals("")){
            return;
        }

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
    }*/

    private fun showProgressDialog() {
        try{
            progressDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            progressDialog!!.progressHelper!!.barColor = R.color.colorAccent
            progressDialog!!.titleText = getString(R.string.loading)
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        } catch (ex: Exception) {

        }

    }

    private fun hideProgressDialog() {
        if(progressDialog!!.isShowing) { progressDialog!!.dismissWithAnimation() }
    }

}
