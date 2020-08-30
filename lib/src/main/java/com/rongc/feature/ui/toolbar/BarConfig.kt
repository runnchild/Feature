package com.rongc.feature.ui.toolbar

import androidx.core.graphics.ColorUtils
import com.rongc.feature.R
import com.rongc.feature.utils.Compat.color
import com.rongc.feature.utils.Compat.drawable

class BarConfig {

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

}