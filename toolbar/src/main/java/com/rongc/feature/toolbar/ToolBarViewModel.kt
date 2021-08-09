package com.rongc.feature.toolbar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToolBarViewModel : ViewModel() {
    //    val barConfig = MutableLiveData<BarConfig>()
    val toolbarConfig = MutableLiveData<ToolbarConfig>()
    val statusBarConfig = MutableLiveData<StatusBarConfig>()

    val backClick = {
        backLiveData.value = true
    }

//    val menuItems = ObservableArrayList<TextView.() -> Unit>()
//    var titleBlock: (TextView.() -> Unit)? = null
//    var navigationBlock: (ImageView.() -> Unit)? = null

    fun setToolbarConfig(config: ToolbarConfig) {
        if (config.iconLightModeColor != BarConfig.UNDEFINE) {
            config.navigationIcon.setTint(
                if (config.isLightMode) {
                    config.iconLightModeColor
                } else {
                    config.iconColor
                }
            )
        }

        if (config.titleLightModeColor != BarConfig.UNDEFINE) {
            config._titleColor = if (config.isLightMode) {
                config.titleLightModeColor
            } else {
                config.titleDarkModeColor
            }
        }
        this.toolbarConfig.value = config
    }

    fun setStatusBarConfig(statusBarConfig: StatusBarConfig) {
        this.statusBarConfig.value = statusBarConfig
    }

    fun setBarConfig(barConfig: BarConfig) {
//        this.barConfig.value = barConfig
        setStatusBarConfig(barConfig.statusBarConfig)
        setToolbarConfig(barConfig.toolbarConfig)

//        if (barConfig.bottomLineColor != BarConfig.UNDEFINE) {
//            dividerColor.set(barConfig.bottomLineColor.toDrawable())
//        }
//        menuItems.clear()
//        menuItems.addAll(barConfig.menuItems)
//        backVisible.set(barConfig.navigationVisible)
//
//        val toolbarBackDrawable = barConfig.navigationIcon
//        toolbarBackDrawable.setTint(if (!barConfig.isLightMode) Color.WHITE else Color.BLACK)
//        backIcon.set(toolbarBackDrawable)
//        background.set(barConfig.background.toDrawable())
//        toolbarVisible.set(barConfig.toolbarVisible)
//        titleVisible.set(barConfig.titleVisible)
//        title.set(barConfig.title)
//        titleBlock = barConfig.titleBlock
//        navigationBlock = barConfig.navigationBlock
    }

//    val toolbarVisible = ObservableBoolean(true)
//    val title = ObservableField<CharSequence>()
//    val titleVisible = ObservableBoolean(true)
//
//    val backVisible = ObservableBoolean(true)
//    val dividerColor = ObservableField<Drawable>()
//    val dividerSize = ObservableFloat(0.5f.dp)
//    val backIcon = ObservableField<Drawable>()
//    val background = ObservableField<Drawable>()

    val backLiveData = MutableLiveData<Boolean>()
}