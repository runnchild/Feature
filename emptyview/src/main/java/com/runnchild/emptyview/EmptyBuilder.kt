package com.runnchild.emptyview

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable

class EmptyBuilder(val state: EmptyState = EmptyState.EMPTY_DATA) {
    var tip: String? = null
        set(value) {
            tip {
                text = value
            }
            field = value
        }
    var subTip: String? = null
        set(value) {
            subTip {
                text = value
            }
            field = value
        }

    var btnVisible: Boolean = true

    var btnText: String? = "刷新"
        set(value) {
            refreshBtn {
                text = value
            }
            field = value
        }
    var btnClick: () -> Unit = {}
    var iconDrawable: Drawable? = Color.GRAY.toDrawable()
        set(value) {
            icon {
                setImageDrawable(value)
            }
            field = value
        }

    var refreshBuilder: (TextView.() -> Unit)? = null
    var tipBuilder: (TextView.() -> Unit)? = null
    var subTipBuilder: (TextView.() -> Unit)? = null
    var iconBuilder: (ImageView.() -> Unit)? = null

    fun refreshBtn(build: TextView.() -> Unit) {
        refreshBuilder = build
    }

    fun tip(build: TextView.() -> Unit) {
        tipBuilder = build
    }

    fun subTip(build: TextView.() -> Unit) {
        subTipBuilder = build
    }

    fun icon(build: ImageView.() -> Unit) {
        iconBuilder = build
    }
}