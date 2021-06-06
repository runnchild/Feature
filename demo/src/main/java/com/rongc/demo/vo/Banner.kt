package com.rongc.demo.vo

import androidx.core.graphics.drawable.toDrawable

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/6/6
 */
data class Banner(val color: Int, val content: String) {
    val background get() = color.toDrawable()
}