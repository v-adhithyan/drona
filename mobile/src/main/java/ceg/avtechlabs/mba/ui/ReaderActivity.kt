package ceg.avtechlabs.mba.ui

import android.annotation.TargetApi
import android.app.ProgressDialog
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
import android.view.*
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.widget.ProgressBar
import ceg.avtechlabs.mba.R
import android.widget.Toast
import ceg.avtechlabs.mba.listeners.SwypeListener
import ceg.avtechlabs.mba.util.Extractor
import cn.pedant.SweetAlert.SweetAlertDialog
import com.chimbori.crux.articles.Article
import com.crazyhitty.chdev.ks.rssmanager.Channel
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_reader.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*


class ReaderActivity : AppCompatActivity() {

    var webview: WebView? = null
    var url = ""
    var i = 0
    var array = arrayOf("one", "two", "three")

    var currentArticle: Article? = null
    var nextArticle: Article? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        @TargetApi(21)
        window.exitTransition = Explode()



        val context = this as Context

        /*scroll
                .setOnTouchListener(object:SwypeListener(context) {
            override fun onSwipeRight() { change() }
            override fun onSwipeLeft() { change() }
            override fun onSwipeTop() { }
            override fun onSwipeBottom() { }
        });*/
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
        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.text_share, ITEMS[i].link))
        startActivity(Intent.createChooser(intent, resources.getString(R.string.intent_share)))
    }

    private fun change() = if(i == ITEMS!!.size) {
        Toast.makeText(this, "No more items", Toast.LENGTH_LONG).show()
    } else {
        collapsing_toolbar.title = "Drona"
        //collapsing_toolbar.title = ITEMS[i].title
        textviewTitle.text = ITEMS[i].title
        textviewDescription.text = ITEMS[i].description
        textviewDate.text = ITEMS[i].pubDate
        //Toast/.makeText(this, "${ITEMS!!.size - i - 1} items left", Toast.LENGTH_SHORT).show()
        //i = i+1

        val progressDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        //val progressDialog = ProgressDialog(this)
        progressDialog.progressHelper!!.barColor = R.color.colorAccent
        progressDialog.titleText = "Loading .."
        progressDialog.setCancelable(false)
        progressDialog.show()
       // progressDialog.isIndeterminate = true
        //progressDialog.setProgressStyle(DoubleBounce())
        /*val progressBar = ProgressBar(this)
        val doubleBounce = DoubleBounce()
        progressBar.indeterminateDrawable = doubleBounce
        progressBar.visibility = ProgressBar.VISIBLE*/

        Thread{

            if(i == 0) {
                currentArticle = Extractor(ITEMS[i].link).extract()
                nextArticle = Extractor(ITEMS[i+1].link).extract()
            } else if(i < ITEMS.size-1){
                //nextArticle = Extractor(ITEMS[i+1].link).extract()
            }


            var article = currentArticle
            val minutes = ((article!!.document.text().split(" ").size).toFloat() / 275F).toFloat()
            runOnUiThread {

                //collapsing_toolbar.title = article!!.title
                textviewTitle.text = (article!!.title).toUpperCase()
                textviewTitle.setAllCaps(true)
                textviewDescription.text = Html.fromHtml(article.document.text())
                Picasso.with(this).load(article.imageUrl).into(object: com.squareup.picasso.Target {
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


                                //setBackgroundTintList(ColorStateList.valueOf(vibrantColor));

                            }
                        });
                    }
                })


                //progressBar.visibility = ProgressBar.INVISIBLE
                progressDialog.dismiss()
                AnimationUtils.loadAnimation(this, R.anim.slide_right)
                Snackbar.make(coordinatorLayoutReader, "$minutes minutes to read this story.", Snackbar.LENGTH_LONG).show()
                Toast.makeText(this, "${ITEMS.size - i - 1} unread stories remaining ..", Toast.LENGTH_SHORT).show()
                i = i + 1
            }
            //prefetch next articlea
            currentArticle = Extractor(ITEMS[i+1].link).extract()

        }.start()
    }

    fun open(v: View) {
        Toast.makeText(this, "open", Toast.LENGTH_LONG).show()
    }

    fun nextArticle(v: View) {
        change()
    }

    fun share(v: View) {
        Toast.makeText(this, "share", Toast.LENGTH_LONG).show()
    }

    companion object {
        //var rss: List<RSS>? = null
         var ITEMS = ArrayList<Channel.Item>()
    }
}
