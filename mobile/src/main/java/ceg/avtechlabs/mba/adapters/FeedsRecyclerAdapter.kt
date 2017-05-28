package ceg.avtechlabs.mba.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.crazyhitty.chdev.ks.rssmanager.Channel
import com.crazyhitty.chdev.ks.rssmanager.RSS
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.card_row.*
import java.net.URI
import java.util.*


/**
 * Created by adhithyan-3592 on 20/04/17.
 */

class FeedsRecyclerViewAdapter(private val context: Context, private val rssList: List<RSS>) : RecyclerView
.Adapter<FeedsRecyclerViewAdapter.DataObjectHolder>() {
    private var items = ArrayList<Channel.Item>()
    private var images = ArrayList<String>()

    init {
        for (rss in rssList) {
            items.addAll(rss.channel.items)
        }
    }

    /*fun setOnItemClickListener(myClickListener: MyClickListener) {
        //myClickListener = myClickListener
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_row, parent, false)

        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        holder.title.setText(items.get(position).title)
        var description = items.get(position).description
        //description?.let { holder.description.setText(Html.fromHtml(description)) }
        holder.feedIcon.setImageURI(Uri.parse("https://raw.githubusercontent.com/facebook/fresco/master/docs/static/logo.png"))
        holder.title.setOnClickListener {
            val intent = Intent(context, ReaderActivity::class.java)
            intent.putExtra(DisplayActivity.URL, items.get(position).link)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface MyClickListener {
        fun onItemClick(position: Int, v: View)
    }

    class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var title: TextView
        //internal var description: TextView
        internal var feedIcon: SimpleDraweeView

        init {
            title = itemView.findViewById(R.id.card_title) as TextView
            //description = itemView.findViewById(R.id.card_description) as TextView
            feedIcon = itemView.findViewById(R.id.feed_icon) as SimpleDraweeView
            //Log.i(LOG_TAG, "Adding Listener")
            //itemView.setOnClickListener(this)
        }

    }

    companion object {
        val LOG_TAG = "RecyclerViewAdapter"
        var myClickListener: MyClickListener? = null
    }
}