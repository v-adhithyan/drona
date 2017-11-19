package ceg.avtechlabs.mba.ui

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat.setBackgroundTintList
import android.support.v7.graphics.Palette
import android.text.Html
import android.transition.Explode
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import ceg.avtechlabs.mba.R
import android.widget.Toast
import ceg.avtechlabs.mba.listeners.SwypeListener
import ceg.avtechlabs.mba.util.Extractor
import cn.pedant.SweetAlert.SweetAlertDialog
import com.chimbori.crux.articles.Article
import com.crazyhitty.chdev.ks.rssmanager.Channel
import com.crazyhitty.chdev.ks.rssmanager.RSS
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_reader.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.Reader
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


        val context = this

        scroll
                .setOnTouchListener(object:SwypeListener(context) {
            override fun onSwipeRight() { change() }
            override fun onSwipeLeft() { change() }
            override fun onSwipeTop() { }
            override fun onSwipeBottom() { }
        });
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
        collapsing_toolbar.title = "Drona"

        textviewDescription.setLineSpacing(1.2F, 1.0F)
        textviewTitle.setLineSpacing(1.2F, 1.0F)

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
        textviewTitle.text = ITEMS[i].title
        textviewDescription.text = ITEMS[i].description
        //Toast.makeText(this, "${ITEMS!!.size - i - 1} items left", Toast.LENGTH_SHORT).show()
        //i = i+1

        val progressDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog.progressHelper!!.barColor = R.color.colorAccent
        progressDialog.titleText = "Loading .."
        progressDialog.setCancelable(false)
        //progressDialog.setIndeterminateDrawable(DoubleBounce())
        progressDialog.show()
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

                textviewTitle.text = article!!.title
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
                                //val vibrantColor = palette.getVibrantColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

                                collapsing_toolbar.setContentScrimColor(mutedColor);
                                collapsing_toolbar.setStatusBarScrimColor(mutedDarkColor);
                                //setBackgroundTintList(ColorStateList.valueOf(vibrantColor));

                            }
                        });
                    }
                })


                progressDialog.dismiss()
                Snackbar.make(coordinatorLayoutReader, "$minutes minutes to read this story.", Snackbar.LENGTH_LONG).show()
                Toast.makeText(this, "${ITEMS!!.size - i - 1} unread stories remaining ..", Toast.LENGTH_SHORT).show()
                i = i + 1
            }
            //prefetch next articlea
            currentArticle = Extractor(ITEMS[i+1].link).extract()

        }.start()
    }

    companion object {
        //var rss: List<RSS>? = null
         var ITEMS = ArrayList<Channel.Item>()
    }
}
