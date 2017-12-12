package ceg.avtechlabs.mba.adapters

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.ui.DisplayActivity
import ceg.avtechlabs.mba.ui.MainActivity
import ceg.avtechlabs.mba.util.internetAvailable
import ceg.avtechlabs.mba.util.showNoInternetDialog
import com.squareup.picasso.Picasso
import java.util.*

/**
 * Created by Adhithyan V on 12-12-2017.
 */
class MenuAdapter(internal val activity: Activity, internal val adapter: ArrayList<HashMap<String, String>>): BaseAdapter() {
    var inflater: LayoutInflater? = null

    init {
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflater!!.inflate(R.layout.menu_item, null)
        val item = adapter[position]

        val thumbnail = view.findViewById(R.id.menu_image) as ImageView
        val menu = view.findViewById(R.id.menu_title) as TextView

        val details = item.get(MainActivity.TITLE)!!.split(",")
        val topic = details[1]
        menu.text = details[0]
        val imageId = item.get(MainActivity.IMAGE)!!.toInt()
        Picasso.with(activity).load(imageId).into(thumbnail)

        view.setOnClickListener {
            if(topic.equals("Marketing")) {
                loadMarketingFeeds()
            } else if (topic.equals("Finance")) {
                loadFinanceFeeds()
                
            } else if (topic.equals("Economics")) {
                loadEconomicsFeeds()

            } else {
                loadOthersFeeds()
            }
            
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return adapter.size
    }

    fun loadMarketingFeeds() {
        val topic = "Marketing"
        startReader(topic)
    }

    fun loadFinanceFeeds() {
        val topic ="Finance"
        startReader(topic)
    }

    fun loadEconomicsFeeds() {
        val topic = "Economics"
        startReader(topic)
    }

    fun loadLeadershipFeeds() {
        val topic = "Leadership"
        startReader(topic)
    }

    fun loadOthersFeeds() {
        val topic = "Others"
        startReader(topic)
    }

    private fun startReader(topic: String) {
        if(activity.internetAvailable()) {
            val intent = Intent(activity, DisplayActivity::class.java)
            intent.putExtra(DisplayActivity.TOPIC, topic)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
            } else { activity.startActivity(intent) }
        } else {
            activity.showNoInternetDialog()
        }

    }

}