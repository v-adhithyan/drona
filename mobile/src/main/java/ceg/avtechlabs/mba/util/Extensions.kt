package ceg.avtechlabs.mba.util

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import ceg.avtechlabs.mba.R
import org.simpleframework.xml.Attribute

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

