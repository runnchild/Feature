package com.rongc.feature.toolbar

import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.rongc.feature.toolbar.BarConfig.Companion.TRANSPARENT_BLACK
import com.rongc.feature.toolbar.BarConfig.Companion.TRANSPARENT_WHITE
import com.rongc.feature.toolbar.BarConfig.Companion.UNDEFINE


/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/8/7
 * @since 2.1.4
 */
class StatusBarConfig {

    /**
     * 状态栏是否透明
     * TRANSPARENT_WHITE 透明且icon白色
     * TRANSPARENT_BLACK 透明且icon黑色（明亮模式）
     */
    var statusBarState = 0
        set(value) {
            if (value >= TRANSPARENT_WHITE) {
                isStatusTransparent = true
                isLightMode = value == TRANSPARENT_BLACK
            }
            field = value
        }


    internal var isStatusTransparent = false
    internal var isLightMode = false
    internal var navLightMode = true

    /**
     * 状态栏颜色
     */
    @ColorInt
    var statusBarColor = 0
        set(value) {
            isLightMode = ColorUtils.calculateLuminance(value) > 0.5f
            field = value
        }

    /**
     * 底部导航栏颜色
     */
    @ColorInt
    var navColor = UNDEFINE
        set(value) {
            navLightMode = ColorUtils.calculateLuminance(value) > 0.5f
            field = value
        }
}