package com.learn.androidplayground.viewpagerrecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learn.androidplayground.R


/**
 * @author Partho
 * @since 26 Nov,2023
 */
class ViewPagerListAdapter(
    private val items: ArrayList<ItemEntity>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ViewPagerItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerItemViewHolder {
        return ViewPagerItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_temple_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewPagerItemViewHolder, position: Int) {
        holder.bindData(position ,items[position], listener)
    }

    override fun getItemCount() = items.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}