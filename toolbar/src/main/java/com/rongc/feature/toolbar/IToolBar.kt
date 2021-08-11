package com.rongc.feature.toolbar

import android.widget.ImageView
import android.widget.TextView

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/5/29
 */
interface IToolBar {
    val viewModel: ToolBarViewModel

    fun toolbar(config: ToolbarConfig.() -> Unit) {
        viewModel.setToolbarConfig(config)
        if (viewModel.toolbarConfig.value?.titleBlock != null) {
            title(viewModel.toolbarConfig.value?.titleBlock)
            viewModel.toolbarConfig.value?.titleBlock = null
        }
        if (viewModel.toolbarConfig.value?.navigationBlock != null) {
            navigation(viewModel.toolbarConfig.value?.navigationBlock)
            viewModel.toolbarConfig.value?.navigationBlock = null
        }
    }

    fun statusBar(config: StatusBarConfig.() -> Unit) {
        viewModel.setStatusBarConfig(config)
    }

    fun title(block: (TextView.() -> Unit)?)

    fun navigation(block: (ImageView.() -> Unit)?)

    fun setBarConfig(barConfig: BarConfig) {
        title(barConfig.toolbarConfig.titleBlock)
        navigation(barConfig.toolbarConfig.navigationBlock)
        viewModel.setBarConfig(barConfig)
    }
}