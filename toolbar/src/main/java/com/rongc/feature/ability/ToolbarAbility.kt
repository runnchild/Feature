package com.rongc.feature.ability

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.view.WindowManager
import androidx.core.graphics.ColorUtils
import androidx.core.view.forEach
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.BarUtils
import com.rongc.feature.toolbar.*
import com.rongc.feature.ui.host.IHost

/**
 * <p>
 * describe:
 * 包含返回按键、标题、可多个菜单按钮的简约Toolbar
 * </p>
 * @author qiurong
 * @date 2021/5/28
 */
open class ToolbarAbility(private val host: IHost, private val config: BarConfig.() -> Unit = {}) :
    IAbility {

    protected lateinit var toolBar: PsnToolbar
    protected lateinit var barConfig: BarConfig

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
        val toolBarConfig = ToolBarConfig()
        toolBarConfig.backLiveData.observe(owner) {
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
        val label = (host as? Activity)?.title
        barConfig = BarConfig()
        barConfig.title = label
        barConfig.apply(config)
        toolBar.setViewModel(toolBarConfig)
    }

    private fun findToolBar(view: View): PsnToolbar? {
        if (view is PsnToolbar) {
            toolBar = view
            return toolBar
        }
        val toolbarStub = view.findViewById<ViewStub>(R.id.psn_toolBar)
        if (toolbarStub != null) {
            toolbarStub.layoutResource = R.layout.include_psn_toolbar
            toolBar = toolbarStub.inflate() as PsnToolbar
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

    override fun onResume(owner: LifecycleOwner) {
        toolBar.getViewModel()?.setConfig(barConfig)
        setupStatusBar()
    }

    private fun setupStatusBar() {
        val activity = when (host) {
            is Fragment -> {
                host.requireActivity()
            }
            is Activity -> {
                host
            }
            else -> {
                return
            }
        }

        if (barConfig.isStatusTransparent) {
            BarUtils.transparentStatusBar(activity)
        } else if (barConfig.statusColor != BarConfig.UNDEFINE) {
//            BarUtils.setStatusBarColor(activity, barConfig.statusColor)
            BarUtils.setStatusBarLightMode(activity, barConfig.isLightMode)
//            toolBar[0].addPaddingTopEqualStatusBar(true)
            setStatusBarColor(activity, barConfig.statusColor)
        }

        if (barConfig.navColor != BarConfig.UNDEFINE) {
            BarUtils.setNavBarColor(activity, barConfig.navColor)
            BarUtils.setNavBarLightMode(activity, barConfig.navLightMode)
        }
    }
}

fun setStatusBarColor(activity: Activity, color: Int) {
    val window = activity.window
    // clear FLAG_TRANSLUCENT_STATUS flag:
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    // finally change the color
    window.statusBarColor = color

    BarUtils.setStatusBarLightMode(activity, ColorUtils.calculateLuminance(color) > 0.5f)
}