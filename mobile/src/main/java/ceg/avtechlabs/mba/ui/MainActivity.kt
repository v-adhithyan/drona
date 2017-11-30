package ceg.avtechlabs.mba.ui

import android.annotation.TargetApi
import android.app.ActivityOptions
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.NotificationCompat
import android.transition.Explode
import android.view.Menu
import android.view.MenuItem
import android.view.View

import ceg.avtechlabs.mba.R
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import android.view.WindowManager
import android.widget.Toast
import ceg.avtechlabs.mba.jobs.DronaJobPool
import ceg.avtechlabs.mba.util.*


class MainActivity : AppCompatActivity() {
    val res = "res:/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        @TargetApi(21)
        window.exitTransition = Explode()

        imageview_marketing.setImageURI(Uri.parse("$res${R.drawable.marketing_menu}"))
        imageview_finance.setImageURI(Uri.parse("$res${R.drawable.finance_menu}"))
        imageview_economics.setImageURI(Uri.parse("$res${R.drawable.economics_menu}"))
        //imageview_leadership.setImageURI(Uri.parse("$res${R.drawable.leadership_menu}"))
        imageview_others.setImageURI(Uri.parse("$res${R.drawable.others_menu}"))

        adview_main.loadAd(AdRequest.Builder().build())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }
        //startActivity(Intent(this, TempActivity::class.java))

        DronaJobPool.addJobsToSystem()
        if(!(getPreference(Globals.FIRST_RUN) as Boolean)) {
            showFeedPreferenceChooser()
            storePreference(Globals.FIRST_RUN, true)
            //Logger.out(getPreference(Globals.FEED_PREFERENCES) as String)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_main_about -> { showAbout() }
            else -> {}
        }

        return true
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    fun loadMarketingFeeds(v: View) {
        val topic = getString(R.string.topic_marketing)
        startReader(topic)
    }

    fun loadFinanceFeeds(v: View) {
        val topic = getString(R.string.topic_finance)
        startReader(topic)
    }

    fun loadEconomicsFeeds(v: View) {
        val topic = getString(R.string.topic_economics)
        startReader(topic)
    }

    fun loadLeadershipFeeds(v: View) {
        val topic = getString(R.string.topic_leadership)
        startReader(topic)
    }

    fun loadOtherFeeds(v: View) {
        val topic = getString(R.string.topic_others)
        startReader(topic)
    }
    
    private fun startReader(topic: String) {
        val intent = Intent(this, DisplayActivity::class.java)
        intent.putExtra(DisplayActivity.TOPIC, topic)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        } else { startActivity(intent) }
    }

    private fun showAbout() { startActivity(Intent(this, AboutActivity::class.java)) }


}
