package com.rongc.feature.toolbar

class BarConfig {
    companion object {
        const val UNDEFINE = -2

        const val TRANSPARENT_WHITE = 1
        const val TRANSPARENT_BLACK = 2
    }

    internal var toolbarConfig = ToolbarConfig()
    internal var statusBarConfig = StatusBarConfig()

    fun toolbar(block: ToolbarConfig.() -> Unit) {
        toolbarConfig.apply(block)
    }

    fun statusBar(block: StatusBarConfig.() -> Unit) {
        statusBarConfig.apply(block)
    }
}