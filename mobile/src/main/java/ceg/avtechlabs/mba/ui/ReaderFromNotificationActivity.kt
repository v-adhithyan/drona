package ceg.avtechlabs.mba.ui

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
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
import ceg.avtechlabs.mba.util.Extractor
import ceg.avtechlabs.mba.util.loadInterstitialAd
import cn.pedant.SweetAlert.SweetAlertDialog
import com.chimbori.crux.articles.Article
import com.crazyhitty.chdev.ks.rssmanager.Channel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_reader.*
import kotlinx.android.synthetic.main.activity_reader_from_notification.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

class ReaderFromNotificationActivity : AppCompatActivity() {


    var webview: WebView? = null
    var url = ""
    var i = 0
    var array = arrayOf("one", "two", "three")
    var adLoadCount = 0
    var currentArticle: Article? = null
    var nextArticle: Article? = null
    var englishLocale = true //assume default is english
    var title = ""
    var link = ""
    var desc = ""
    var date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader_from_notification)
        @TargetApi(21)
        window.exitTransition = Explode()

        //

        title = intent.getStringExtra(INTENT_READ_TITLE)
        desc = intent.getStringExtra(INTENT_READ_DESC)
        date = intent.getStringExtra(INTENT_PUB_DATA)
        link = intent.getStringExtra(INTENT_READ_URl)
        notify_adView.loadAd(AdRequest.Builder().build())
        change()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_reader, menu)
        return true
    }

    /*override fun onNewIntent(intent: Intent?) {
        Toast.makeText(this, intent!!.extras.toString(), Toast.LENGTH_LONG).show()
        this.intent = intent
    }*/

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
        //intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.text_share, ITEMS[j].link))
        //startActivity(Intent.createChooser(intent, resources.getString(R.string.intent_share)))
    }

    private fun change() {
        notify_collapsing_toolbar.title = getString(R.string.app_name)
        notify_textviewTitle.text = title
        notify_textviewDescription.text = desc
        notify_textviewDate.text = date

        val progressDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog.progressHelper!!.barColor = R.color.colorAccent
        progressDialog.titleText = getString(R.string.loading)
        progressDialog.setCancelable(false)
        progressDialog.show()

        Thread{

            val article = Extractor(link).extract()
            //val minutes = ((article!!.document.text().split(" ").size).toFloat() / 275F).toFloat()
            runOnUiThread {

                //Toast.makeText(this, currentArticle!!.document.text().toString() + "curre", Toast.LENGTH_LONG).show()
                //collapsing_toolbar.title = article!!.title
                notify_textviewTitle.text = (article!!.title).toUpperCase()
                notify_textviewTitle.setAllCaps(true)
                if(article!!.document.text().length == 0) {
                    notify_textviewDescription.text = desc
                    Toast.makeText(this, getString(R.string.read_more), Toast.LENGTH_LONG).show()
                } else {
                    notify_textviewDescription.text = Html.fromHtml(article!!.document.text())
                }
                Picasso.with(this).load(article!!.imageUrl).into(object: com.squareup.picasso.Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                    }

                    override fun onBitmapFailed(errorDrawable: Drawable?) {

                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        notify_image.setImageBitmap(bitmap)
                        Palette.from(bitmap).generate(object: Palette.PaletteAsyncListener {
                            override fun onGenerated(palette: Palette) {
                                val mutedColor = palette.getMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                                val mutedDarkColor = palette.getDarkMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

                                notify_collapsing_toolbar.setContentScrimColor(mutedColor);
                                notify_collapsing_toolbar.setStatusBarScrimColor(mutedDarkColor);
                                @TargetApi(21)
                                window.statusBarColor = palette.getDarkVibrantColor(resources.getColor(R.color.colorPrimaryDark))


                                //setBackgroundTintList(ColorStateList.valueOf(vibrantColor));

                            }
                        });
                    }
                })


                //progressBar.visibility = ProgressBar.INVISIBLE
                progressDialog.dismiss()
                AnimationUtils.loadAnimation(this, R.anim.slide_right)
                //Snackbar.make(coordinatorLayoutReader, " minutes to read this story.", Snackbar.LENGTH_LONG).show()
                //Toast.makeText(this, "${ITEMS.size - i - 1} unread stories remaining ..", Toast.LENGTH_SHORT).show()
                //i = i + 1
            }
            //prefetch next articlea
            //currentArticle = Extractor(ITEMS[i + 1].link).extract()


        }.start()
    }


    fun share(v: View) {
        //Toast.makeText(this, "share", Toast.LENGTH_LONG).show()
        shareLink()
    }


    override fun onBackPressed() {
        loadInterstitialAd()
        finish()
    }

    companion object {
        val INTENT_READ_TITLE = "title"
        val INTENT_READ_DESC = "description"
        val INTENT_READ_URl = "url"
        val INTENT_PUB_DATA = "date"
    }
}
