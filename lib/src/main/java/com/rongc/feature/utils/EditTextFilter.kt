package com.rongc.feature.utils

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.RegexUtils
import kotlin.math.ceil


/**
 * @description 字符长度过滤，允许设置最大长度为#maxLength个英文字符或者#maxLength/2个中文汉字
 * @author rongc
 * @date 20-8-25$
 * @update
 * @param onlyChinese 是否仅支持中文
 * @param allowAll 是否允许所有字符，否则按规则过滤 #regex
 */
class EditTextFilter(
    private val maxLength: Int,
    private val onlyChinese: Boolean = false,
    private val allowAll: Boolean = true
) : InputFilter {
    companion object {
        /**
         * 获取字符数量 汉字占2个长度，英文占1个长度
         *
         * @param text
         * @return
         */
        fun getTextLength(text: String): Int {
            var length = 0
            for (element in text) {
                if (element.toInt() > 255) {
                    length += 2
                } else {
                    length++
                }
            }
            return length
        }

        /**
         * 获取字符数量， 汉字占1，字母占0.5
         */
        fun getTextNum(text: String): Int {
            var length = 0.0
            for (element in text) {
                if (element.toInt() <= 255) {
                    length += 0.5f
                } else {
                    length++
                }
            }
            return ceil(length).toInt()
        }
    }

    private fun regex(source: CharSequence): Boolean {
        return RegexUtils.isMatch(
            if (onlyChinese) "^[\\u4e00-\\u9fa5]+" else "^[\\w\\u4e00-\\u9fa5]+",
            source
        )
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        // 判断EditText输入内容+已有内容长度是否超过设定值，超过则做处理
        if (!allowAll && !regex(source)) {
            return ""
        }
        if (getTextLength(dest.toString()) + getTextLength(source.toString()) > maxLength) {
            // 输入框内已经有20个字符则返回空字符
            when {
                getTextLength(dest.toString()) >= 20 -> {
                    // 如果输入框内没有字符，且输入的超过了20个字符，则截取前10个汉字
                    return ""
                }
                getTextLength(dest.toString()) == 0 -> {
                    return source.toString().substring(0, 10)
                }
                else -> {
                    // 输入框已有的字符数为双数还是单数
                    return if (getTextLength(dest.toString()) % 2 == 0) {
                        source.toString().substring(0, 10 - getTextLength(dest.toString()) / 2)
                    } else {
                        source.toString()
                            .substring(0, 10 - (getTextLength(dest.toString()) / 2 + 1))
                    }
                }
            }
        }
        return source
    }
}

@BindingAdapter("lengthFilter", "onlyChinese", "allowAll", requireAll = false)
fun EditText.setLengthFilter(size: Int, onlyChinese: Boolean, allowAll: Boolean) {
    filters = arrayOf(EditTextFilter(size, onlyChinese, allowAll))
}

@BindingAdapter("idFilter")
fun EditText.setIDFilter(filter: Boolean) {
    filters = arrayOf(object : InputFilter.LengthFilter(18) {
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            if (!RegexUtils.isMatch("[0-9Xx]+", source)) {
                return ""
            }
            return super.filter(source, start, end, dest, dstart, dend)
        }
    })
}