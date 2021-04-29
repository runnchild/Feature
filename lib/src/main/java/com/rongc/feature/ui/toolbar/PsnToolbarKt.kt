package com.rongc.feature.ui.toolbar

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.BarUtils
import com.rongc.feature.R
import com.rongc.feature.utils.Compat.color
import com.rongc.feature.utils.idp

@BindingAdapter("addStatusBarHeight")
fun View?.addPaddingTopEqualStatusBar(add: Boolean) {
    if (add) {
        this?.updatePadding(top = BarUtils.getStatusBarHeight())
    }
}

@BindingAdapter("psn_background")
fun View.backgroundAndLightMode(it: Drawable?) {
    val value = (it as? ColorDrawable)?.color ?: 0
    val isLightMode = ColorUtils.calculateLuminance(value) > 0.5f
    (parent as PsnToolbar).setLightMode(isLightMode)
    background = it
}

@BindingAdapter("menus")
fun ViewGroup.setMenus(items: ArrayList<TextView.() -> Unit>) {
    removeAllViews()
    items.forEach {
        addItemMenu(it)
    }
}

private fun ViewGroup.addItemMenu(item: TextView.() -> Unit) {
    val menu = TextView(context).apply {
        textSize = 16f
        setTextColor(R.color.gray_353535.color())
        gravity = Gravity.CENTER
        val padding = if (childCount > 0) {
            7.idp
        } else {
            15.idp
        }
        setPadding(7.idp, 0, padding, 0)
    }.apply(item)

    addView(
        menu, childCount - 1,
        FrameLayout.LayoutParams(-2, -1)
    )
}

@BindingAdapter("visible", "transition")
fun View.visibleWithTransition(visible: Boolean, transition: Boolean) {
//    if (isVisible == visible) {
//        return
//    }
//    if (!isVisible && visible) {
//        postDelayed(100) {
//            TransitionManager.beginDelayedTransition(parent as ViewGroup, AutoTransition().apply {
//                duration = 50
//            })
//            isVisible = visible
//        }
//    } else {
    isVisible = visible
//    }
//    TransitionManager.beginDelayedTransition(parent as ViewGroup, AutoTransition())
//    isVisible = visible
}