package com.learn.androidplayground.viewpagerrecycler

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.learn.androidplayground.R
import com.learn.androidplayground.util.getCompactDrawable

/**
 * @author Partho
 * @since 26 Nov,2023
 */
class ViewPagerItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val containerLayout: RelativeLayout = itemView.findViewById(R.id.containerLayout)
    private val img1: ImageView = itemView.findViewById(R.id.img1)
    private val nameTv: TextView = itemView.findViewById(R.id.nameTv)
    private val context = itemView.context

    fun bindData(
        position: Int,
        itemEntity: ItemEntity,
        listener: ViewPagerListAdapter.OnItemClickListener
    ) {
        containerLayout.background = context.getCompactDrawable(itemEntity.backgroundColor)
        img1.background = context.getCompactDrawable(itemEntity.drawableRes)
        nameTv.text = context.getString(itemEntity.stringRes)
        img1.setOnClickListener {
            listener.onItemClick(position)
        }
    }
}