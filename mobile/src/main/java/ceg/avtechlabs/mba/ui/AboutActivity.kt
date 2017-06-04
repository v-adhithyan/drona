package ceg.avtechlabs.mba.ui

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ceg.avtechlabs.mba.BuildConfig

import ceg.avtechlabs.mba.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val aboutPage = AboutPage(this)
                .isRTL(false)
                .setDescription("Android app for every MBA student")
                .addItem(Element().setTitle(BuildConfig.VERSION_NAME))
                .addEmail("pollachi.developer@gmail.com")
                .addPlayStore(packageName)
                .create()

        setContentView(aboutPage)
    }
}
