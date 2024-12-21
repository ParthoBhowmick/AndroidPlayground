package com.learn.androidplayground.signature

import android.app.Activity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.learn.androidplayground.R

class SignatureActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val cardList = listOf("Card 1", "Card 2", "Card 3")
        val adapter = CardAdapter(cardList)
        viewPager.adapter = adapter
       // viewPager.setPageTransformer(FlipPageTransformer())
    }
}