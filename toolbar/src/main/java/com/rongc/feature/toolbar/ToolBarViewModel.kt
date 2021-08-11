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

    fun setToolbarConfig(configApply: ToolbarConfig.() -> Unit) {
        val config = (toolbarConfig.value ?: ToolbarConfig()).apply(configApply)
        setToolbar(config)
    }

    private fun setToolbar(config: ToolbarConfig) {
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

    fun setStatusBarConfig(config: StatusBarConfig.() -> Unit) {
        setStatusBar((statusBarConfig.value ?: StatusBarConfig()).apply(config))
    }

    private fun setStatusBar(config: StatusBarConfig) {
        statusBarConfig.value = config
    }

    fun setBarConfig(barConfig: BarConfig) {
        setStatusBar(barConfig.statusBarConfig)
        setToolbar(barConfig.toolbarConfig)
    }

    val backLiveData = MutableLiveData<Boolean>()
}