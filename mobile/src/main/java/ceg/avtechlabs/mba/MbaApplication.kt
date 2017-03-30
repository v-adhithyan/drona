package ceg.avtechlabs.mba

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class MbaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val font = CalligraphyConfig.Builder()
                . setDefaultFontPath("fonts/proximanovaregular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        CalligraphyConfig.initDefault(font)
    }
}
