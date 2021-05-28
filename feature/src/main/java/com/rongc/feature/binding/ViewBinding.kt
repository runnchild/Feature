package com.rongc.feature.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ClickUtils

/**
 * View点击事件，默认不传递参数。如果需要传递参数可定义方法
 * <code>
 *     fun click(params: Any): () -> Unit = {
 *          log(params)
 *     }
 * </code>
 */
@BindingAdapter("android:onClick", "disableDebounce", requireAll = false)
fun View.onClick(call: (() -> Unit)?, disableDebounce: Boolean = false) {
    call ?: return
    onClick({ _: View -> call() }, disableDebounce)
}

/**
 * View点击时间，传递View本身
 * @param disableDebounce 是否关闭去抖动
 */
@BindingAdapter("android:onClick", "disableDebounce", requireAll = false)
fun View.onClick(call: ((View) -> Unit)?, disableDebounce: Boolean = false) {
    call ?: return
    if (!disableDebounce) {
        ClickUtils.applySingleDebouncing(this, call)
    } else {
        setOnClickListener {
            call(it)
        }
    }
}

@BindingAdapter("marginTopStatus")
fun View.addStatusBarMarginTop(add: Boolean) {
    if (add) {
        BarUtils.addMarginTopEqualStatusBarHeight(this)
    } else {
        BarUtils.subtractMarginTopEqualStatusBarHeight(this)
    }
}

@BindingAdapter("level")
fun View.level(level: Int) {
    if (this is ImageView) {
        drawable ?: background
    } else {
        background
    }.level = level
}

@BindingAdapter("fromHtml")
fun TextView.fromHtml(str: String?) {
    text = HtmlCompat.fromHtml(str ?: "", 0)
}