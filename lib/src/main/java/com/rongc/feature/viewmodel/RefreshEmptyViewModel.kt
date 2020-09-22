package com.rongc.feature.viewmodel

import android.graphics.drawable.Drawable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.rongc.feature.R
import com.rongc.feature.utils.Compat.drawable

class RefreshEmptyViewModel {
    companion object {
        const val EMPTY_NET_DISCONNECT = -2
        const val EMPTY_NET_UNAVAILABLE = -1
        const val EMPTY_EMPTY = 0
    }

    val tip = ObservableField<String>()
    val subTip = ObservableField<String>()
    val btnVisible = ObservableBoolean()
    val btnText = ObservableField("刷新")
    val icon = ObservableField<Drawable>()

    var refreshClick = {}

    fun showNoNet() {
        emptyBuilder {
            tip = "网络未连接"
            subTip = "请检查你的网络设置后刷新"
            btnVisible = true
            btnText = "刷新"
            iconDrawable = R.mipmap.empty_no_net_work.drawable()
        }
    }

    fun showNetUnavailable() {
        emptyBuilder {
            tip = "网络异常"
            subTip = "请检查你的网络设置后刷新"
            btnVisible = true
            btnText = "刷新"
            iconDrawable = R.mipmap.empty_no_net_work.drawable()
        }
    }

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
            refreshClick = it.refreshClick?:{}
        }
    }
}

class EmptyBuilder {
    var tip: String? = null
    var subTip: String? = null
    var btnVisible: Boolean = true
    var btnText: String? = "刷新"
    var iconDrawable: Drawable? = R.mipmap.empty_no_net_work.drawable()
    var refreshClick:(()->Unit)? = null
}