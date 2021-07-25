package com.runnchild.emptyview

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class EmptyViewConfig {
    val btnVisible = ObservableBoolean()
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
            btnVisible.set(it.btnVisible)
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