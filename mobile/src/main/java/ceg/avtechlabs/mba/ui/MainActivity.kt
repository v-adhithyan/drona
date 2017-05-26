package ceg.avtechlabs.mba.ui

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import ceg.avtechlabs.mba.R
import cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val res = "res:/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageview_marketing.setImageURI(Uri.parse("$res${R.drawable.marketing_menu}"))
        imageview_finance.setImageURI(Uri.parse("$res${R.drawable.finance_menu}"))
        imageview_economics.setImageURI(Uri.parse("$res${R.drawable.economics_menu}"))
        //imageview_leadership.setImageURI(Uri.parse("$res${R.drawable.leadership_menu}"))
        imageview_others.setImageURI(Uri.parse("$res${R.drawable.others_menu}"))
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
        startActivity(intent)
    }
}
