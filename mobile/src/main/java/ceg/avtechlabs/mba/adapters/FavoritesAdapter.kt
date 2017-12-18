package ceg.avtechlabs.mba.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.listeners.FavItemClickListener
import ceg.avtechlabs.mba.models.FavObject
import ceg.avtechlabs.mba.util.loadImage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.favorite_item.view.*

/**
 * Created by Adhithyan V on 18-12-2017.
 */

class FavoritesAdapter(internal val context: Context, internal  val favoritesList: List<FavObject>): RecyclerView.Adapter<FavoritesAdapter.FavViewHolder>() {

    private var clickListener: FavItemClickListener? = null

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val fav = favoritesList[position]

        //holder.thumbnail!!.loadImage(fav.imageUrl)
        holder.thumbnail!!.loadImage(fav.imageUrl)
        holder.title!!.text = fav.title
        holder.thumbnail!!.tag = fav.content
        holder.title!!.tag = fav.date
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.favorite_item, parent, false)

        return FavViewHolder(view)
    }

    fun setClickListener(clickListener: FavItemClickListener) {
        this.clickListener = clickListener
    }

    inner class FavViewHolder(internal val v: View): RecyclerView.ViewHolder(v), View.OnClickListener{

        var thumbnail: ImageView? = null
        var title: TextView? = null

        init {
            thumbnail = v.findViewById(R.id.fav_thumbnail) as ImageView
            title = v.findViewById(R.id.fav_description) as TextView
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if(clickListener != null)
                clickListener!!.onClick(v, adapterPosition)
        }

    }
}