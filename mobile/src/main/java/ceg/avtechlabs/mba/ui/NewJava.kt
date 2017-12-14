package ceg.avtechlabs.mba.ui

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.text.Html
import android.view.animation.AnimationUtils
import android.widget.Toast
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.util.Extractor
import cn.pedant.SweetAlert.SweetAlertDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_reader.*

/**
 * Created by Adhithyan V on 12-12-2017.
 */

/*class NewJava {
    private fun change() = if(i == ReaderActivity.ITEMS!!.size) {
        Toast.makeText(this, "No more items", Toast.LENGTH_LONG).show()
    } else {
        collapsing_toolbar.title = ReaderActivity.TITLE[i]
        //collapsing_toolbar.title = ITEMS[i].title
        textviewTitle.text = ReaderActivity.ITEMS[i].title
        textviewDescription.text = ReaderActivity.ITEMS[i].description
        textviewDate.text = ReaderActivity.ITEMS[i].pubDate
        //Toast/.makeText(this, "${ITEMS!!.size - i - 1} items left", Toast.LENGTH_SHORT).show()
        //i = i+1
        val progressDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        //val progressDialog = ProgressDialog(this)
        progressDialog.progressHelper!!.barColor = R.color.colorAccent
        progressDialog.titleText = getString(R.string.loading)
        progressDialog.setCancelable(false)
        progressDialog.show()
        // progressDialog.isIndeterminate = true
        //progressDialog.setProgressStyle(DoubleBounce())
        /*val progressBar = ProgressBar(this)
        val doubleBounce = DoubleBounce()
        progressBar.indeterminateDrawable = doubleBounce
        progressBar.visibility = ProgressBar.VISIBLE*/

        Thread{
            db!!.markFeedAsRead(ReaderActivity.ITEMS[i].title, ReaderActivity.ITEMS[i].description, category)

            if(i < ReaderActivity.ITEMS.size) {
                try {
                    currentArticle = Extractor(ReaderActivity.ITEMS[i].link).extract()
                    //nextArticle = Extractor(ITEMS[i+1].link).extract()
                } catch(ex: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@ReaderActivity, getString(R.string.toast_feeds_not_loaded), Toast.LENGTH_LONG).show()
                        finish()
                    }
                }

                //Looper.prepare()

            }



            var article = currentArticle
            //val minutes = ((article!!.document.text().split(" ").size).toFloat() / 275F).toFloat()
            runOnUiThread {

                //Toast.makeText(this, currentArticle!!.document.text().toString() + "curre", Toast.LENGTH_LONG).show()
                //collapsing_toolbar.title = article!!.title
                textviewTitle.text = (article!!.title).toUpperCase()
                textviewTitle.setAllCaps(true)
                if(article!!.document.text().length == 0) {
                    textviewDescription.text = ReaderActivity.ITEMS[i].description
                    Toast.makeText(this, getString(R.string.read_more), Toast.LENGTH_LONG).show()
                } else {
                    textviewDescription.text = Html.fromHtml(article.document.text())
                }
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
                Snackbar.make(coordinatorLayoutReader, " minutes to read this story.", Snackbar.LENGTH_LONG).show()
                Toast.makeText(this, "${ReaderActivity.ITEMS.size - i - 1} unread stories remaining ..", Toast.LENGTH_SHORT).show()
                i = i + 1
            }
            //prefetch next articlea



        }.start()
    }

}*/
