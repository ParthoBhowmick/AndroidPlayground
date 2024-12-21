package com.learn.androidplayground.viewpagerrecycler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.learn.androidplayground.R
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class ViewPagerWithRecyclerViewActivity : AppCompatActivity() {

    private lateinit var testPager: ViewPager2
    private lateinit var dotIndicator: DotsIndicator
    private lateinit var viewPagerAdapter: ViewPagerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager_with_recycler_view)
        testPager = findViewById(R.id.testPager)
        dotIndicator = findViewById(R.id.dotsIndicator)
        val itemList: ArrayList<ItemEntity> = ArrayList<ItemEntity>().apply {
            add(ItemEntity(R.string.txt_church, R.drawable.ic_chruch, R.color.color_sky_blue))
            add(ItemEntity(R.string.txt_mosque, R.drawable.ic_mosque, R.color.color_yellow))
            add(ItemEntity(R.string.txt_temple, R.drawable.ic_temple, R.color.color_light_violet))
        }
        viewPagerAdapter = ViewPagerListAdapter(itemList, object : ViewPagerListAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                itemList.removeAt(position)
                viewPagerAdapter.notifyItemRemoved(position)
            }
        })
        testPager.setOffscreenPageLimit(3)
        testPager.addItemDecoration(MarginItemDecoration(20))
        testPager.adapter = viewPagerAdapter
        dotIndicator.attachTo(testPager)
    }
}