package com.rongc.feature.toolbar

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.BarUtils
import com.rongc.feature.ability.ToolbarAbility
import com.rongc.feature.ui.host.IAbilityList
import com.rongc.feature.utils.idp

@BindingAdapter("addStatusBarHeight")
fun View?.addPaddingTopEqualStatusBar(add: Boolean) {
    if (add) {
        this?.updatePadding(top = BarUtils.getStatusBarHeight())
    }
}

@BindingAdapter("menus")
fun ViewGroup.setMenus(items: ArrayList<TextView.() -> Unit>?) {
    removeAllViews()
    items?.forEach {
        addItemMenu(it)
    }
}

private fun ViewGroup.addItemMenu(item: TextView.() -> Unit) {
    val menu = TextView(context).apply {
        textSize = 16f
        setTextColor(Color.parseColor("#353535"))
        gravity = Gravity.CENTER
        val padding = if (childCount > 0) {
            7.idp
        } else {
            15.idp
        }
        setPadding(7.idp, 0, padding, 0)
    }.apply(item)

    addView(
        menu, childCount - 1, FrameLayout.LayoutParams(-2, -1)
    )
}

fun IAbilityList.toolbar(config: ToolbarConfig.() -> Unit) {
    val toolbarAbility = findAbility { it is ToolbarAbility } as? ToolbarAbility
    if (toolbarAbility == null) {
        registerAbility(ToolbarAbility(this))
    }
    toolbarAbility?.toolBar?.toolbar(config)
}

fun IAbilityList.statusBar(config: StatusBarConfig.() -> Unit) {
    val toolbarAbility = findAbility { it is ToolbarAbility } as? ToolbarAbility
    if (toolbarAbility == null) {

    }
    toolbarAbility?.toolBar?.statusBar(config)
}