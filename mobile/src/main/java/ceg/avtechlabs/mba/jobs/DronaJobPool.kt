package ceg.avtechlabs.mba.jobs

/**
 * Created by Adhithyan V on 30-11-2017.
 */
class DronaJobPool {

    companion object {
        fun addJobsToSystem() {
            NewFeedsCheckerJob.scheduleJob()
        }
    }
}