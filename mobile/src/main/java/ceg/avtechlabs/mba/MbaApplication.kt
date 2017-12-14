package ceg.avtechlabs.mba

import android.app.Application
import ceg.avtechlabs.mba.jobs.DronaJobCreator
import com.evernote.android.job.JobManager
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class MbaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val font = CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/proximanova.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        CalligraphyConfig.initDefault(font)

        //Fresco.initialize(this)

        JobManager.create(this).addJobCreator(DronaJobCreator())
    }

}
