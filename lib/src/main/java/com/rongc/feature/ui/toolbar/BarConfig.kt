package com.rongc.feature.ui.toolbar

import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.rongc.feature.R
import com.rongc.feature.utils.Compat.color
import com.rongc.feature.utils.Compat.drawable

class BarConfig {

    val menuItems = arrayListOf<TextView.() -> Unit>()
//    val imageMenuItems = arrayListOf<ImageView.() -> Unit>()

    var isStatusTransparent = false

    internal var isLightMode = false

    var statusColor = -2
        set(value) {
            isLightMode = ColorUtils.calculateLuminance(value) > 0.5f
            field = value
        }

    var toolBarBackground = -1
        set(value) {
            toolBarLightMode = ColorUtils.calculateLuminance(value) > 0.5f
            field = value
        }

    var toolBarDividerColor = R.color.divider_color.color()

    internal var toolBarLightMode = false

    var toolbarBackVisible = true

    var toolbarBackDrawable = R.mipmap.common_icon_back.drawable()!!

    internal var navLightMode = false

    var navColor = -2
        set(value) {
            navLightMode = ColorUtils.calculateLuminance(value) > 0.5f
            field = value
        }

    fun menu(item: TextView.() -> Unit) {
        menuItems.add(item)
    }

//    fun imageMenu(item: ImageView.() -> Unit) {
//        imageMenuItems.add(item)
//    }
}