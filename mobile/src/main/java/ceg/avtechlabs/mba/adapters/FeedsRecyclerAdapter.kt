package ceg.avtechlabs.mba.adapters

import android.app.Activity
import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.models.AdapterObject
import ceg.avtechlabs.mba.ui.DisplayActivity
import ceg.avtechlabs.mba.ui.ReaderActivity
import ceg.avtechlabs.mba.util.getShareLinkIntent
import com.crazyhitty.chdev.ks.rssmanager.Channel
import com.crazyhitty.chdev.ks.rssmanager.RSS
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.card_row.*
import java.net.URI
import java.util.*


/**
 * Created by adhithyan-3592 on 20/04/17.
 */

class FeedsRecyclerViewAdapter(val activity: Activity, private val rssList: List<RSS>) : RecyclerView
.Adapter<FeedsRecyclerViewAdapter.DataObjectHolder>() {
    private var items = ArrayList<Channel.Item>()
    private var titles = emptyArray<String>()

    init {
        for (rss in rssList) {
            items.addAll(rss.channel.items)
        }
        ACTIVITY = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_row, parent, false)

        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        holder.title.setText(items.get(position).title)
        holder.title.tag = items.get(position).link

        var description = items.get(position).description
        description?.let { holder.description.setText(Html.fromHtml(it)) }
        //holder.feedIcon.setImageURI(Uri.parse("https://raw.githubusercontent.com/facebook/fresco/master/docs/static/logo.png"))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    companion object {
        var ACTIVITY: Activity? = null
    }

    class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var title: TextView
        internal var description: TextView
        //internal var feedIcon: SimpleDraweeView

        init {
            title = itemView.findViewById(R.id.card_title) as TextView
            description = itemView.findViewById(R.id.card_description) as TextView
            //feedIcon = itemView.findViewById(R.id.feed_icon) as SimpleDraweeView
            //Log.i(LOG_TAG, "Adding Listener")
            itemView.setOnClickListener {
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(itemView.context.resources.getColor(R.color.colorPrimary))

                //val pendingIntent =  PendingIntent.getActivity(itemView.context, 0, getShareLinkIntent(itemView.context, title.tag.toString()), 0)
                //builder.addMenuItem(itemView.context.getString(R.string.menu_share), pendingIntent)
                builder.addDefaultShareMenuItem()
                val intent = builder.build()
                intent.launchUrl(itemView.context, Uri.parse(title.tag.toString()))
            }
        }

    }
}