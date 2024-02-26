package com.learn.androidplayground.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * @author Partho
 * @since 26 Nov,2023
 */

internal fun Context.getCompactDrawable(@DrawableRes drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableId)
}

internal fun Context.getCompactColor(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}