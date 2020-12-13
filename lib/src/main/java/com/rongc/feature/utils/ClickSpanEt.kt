package com.rongc.feature.utils

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.text.clearSpans
import androidx.databinding.BindingAdapter

/**
 * @description 为一个文本设置多个ClickSpan， 每个ClickSpan内容需用{0}标注，
 * 并且每个标注需要成对连续的出现
 * 比如： abc{0}clickSpan{0}edf{1}dfd{1}
 * @author rongc
 * @date 20-8-21
 * @param content 需要设置的内容，为空使用TextView里面的text
 * @param clicks span点击回调
 * note: 使用了此方法在页面退出时clicks仍被ClickableSpan内部持有造成泄露，
 * 因此在退出时需调用#clearSpans
 */
@BindingAdapter("spanColor", "spanClicks", "spanContent")
fun TextView.setClickSpans(
    spanColor: Int,
    clicks: List<() -> Unit>,
    content: CharSequence? = null
) {
    val text = content ?: text ?: return
    movementMethod = LinkMovementMethod()
    val indexArr = mutableListOf<Pair<Int, Int>>()
    val replaceText = text.run {
        var lastTargetLen = 0
        repeat(100) {
            val target = "{$it}"
            val indexOf = indexOf(target)
            if (indexOf < 0) {
                return@repeat
            }
            val start = kotlin.math.max(0, indexOf - lastTargetLen)
            val end = lastIndexOf(target) - lastTargetLen - target.length
            indexArr.add(start to end)
            lastTargetLen += target.length * 2
        }

        replace("[{]\\d+[}]".toRegex(), "")
    }
    highlightColor = 0
    this.text = SpannableStringBuilder(replaceText).apply {
        indexArr.forEachIndexed { index, pair ->
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        clicks.getOrNull(index)?.invoke()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = spanColor
                    }
                },
                pair.first,
                pair.second,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}

fun TextView.clearSpans() {
    (text as? SpannableString)?.clearSpans()
    try {
        val field = text::class.java.superclass.getDeclaredField("mSpans")
        field.isAccessible = true
        field.set(this, null)
    } catch (e: Exception) {
    }
}