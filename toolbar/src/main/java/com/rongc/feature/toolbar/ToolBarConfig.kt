package com.rongc.feature.toolbar

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.lifecycle.MutableLiveData
import com.rongc.feature.utils.dp

class ToolBarConfig {

    val backClick = {
        backLiveData.value = true
    }

    val menuItems = ObservableArrayList<TextView.() -> Unit>()

    fun setConfig(barConfig: BarConfig) {
        if (barConfig.toolBarDividerColor != BarConfig.UNDEFINE) {
            dividerColor.set(barConfig.toolBarDividerColor.toDrawable())
        }
        menuItems.clear()
        menuItems.addAll(barConfig.menuItems)
        backVisible.set(barConfig.toolbarBackVisible)

        val toolbarBackDrawable = barConfig.toolbarBackDrawable
        toolbarBackDrawable.setTint(if (!barConfig.isLightMode) Color.WHITE else Color.BLACK)
        backIcon.set(toolbarBackDrawable)
        background.set(barConfig.toolBarBackground.toDrawable())
        toolbarVisible.set(barConfig.toolbarVisible)
        titleVisible.set(barConfig.titleVisible)
        title.set(barConfig.title)
    }

    val toolbarVisible = ObservableBoolean(true)
    val title = ObservableField<CharSequence>()
    val titleVisible = ObservableBoolean(true)

    val backVisible = ObservableBoolean(true)
    val dividerColor = ObservableField<Drawable>()
    val dividerSize = ObservableFloat(0.5f.dp)
    val backIcon = ObservableField<Drawable>()
    val background = ObservableField<Drawable>()

    val backLiveData = MutableLiveData<Boolean>()
}