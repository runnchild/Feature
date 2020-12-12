package com.rongc.feature.viewmodel

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.rongc.feature.R
import com.rongc.feature.utils.Compat.color
import com.rongc.feature.utils.Compat.drawable

class RefreshEmptyViewModel {
    companion object {
        const val EMPTY_NET_DISCONNECT = -2
        const val EMPTY_NET_UNAVAILABLE = -1
        const val EMPTY_EMPTY = 0
    }

    val tip = ObservableField<String>()
    val tipColor = ObservableInt()
    val subTip = ObservableField<String>()
    val btnVisible = ObservableBoolean()
    val btnText = ObservableField("刷新")
    val icon = ObservableField<Drawable>()
    var refreshBuilder = ObservableField<TextView.() -> Unit>()
    var tipBuilder = ObservableField<TextView.() -> Unit>()
    var subTipBuilder = ObservableField<TextView.() -> Unit>()

    var refreshClick = ObservableField {}

    fun showNoNet(refresh: () -> Unit) {
        emptyBuilder {
            tip = "网络未连接"
            tipColor = R.color.gray_353535.color()
            subTip = "请检查你的网络设置后刷新"
            btnVisible = true
            btnText = "刷新"
            iconDrawable = R.mipmap.empty_no_net_work.drawable()
            refreshClick = refresh
        }
    }

    fun showNetUnavailable(refresh: () -> Unit) {
        emptyBuilder {
            tip = "网络异常"
            tipColor = R.color.gray_353535.color()
            subTip = "请检查你的网络设置后刷新"
            btnVisible = true
            btnText = "刷新"
            iconDrawable = R.mipmap.empty_no_net_work.drawable()
            refreshClick = refresh
        }
    }

    fun emptyBuilder(builder: EmptyBuilder.() -> Unit) {
        this.builder(EmptyBuilder().apply(builder))
    }

    fun builder(builder: EmptyBuilder) {
        builder.let {
            icon.set(it.iconDrawable)

            refreshBuilder.set(builder.refreshBuilder)
            if (refreshBuilder.get() == null) {
                refreshBuilder.set {
                    isVisible = it.btnVisible
                    text = it.btnText
                    setOnClickListener { _ ->
                        it.refreshClick?.invoke()
                    }
                }
            }

            tipBuilder.set(builder.tipBuilder)
            if (tipBuilder.get() == null) {
                tipBuilder.set {
                    text = it.tip
                    if (it.tipColor == null) {
                        setTextColor(Color.parseColor("#b2b2b2"))
                    }
                }
            }

            subTipBuilder.set(builder.subTipBuilder)
            if (subTipBuilder.get() == null) {
                subTipBuilder.set {
                    text = it.subTip
                }
            }
        }
    }
}

class EmptyBuilder {
    var tip: String? = null
    var tipColor: Int? = null
    var subTip: String? = null
    var btnVisible: Boolean = false
    var btnText: String? = "刷新"
    var iconDrawable: Drawable? = R.mipmap.empty_icon_empty.drawable()
    var refreshClick: (() -> Unit)? = null
    var refreshBuilder: (TextView.() -> Unit)? = null
    var tipBuilder: (TextView.() -> Unit)? = null
    var subTipBuilder: (TextView.() -> Unit)? = null

    fun refreshBtn(build: TextView.() -> Unit) {
        refreshBuilder = build
    }

    fun tip(build: TextView.() -> Unit) {
        tipBuilder = build
    }

    fun subTip(build: TextView.() -> Unit) {
        subTipBuilder = build
    }

}