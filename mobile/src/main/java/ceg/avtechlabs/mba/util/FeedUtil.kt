package ceg.avtechlabs.mba.util

import android.content.Context
import ceg.avtechlabs.mba.models.DronaDBHelper

/**
 * Created by Adhithyan V on 04-12-2017.
 */

class FeedUtil(internal var context: Context) {
    companion object {

    }

    fun isNewFeed(title: String, topic: String): Boolean {
        val db = DronaDBHelper(context)
        val insert = db.insert(title, topic)
        if(!insert) {
            return false
        }
        return true
    }
}