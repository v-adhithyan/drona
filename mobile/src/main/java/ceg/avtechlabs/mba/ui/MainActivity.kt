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
        val alert = SweetAlertDialog(this)
        alert.titleText = "Marketing"
        alert.show()
    }

    fun loadFinanceFeeds(v: View) {
        /*val alert = SweetAlertDialog(this)
        alert.titleText = "Finance"
        alert.show()*/
        val intent = Intent(this, DisplayActivity::class.java)
        startActivity(intent)
    }

    fun loadEconomicsFeeds(v: View) {
        val alert = SweetAlertDialog(this)
        alert.titleText = "Economics"
        alert.show()
    }

    fun loadLeadershipFeeds(v: View) {
        val alert = SweetAlertDialog(this)
        alert.titleText = "Leadership"
        alert.show()
    }

    fun loadOtherFeeds(v: View) {
        val alert = SweetAlertDialog(this)
        alert.titleText = "Other"
        alert.show()
    }
}
