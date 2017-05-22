package ceg.avtechlabs.mba.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ceg.avtechlabs.mba.R
import ceg.avtechlabs.mba.models.AdapterObject
import kotlinx.android.synthetic.main.card_row.*
import java.util.*


/**
 * Created by adhithyan-3592 on 20/04/17.
 */

class RecyclerViewAdapter(private val mDataset: ArrayList<AdapterObject>) : RecyclerView
.Adapter<RecyclerViewAdapter.DataObjectHolder>() {

    class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var title: TextView
        internal var description: TextView

        init {
            title = itemView.findViewById(R.id.card_title) as TextView
            description = itemView.findViewById(R.id.card_description) as TextView
            //Log.i(LOG_TAG, "Adding Listener")
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            myClickListener!!.onItemClick(getAdapterPosition(), v)
        }
    }

    fun setOnItemClickListener(myClickListener: MyClickListener) {
        //myClickListener = myClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_row, parent, false)

        val dataObjectHolder = DataObjectHolder(view)
        return dataObjectHolder
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        holder.title.setText("")
        holder.description.setText("")
    }

    fun addItem(dataObj: AdapterObject, index: Int) {
        mDataset.add(index, dataObj)
        notifyItemInserted(index)
    }

    fun deleteItem(index: Int) {
        //mDataset.remove(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int {
        return 5
    }

    interface MyClickListener {
        fun onItemClick(position: Int, v: View)
    }

    companion object {
        val LOG_TAG = "RecyclerViewAdapter"
        var myClickListener: MyClickListener? = null
    }
}