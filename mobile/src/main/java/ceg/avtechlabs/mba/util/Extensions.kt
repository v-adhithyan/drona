package ceg.avtechlabs.mba.util

import android.content.Context
import android.net.ConnectivityManager
import org.simpleframework.xml.Attribute

/**
 * Created by Adhithyan V on 23-05-2017.
 */

fun Context.internetAvailable(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo != null
}