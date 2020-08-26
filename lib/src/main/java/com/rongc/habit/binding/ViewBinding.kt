package com.rongc.habit.binding

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.ClickUtils

/**
 * View点击事件，默认不传递参数。如果需要传递参数可定义方法
 * <code>
 *     fun click(params: Any): () -> Unit = {
 *          log(params)
 *     }
 * </code>
 */
@BindingAdapter("android:onClick", "debounce", requireAll = false)
fun View.onClick(call: () -> Unit, debounce: Boolean = false) {
    onClick({ _: View -> call() }, debounce)
}

/**
 * View点击时间，传递View本身
 * @param debounce 是否去抖动
 */
@BindingAdapter("android:onClick", "debounce", requireAll = false)
fun View.onClick(call: (View) -> Unit, debounce: Boolean = false) {
    if (debounce) {
        ClickUtils.applySingleDebouncing(this, call)
    } else {
        setOnClickListener {
            call(it)
        }
    }
}

@BindingAdapter("bgColor", "bgPressedColor", "bgDisableColor", requireAll = false)
fun View.bgState(bgColor: Int, bgPressedColor: Int = bgColor, bgDisableColor: Int = bgColor) {
    bgDrawableState(
        ColorDrawable(bgColor),
        ColorDrawable(bgPressedColor),
        ColorDrawable(bgDisableColor)
    )
}

@BindingAdapter("bgDrawable", "bgPressedDrawable", "bgDisableDrawable", requireAll = false)
fun View.bgDrawableState(
    drawable: Drawable,
    pressedDrawable: Drawable = drawable,
    disableDrawable: Drawable = drawable
) {
//    getRoundDrawable()
    StateListDrawable().run {
        addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
        addState(intArrayOf(-android.R.attr.state_enabled), disableDrawable)
        addState(StateSet.WILD_CARD, drawable)
        background = this
    }
}

/** 对TextView设置不同状态时其文字颜色。  */
fun createColorStateList(
    normal: Int,
    disable: Int = normal,
    pressed: Int = normal,
    focused: Int = normal,
    checked: Int = normal
): ColorStateList? {
    val colors = intArrayOf(pressed, focused, normal, focused, disable, normal, checked)
    val states = arrayOfNulls<IntArray>(7)
    states[0] = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
    states[1] = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused)
    states[2] = intArrayOf(android.R.attr.state_enabled)
    states[3] = intArrayOf(android.R.attr.state_focused)
    states[4] = intArrayOf(android.R.attr.state_window_focused)
    states[5] = intArrayOf(android.R.attr.state_checked)
    states[6] = intArrayOf()
    return ColorStateList(states, colors)
}

@BindingAdapter("disableColor", "pressedColor", "focusedColor", "checkedColor", requireAll = false)
fun TextView.colorState(
    disable: Int = currentTextColor,
    pressed: Int = currentTextColor,
    focused: Int = currentTextColor,
    checkedColor: Int = currentTextColor
) {
    setTextColor(createColorStateList(currentTextColor, disable, pressed, focused, checkedColor))
}