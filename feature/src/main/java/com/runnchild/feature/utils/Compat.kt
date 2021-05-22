package com.runnchild.feature.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

/**
 * Desc: 常用的扩展方法
 *
 * Date: 2020/5/19
 * Copyright: Copyright (c) 2019-2020
 * Company: @微微科技有限公司
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * @author: QiuRongCai
 */

// -------------------dimension--------------------------------------------
val Float.dp: Float
    get() {
        val it = Utils.getApp()
        val res = it.resources.getIdentifier("dp$this", "dimen", it.packageName)
        return if (res != 0) {
            it.resources.getDimension(res)
        } else {
            SizeUtils.dp2px(this@dp).toFloat()
        }
    }
val Int.dp get() = toFloat().dp

val Float.idp get() = dp.toInt()

val Int.idp get() = dp.toInt()

val Float.sp: Float
    get() {
        val it = Utils.getApp()
        val res = it.resources.getIdentifier("sp$this", "dimen", it.packageName)
        return if (res != 0) {
            it.resources.getDimension(res)
        } else {
            SizeUtils.dp2px(this).toFloat()
        }
    }
val Int.sp get() = toFloat().sp
val Int.isp get() = toFloat().sp.toInt()
// -------------------dimension end-----------------------------------------

fun Int.color() = ContextCompat.getColor(Utils.getApp(), this)

fun Int.string(): String = StringUtils.getString(this)

fun Int.drawable() = ContextCompat.getDrawable(Utils.getApp(), this)

/**
 * 将16进制color转换为rgb
 * eg:0x7a8a9a
 * @param color Int
 * @return Int
 */
fun Int.rgb(): Int {
    return Color.rgb(
        this and 0xFF0000 shr 16,
        this and 0x00FF00 shr 8,
        this and 0x0000FF shr 0
    )
}

fun Int?.toBoolean(): Boolean = this != null && this != 0
fun Boolean.toInt() = if (this) 1 else 0

fun String?.safeInt(default: Int = 0): Int {
    return try {
        this?.toInt() ?: default
    } catch (e: Exception) {
        default
    }
}

fun Any?.logd() {
    LogUtils.d(this)
}

fun Any?.logi() {
    LogUtils.i(this)
}

fun Any?.logw() {
    LogUtils.w(this)
}

fun Any?.loge() {
    LogUtils.e(this)
}
// -----------------------GsonKt----------------------------
/**
 * String 解析为List<T>
 * @receiver String json字符串
 * @return List<T>
 */
inline fun <reified T> String.parseList(): ArrayList<T> {
    return Gson().fromJson(this, object : TypeToken<List<T>>() {}.type)
}

/**
 * String 解析为对象T
 * @receiver String json字符串
 * @return T
 */
inline fun <reified T> String.parse(): T {
    return Gson().fromJson(this, T::class.java)
}

fun Any.toJson(): String {
    return Gson().toJson(this)
}

fun <T> String?.opt(block: JSONObject.() -> T): T? {
    try {
        this ?: return null
        return JSONObject(this).run(block)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun String?.optString(what: String, default: String = ""): String {
    return opt { optString(what, default) } ?: ""
}
// -------------------------GsonKt end -----------------------

fun View.setBgDrawable(drawable: Drawable) = ViewCompat.setBackground(this, drawable)

@BindingAdapter(
    "round_color",
    "tl_radius",
    "tr_radius",
    "br_radius",
    "bl_radius",
    requireAll = false
)
fun View.setRoundBg(color: Int, l: Float = 0f, t: Float = 0f, r: Float = 0f, b: Float = 0f) {
    var bgColor = color
    if (bgColor == 0 && background is ColorDrawable) {
        bgColor = (background as ColorDrawable).color
    }
    setBgDrawable(getRoundDrawable(bgColor, l, t, r, b))
}

fun getRoundDrawable(
    color: Int,
    l: Float = 0f,
    t: Float = 0f,
    r: Float = 0f,
    b: Float = 0f
): Drawable {
    val arrayOf = floatArrayOf(l, l, t, t, r, r, b, b)
    val drawable = ShapeDrawable(RoundRectShape(arrayOf, null, null))
    drawable.paint.color = color
    return drawable
}

/**
 * 设置可圆角的View背景
 * @receiver View
 * @param color Int背景颜色
 * @param r Float 圆角大小
 */
@BindingAdapter("round_color", "round_radius", requireAll = false)
fun View.setRoundBg(color: Int, r: Float = 0f) {
    setRoundBg(color, r, r, r, r)
}

fun View.activity() = context.activity()

fun Context.activity(): Activity? {
    var context = this
    if (context is Activity) {
        //context本身是Activity的实例
        return context
    } else if (context is ContextWrapper) {
        //Activity有可能被系统＂装饰＂，看看context.base是不是Activity
        context = context.baseContext
        if (context is Activity) {
            return context
        }
    }
    return null
}

fun View?.removeFromParent() {
    (this?.parent as? ViewGroup)?.removeView(this)
}

@BindingAdapter("visible")
fun View.visible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("orInvisible")
fun View.orInvisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun TextView.drawableSide(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom)
}

fun Float.formatWith(fraction: Int = 1, isHalf: Boolean = false): String? {
    return NumberUtils.format(this, fraction, isHalf)
}

fun Double.formatWith(fraction: Int = 1, isHalf: Boolean = false): String? {
    return NumberUtils.format(this, fraction, isHalf)
}