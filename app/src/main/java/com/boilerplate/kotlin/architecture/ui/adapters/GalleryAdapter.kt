package com.boilerplate.kotlin.architecture.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.boilerplate.kotlin.architecture.R
import kotlinx.android.synthetic.main.gallery_item.view.*

/**
 * Created by a.belichenko@gmail.com on 10.10.17.
 */
class GalleryAdapter(val items: List<String>, val listener: (String) -> Unit) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], listener)

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String, listener: (String) -> Unit) = with(itemView) {
            itemHeader.text = item
            itemPic.setActualImageResource(R.drawable.item_pic)
            setOnClickListener { listener(item) }
        }
    }

}