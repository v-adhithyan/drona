package ceg.avtechlabs.mba.ui

import android.os.AsyncTask

/**
 * Created by Adhithyan V on 11-12-2017.
 */

object Temp {
    fun ah() {
        object : AsyncTask<String, String, String>() {
            /**
             * Before starting background do some work.
             */
            override fun onPreExecute() {}

            override fun doInBackground(vararg params: String): String? {
                // TODO fetch url data do bg process.
                return null
            }

            /**
             * Update list ui after process finished.
             */
            override fun onPostExecute(result: String) {
                // NO NEED to use activity.runOnUiThread(), code execute here under UI thread.

                // Updating parsed JSON data into ListView
                val data: List<*>? = null
                // updating listview
                //((ListActivity) activity).updateUI(data);
            }

        }
    }
}

