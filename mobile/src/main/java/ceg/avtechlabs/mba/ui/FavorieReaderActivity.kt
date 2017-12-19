package ceg.avtechlabs.mba.ui

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.models.DronaDBHelper
import ceg.avtechlabs.mba.util.loadImage
import com.google.android.gms.ads.AdRequest
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_notification_reader.*
import rx.Observable
import rx.schedulers.Schedulers

class FavorieReaderActivity : AppCompatActivity() {

    var title = ""
    var content = ""
    var date = ""
    var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_reader)

        notify_collapsing_toolbar.title = getString(R.string.app_name)
        notify_adView.loadAd(AdRequest.Builder().build())

        title = intent.getStringExtra(NotificationReader.INTENT_READ_TITLE)
        date = intent.getStringExtra(NotificationReader.INTENT_PUB_DATA)
        content = intent.getStringExtra(NotificationReader.INTENT_READ_DESC)
        imageUrl = intent.getStringExtra(NotificationReader.INTENT_READ_URl)

        notify_textviewTitle.text = title
        notify_textviewDate.text  = date
        notify_textviewDescription.setHtml(content)


        notify_scroll.scrollTo(0,0)
        notify_image.loadImage(this, imageUrl, notify_collapsing_toolbar, window)

        star_button.isLiked  = true
        setLikeListener()
    }

    fun setLikeListener() {
        star_button.setOnLikeListener(object: OnLikeListener {
            override fun liked(p0: LikeButton?) {

            }

            override fun unLiked(p0: LikeButton?) {
                star_button.isEnabled = false
                removeFromFavorites(title, content, date, imageUrl)
                Toast.makeText(this@FavorieReaderActivity, getString(R.string.toast_removed_from_favorites)
                , Toast.LENGTH_LONG).show()
            }
        })

    }


    fun removeFromFavorites(title: String, content: String, date: String, imageUrl: String) {
        try {
            val fav = DronaDBHelper(this)

            Observable.just<Unit>(Unit)
                    .map { fav.removeFromFavorites(title, content, date, imageUrl) }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.newThread())
                    .subscribe { a -> {} }
        } catch (ex: Exception) {
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        startActivity(Intent(this, FavoritesActivity::class.java))
    }
}
