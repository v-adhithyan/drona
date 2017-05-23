package ceg.avtechlabs.mba

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class MbaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val font = CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/proximanova.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        CalligraphyConfig.initDefault(font)

        Fresco.initialize(this)
    }
}
