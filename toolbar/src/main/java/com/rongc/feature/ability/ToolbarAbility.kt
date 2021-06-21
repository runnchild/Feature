package com.rongc.feature.ability

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.core.view.forEach
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.rongc.feature.R
import com.rongc.feature.toolbar.BarConfig
import com.rongc.feature.toolbar.PsnToolbar
import com.rongc.feature.toolbar.ToolBarViewModel
import com.rongc.feature.ui.host.IHost

/**
 * <p>
 * describe:
 * 包含返回按键、标题、可多个菜单按钮的简约Toolbar
 * </p>
 * @author qiurong
 * @date 2021/5/28
 */
class ToolbarAbility(private val host: IHost<*>, private val config: BarConfig.() -> Unit = {}) :
    IAbility {

    private lateinit var toolBar: PsnToolbar

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        if (host is Fragment) {
            findToolBar(host.requireView())
        } else if (host is Activity) {
            findToolBar(host.window.decorView)
        }

        if (!::toolBar.isInitialized) {
            throw IllegalStateException("PsnToolbar not found")
        }
        val toolBarViewModel = ToolBarViewModel()
        toolBarViewModel.backLiveData.observe(owner) {
            if (host is DialogFragment) {
                host.dismiss()
                return@observe
            }
            if (host is Fragment) {
                host.requireActivity()
            } else {
                host as Activity
            }.onBackPressed()
        }
        toolBarViewModel.setConfig(BarConfig().apply(config))
        toolBar.setViewModel(toolBarViewModel)
    }

    private fun findToolBar(view: View): PsnToolbar? {
        if (view is PsnToolbar) {
            toolBar = view
            return toolBar
        }
        if (view.findViewById<ViewStub>(R.id.psn_toolBar) != null) {
            toolBar = view.findViewById<ViewStub>(R.id.psn_toolBar).inflate() as PsnToolbar
            return toolBar
        }
        if (view is ViewGroup) {
            view.forEach {
                val findView = findToolBar(it)
                if (findView is PsnToolbar) {
                    return findView
                }
            }
        }
        return null
    }
}