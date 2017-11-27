package ceg.avtechlabs.mba.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ceg.avtechlabs.mba.R

class TempActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //collapsing_toolbar.title = "Drona"

    }
}
