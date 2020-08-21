package com.rongc.habit.binding

import android.R
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import android.view.View
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.ClickUtils
import com.rongc.habit.utils.Compat.getRoundDrawable

/**
 * View点击事件，默认不传递参数。如果需要传递参数可定义方法
 * <code>
 *     fun click(params: Any): () -> Unit = {
 *          log(params)
 *     }
 * </code>
 */
@BindingAdapter("onClick", "debounce", requireAll = false)
fun View.onClick(call: () -> Unit, debounce: Boolean = false) {
    onClick({ _: View -> call() }, debounce)
}

/**
 * View点击时间，传递View本身
 * @param debounce 是否去抖动
 */
@BindingAdapter("onClick", "debounce", requireAll = false)
fun View.onClick(call: (View) -> Unit, debounce: Boolean = false) {
    if (debounce) {
        ClickUtils.applySingleDebouncing(this, call)
    } else {
        setOnClickListener {
            call(it)
        }
    }
    ClickUtils.applyPressedBgDark(this)
}

@BindingAdapter("bgColor", "bgPressedColor", "bgDisableColor", requireAll = false)
fun View.bgState(bgColor: Int, bgPressedColor: Int = bgColor, bgDisableColor: Int = bgColor) {
    bgDrawableState(ColorDrawable(bgColor), ColorDrawable(bgPressedColor), ColorDrawable(bgDisableColor))
}

@BindingAdapter("bgDrawable", "bgPressedDrawable", "bgDisableDrawable", requireAll = false)
fun View.bgDrawableState(
    drawable: Drawable,
    pressedDrawable: Drawable = drawable,
    disableDrawable: Drawable = drawable
) {
//    getRoundDrawable()
    StateListDrawable().run {
        addState(intArrayOf(R.attr.state_pressed), pressedDrawable)
        addState(intArrayOf(-R.attr.state_enabled), disableDrawable)
        addState(StateSet.WILD_CARD, drawable)
        background = this
    }

}