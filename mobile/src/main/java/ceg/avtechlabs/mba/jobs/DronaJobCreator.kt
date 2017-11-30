package ceg.avtechlabs.mba.jobs

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

/**
 * Created by Adhithyan V on 30-11-2017.
 */
class DronaJobCreator: JobCreator {
    override fun create(tag: String): Job? {

        when (tag) {
            NewFeedsCheckerJob.TAG ->  return NewFeedsCheckerJob()
            else ->  return null
        }
    }
}