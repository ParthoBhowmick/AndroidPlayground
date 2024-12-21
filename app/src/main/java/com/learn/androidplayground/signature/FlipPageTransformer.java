package com.learn.androidplayground.signature;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class FlipPageTransformer implements ViewPager2.PageTransformer {
    private static final float ROTATION_MAX = 180.0f;

    @Override
    public void transformPage(View page, float position) {
        float rotation = ROTATION_MAX * position;

        page.setAlpha(position < -1 || position > 1 ? 0 : 1);
        page.setPivotX(position < 0 ? page.getWidth() : 0f);
        page.setPivotY(page.getHeight() * 0.5f);
        page.setRotationY(rotation);
    }
}