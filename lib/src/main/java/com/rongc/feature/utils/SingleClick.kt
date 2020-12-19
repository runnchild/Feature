package com.rongc.feature.utils

import android.view.View
import com.rongc.feature.R

/**
 * Desc:按钮1秒内只允许点击一次
 * Author: QiuRonaC
 * Date: 19-4-26
 * Copyright: Copyright (c) 2013-2018
 * Company: @米冠网络
 * Update Comments:
 */
fun View.click(listener: View.OnClickListener) {
    setOnClickListener {
        val millis = System.currentTimeMillis()
        val firstClickTime = getTag(R.id.tag_time_mills) as? Long ?: 0
        if (firstClickTime + 500L < millis) {
            listener.onClick(this)
            setTag(R.id.tag_time_mills, millis)
        }
    }
}

fun View.singleClick(listener: (v: View) -> Unit) {
    setOnClickListener {
        val millis = System.currentTimeMillis()
        val firstClickTime = getTag(R.id.tag_time_mills) as? Long ?: 0
        if (firstClickTime + 500L < millis) {
            listener(this)
            setTag(R.id.tag_time_mills, millis)
        }
    }
}

fun View.doubleClick(listener: (v: View) -> Unit) {
    setOnClickListener {
        val millis = System.currentTimeMillis()
        val firstClickTime = getTag(R.id.tag_time_mills) as? Long ?: 0
        if (millis - firstClickTime in 100..500) {
            listener(this)
            setTag(R.id.tag_time_mills, 0)
        } else {
            setTag(R.id.tag_time_mills, millis)
        }
    }
}

/**
 * 防李璐帕金森点击
 * 点击已经发生，800毫秒内只执行一次
 */
fun View.singleAction(call: () -> Unit) {
    val millis = System.currentTimeMillis()
    val firstClickTime = getTag(R.id.tag_time_mills) as? Long ?: 0
    if (firstClickTime + 400L < millis) {
        call()
        setTag(R.id.tag_time_mills, millis)
    }
}

/**
 * 350毫秒内如果点击了2次算双击，1次算单击
 */
fun View.doubleOrSingle(call: () -> Unit, doubleClick: () -> Unit) {
    setOnClickListener {
        val millis = System.currentTimeMillis()
        val firstClickTime = getTag(R.id.tag_time_mills) as? Long ?: 0
        if (millis - firstClickTime in 100..350) {
            removeCallbacks(getTag(R.id.tag_binding) as? Runnable)
            setTag(R.id.tag_time_mills, 0)
            doubleClick()
        } else {
            val post = getTag(R.id.tag_binding) as? Runnable ?: Runnable {
                call()
            }
            setTag(R.id.tag_binding, post)
            setTag(R.id.tag_time_mills, millis)

            postDelayed(post, 350)
        }
    }
}


/**
 * 350毫秒内如果执行了2次算双击，1次算单击
 */
fun View.doubleOrSingleAction(call: () -> Unit, doubleClick: () -> Unit) {
    val millis = System.currentTimeMillis()
    val firstClickTime = getTag(R.id.tag_time_mills) as? Long ?: 0
    if (millis - firstClickTime in 100..350) {
        removeCallbacks(getTag(R.id.tag_binding) as? Runnable)
        setTag(R.id.tag_time_mills, 0)
        doubleClick()
    } else {
        val post = getTag(R.id.tag_binding) as? Runnable ?: Runnable {
            call()
        }
        setTag(R.id.tag_binding, post)
        setTag(R.id.tag_time_mills, millis)

        postDelayed(post, 350)
    }
}

/**
 * 350毫秒内如果执行了2次算双击，1次算单击 同时相应单击和双击
 */
fun View.doubleAndSingle(call: () -> Unit, doubleClick: (isSingle: Boolean) -> Unit) {
    setOnClickListener {
        val millis = System.currentTimeMillis()
        val firstClickTime = getTag(R.id.tag_time_mills) as? Long ?: 0
        if (millis - firstClickTime in 100..350) {
            setTag(R.id.tag_time_mills, 0)
            doubleClick(false)
        } else {
            setTag(R.id.tag_time_mills, millis)
            call()
        }
    }
}