package ceg.avtechlabs.mba.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.adapters.FavoritesAdapter
import ceg.avtechlabs.mba.listeners.FavItemClickListener
import ceg.avtechlabs.mba.models.DronaDBHelper
import ceg.avtechlabs.mba.models.FavObject
import ceg.avtechlabs.mba.util.Globals
import cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.favorite_content.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

class FavoritesActivity : AppCompatActivity(), FavItemClickListener {
    var progressDialog: SweetAlertDialog? = null
    var favorites = ArrayList<FavObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showProgressDialog()
        favorites = DronaDBHelper(this).getFavorites()

        if(favorites.size <= 0) {
            setContentView(R.layout.activity_favorites)
        } else {
            setContentView(R.layout.favorite_content)
            recycler_favorites.layoutManager = LinearLayoutManager(this)
            recycler_favorites.itemAnimator = DefaultItemAnimator()
            recycler_favorites.setHasFixedSize(true)
            val adapter = FavoritesAdapter(this, favorites)
            adapter.setClickListener(this)
            recycler_favorites.adapter = adapter
        }

        hideProgressDialog()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

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

    override fun onClick(view: View, position: Int) {
        val fav = favorites[position]
        val intent = Intent(this, NotificationReader::class.java)
        intent.putExtra(NotificationReader.INTENT_READ_TITLE, fav.title)
        intent.putExtra(NotificationReader.INTENT_READ_DESC, fav.content)
        intent.putExtra(NotificationReader.INTENT_PUB_DATA, fav.date)
        intent.putExtra(NotificationReader.INTENT_READ_URl, fav.imageUrl)
        intent.putExtra(NotificationReader.INTENT_SOURCE, Globals.SOURCE_FAV)
        startActivity(intent)
    }
}
