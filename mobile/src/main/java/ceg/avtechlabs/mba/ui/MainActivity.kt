package ceg.avtechlabs.mba.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import ceg.avtechlabs.mba.R
import cn.pedant.SweetAlert.SweetAlertDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        val intent = Intent(this, ReaderActivity::class.java)
        intent.putExtra(ReaderActivity.TOPIC, topic)
        startActivity(intent)
    }
}
