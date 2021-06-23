package com.rongc.list.viewmodel

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class EmptyViewConfig {
    val tip = ObservableField<String>()
    val subTip = ObservableField<String>()
    val btnVisible = ObservableBoolean()
    val btnText = ObservableField("刷新")
    val icon = ObservableField<Drawable>()

    var refreshBuilder = ObservableField<TextView.() -> Unit>()
    var tipBuilder = ObservableField<TextView.() -> Unit>()
    var subTipBuilder = ObservableField<TextView.() -> Unit>()
    var iconBuilder = ObservableField<ImageView.() -> Unit>()

    var refreshClick = ObservableField {}

    fun emptyBuilder(builder: EmptyBuilder.() -> Unit) {
        builder(EmptyBuilder().apply(builder))
    }

    fun builder(builder: EmptyBuilder) {
        builder.let {
            tip.set(it.tip)
            subTip.set(it.subTip)
            btnVisible.set(it.btnVisible)
            btnText.set(it.btnText)
            icon.set(it.iconDrawable)
            refreshClick.set(it.btnClick)

            tipBuilder.set(it.tipBuilder)
            tipBuilder.notifyChange()

            subTipBuilder.set(it.subTipBuilder)
            subTipBuilder.notifyChange()

            iconBuilder.set(it.iconBuilder)
            iconBuilder.notifyChange()

            refreshBuilder.set(it.refreshBuilder)
            refreshBuilder.notifyChange()
        }
    }
}

class EmptyBuilder(val state: EmptyState = EmptyState.EMPTY_DATA) {
    var tip: String? = null
    var subTip: String? = null
    var btnVisible: Boolean = true
    var btnText: String? = "刷新"
    var btnClick: () -> Unit = {}
    var iconDrawable: Drawable? = Color.GRAY.toDrawable()

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

enum class EmptyState {
    EMPTY_NET_DISCONNECT,
    EMPTY_NET_UNAVAILABLE,
    EMPTY_SERVICE,
    EMPTY_DATA,
}