package com.rongc.feature.viewmodel

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.lifecycle.ViewModel
import com.rongc.feature.R
import com.rongc.feature.SingleLiveData
import com.rongc.feature.ui.toolbar.BarConfig
import com.rongc.feature.utils.Compat.color
import com.rongc.feature.utils.Compat.dp
import com.rongc.feature.utils.Compat.drawable

class ToolBarViewModel: ViewModel() {

    val backClick = {
        backLiveData.value = true
    }

    val menuItems = ObservableArrayList<TextView.() -> Unit>()
    val imageMenuItems = arrayListOf<TextView.() -> Unit>()

    fun setConfig(barConfig: BarConfig) {
        if (barConfig.toolBarDividerColor != -2) {
            dividerColor.set(barConfig.toolBarDividerColor.toDrawable())
        }
        menuItems.clear()
        menuItems.addAll(barConfig.menuItems)
        barConfig.menuItems.clear()
        backVisible.set(barConfig.toolbarBackVisible)

        val toolbarBackDrawable = barConfig.toolbarBackDrawable
        toolbarBackDrawable.setTint(if (!barConfig.isLightMode) Color.WHITE else Color.BLACK)
        backIcon.set(toolbarBackDrawable)
        background.set(barConfig.toolBarBackground.toDrawable())
        toolbarVisible.set(barConfig.toolbarVisible)
        titleVisible.set(barConfig.titleVisible)
    }

    val toolbarVisible = ObservableBoolean(true)
    val title = ObservableField<CharSequence>()
    val titleVisible = ObservableBoolean(true)

    val backVisible = ObservableBoolean(true)
    val dividerColor = ObservableField<Drawable>(R.color.divider_color.color().toDrawable())
    val dividerSize = ObservableFloat(0.5f.dp())
    val backIcon = ObservableField<Drawable>(R.mipmap.common_icon_back.drawable())
    val background = ObservableField<Drawable>()

    val backLiveData = SingleLiveData<Boolean>()
}