package com.rongc.feature.utils

import android.app.Activity
import android.content.ContextWrapper
import android.graphics.Color
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
object Compat {
    // -------------------dimension--------------------------------------------
    fun Float.dp(): Float {
        val it = Utils.getApp()
        val res = it.resources.getIdentifier("dp$this", "dimen", it.packageName)
        return if (res != 0) {
            it.resources.getDimension(res)
        } else {
            SizeUtils.dp2px(this@dp).toFloat()
        }
    }

    fun Int.dp() = toFloat().dp()

    fun Float.idp() = dp().toInt()

    fun Int.idp() = dp().toInt()

    fun Float.sp(): Float {
        val it = Utils.getApp()
        val res = it.resources.getIdentifier("sp$this", "dimen", it.packageName)
        return if (res != 0) {
            it.resources.getDimension(res)
        } else {
            SizeUtils.dp2px(this).toFloat()
        }
    }

    fun Int.sp() = toFloat().sp()
    fun Int.isp() = toFloat().sp().toInt()

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

    fun Int.toBoolean() = this == 1
    fun Boolean.toInt() = if (this) 1 else 0

    fun String?.safeInt(default: Int = 0): Int {
        return try {
            this?.toInt() ?: default
        } catch (e: Exception) {
            default
        }
    }

    fun String?.toast() {
        ToastUtils.showShort(this ?: return)
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
// -------------------------GsonKt end -----------------------

    fun View.setBgDrawable(drawable: Drawable) = ViewCompat.setBackground(this, drawable)

    @BindingAdapter(
        "round_color",
        "tl_radius",
        "tr_radius",
        "bl_radius",
        "br_radius",
        requireAll = false
    )
    @JvmStatic
    fun View.setRoundBg(color: Int, l: Float = 0f, t: Float = 0f, r: Float = 0f, b: Float = 0f) {
        setBgDrawable(getRoundDrawable(color, l, t, r, b))
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
    @JvmStatic
    fun View.setRoundBg(color: Int, r: Float = 0f) {
        setRoundBg(color, r, r, r, r)
    }

    fun View.activity(): Activity? {
        var context = this.context
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

    @JvmStatic
    @BindingAdapter("visible")
    fun View.visible(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("orInvisible")
    fun View.orInvisible(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    fun <T : View> View.find(id: Int) = findViewById<T>(id)

    fun TextView.drawableSide(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom)
    }
}