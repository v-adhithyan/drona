package ceg.avtechlabs.mba.util

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.text.TextUtils
import android.widget.Toast
import ceg.avtechlabs.mba.R
import com.yarolegovich.lovelydialog.LovelyChoiceDialog

/**
 * Created by Adhithyan V on 23-05-2017.
 */

fun Context.internetAvailable(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo != null
}

fun getShareLinkIntent(context: Context, url: String): Intent? {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, context.resources.getString(R.string.text_share, url))
    return Intent.createChooser(intent,context.resources.getString(R.string.intent_share))
}

fun Context.showFeedPreferenceChooser() {
    val items = resources.getStringArray(ceg.avtechlabs.mba.R.array.Categories)
    LovelyChoiceDialog(this)
            .setTopColorRes(R.color.colorPrimary)

            .setTitle(R.string.input_choose_feed_preferences_title)
            .setMessage(R.string.input_choose_feed_preferences_message)
            .setItemsMultiChoice(items) { positions, items ->
                val choice = TextUtils.join(",", items)
                storePreference(Globals.FEED_PREFERENCES, choice)
            }
            .setConfirmButtonText("Confirm")
            .setCancelable(false)
            .show()
}

fun Context.storePreference(key: String, message: Any) {
    val preference = getSharedPreferences(Globals.DRONA_PREFERENCES, 0)
    val editor = preference.edit()

    if(message is Boolean) {
        editor.putBoolean(key, message)
    }

    if(message is String) {
        editor.putString(key, message)
    }

    editor.commit()
}

fun Context.getPreference(key: String): Any {
    val preference = getSharedPreferences(Globals.DRONA_PREFERENCES, 0)

    if(key == Globals.FIRST_RUN) {
        return preference.getBoolean(key, false)
    }

    return preference.getString(key, "")
}