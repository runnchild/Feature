package com.rongc.feature.toolbar

import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.rongc.feature.utils.drawable

class BarConfig {
    companion object {
        const val UNDEFINE = -2

        const val TRANSPARENT_WHITE = 1
        const val TRANSPARENT_BLACK = 2
    }

    /**
     * toolbar标题
     */
    var title: CharSequence? = null

    /**
     * toolbar显示状态
     */
    var toolbarVisible = true

    /**
     * 标题显示状态
     */
    var titleVisible = true

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

    val menuItems = arrayListOf<TextView.() -> Unit>()

    internal var isStatusTransparent = false
    internal var isLightMode = false
    internal var toolBarLightMode = false
    internal var navLightMode = true

    /**
     * 状态栏颜色
     */
    var statusColor = UNDEFINE
        set(value) {
            isLightMode = ColorUtils.calculateLuminance(value) > 0.5f
            field = value
        }

    /**
     * toolbar背景色
     */
    var toolBarBackground = -1
        set(value) {
            toolBarLightMode = ColorUtils.calculateLuminance(value) > 0.5f
            field = value
        }

    /**
     * toolbar分割线颜色
     */
    var toolBarDividerColor = UNDEFINE

    /**
     * toolbar返回按钮显示状态
     */
    var toolbarBackVisible = true

    /**
     * toolbar返回按钮图标
     */
    var toolbarBackDrawable = R.mipmap.common_icon_back.drawable()!!

    /**
     * 底部导航栏颜色
     */
    var navColor = -1
        set(value) {
            navLightMode = ColorUtils.calculateLuminance(value) > 0.5f
            field = value
        }

    /**
     * 右上角菜单配置
     */
    fun menu(item: TextView.() -> Unit) {
        menuItems.add(item)
    }
}