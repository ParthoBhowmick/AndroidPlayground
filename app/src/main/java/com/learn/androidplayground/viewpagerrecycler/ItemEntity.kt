package com.learn.androidplayground.viewpagerrecycler

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * @author Partho
 * @since 26 Nov,2023
 */
data class ItemEntity(
    @StringRes val stringRes: Int,
    @DrawableRes val drawableRes: Int,
    @ColorRes val backgroundColor: Int
)
