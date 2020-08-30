package com.rongc.feature.ui

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.forEach
import androidx.lifecycle.*
import com.blankj.utilcode.util.BarUtils
import com.rongc.feature.model.BaseModel
import com.rongc.feature.ui.toolbar.BarConfig
import com.rongc.feature.ui.toolbar.PsnToolbar
import com.rongc.feature.utils.Compat.activity
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.feature.viewmodel.ToolBarViewModel
import java.lang.reflect.ParameterizedType

/**
 * 页面基本业务代理类，维护页面方法执行顺序，监听基本ui Observer变化
 */
open class UiDelegate<M : BaseViewModel<out BaseModel>>(val api: IUI<M>, action: (M) -> Unit) {

    private val barConfig by lazy { BarConfig() }

    private val dialog by lazy {
        AlertDialog.Builder(api.getContext()!!)
            .setView(ProgressBar(api.getContext()))
            .create()
    }

    init {
        val provideViewModel = provideViewModel(api)
        action(provideViewModel)
    }

    @Suppress("UNCHECKED_CAST")
    fun provideViewModel(owner: ViewModelStoreOwner): M {
        val modelClass = (owner.javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments.last() as Class<M>
        return ViewModelProvider(owner, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(clz: Class<T>): T {
                return api.generateViewModel(modelClass) as T
            }
        }).get(modelClass)
    }

    private fun initToolBar(owner: LifecycleOwner, view: View) {
        barConfig.apply(api.getBarConfig())

        initStatusBar(view.activity()!!)

        findToolBar(view)?.let {
            val toolBarViewModel = ToolBarViewModel()
            api.viewModel().toolbarModel = toolBarViewModel
            toolBarViewModel.setConfig(barConfig)
            it.setViewModel(owner, toolBarViewModel)
        }

        BarUtils.addMarginTopEqualStatusBarHeight(
            view.activity()?.findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0) ?: return
        )
    }

    private fun findToolBar(view: View): PsnToolbar? {
        if (view is PsnToolbar) {
            return view
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

    fun init(owner: LifecycleOwner, root: View) {
        api.viewModel().mainScope.onCreate()
        api.initObserver()
        api.initView(root)
        initToolBar(owner, root)
        api.initData()
        api.getLifecycle().addObserver(api.viewModel())

        initObserver(owner, api.viewModel())
    }

    open fun initObserver(owner: LifecycleOwner, viewModel: M) {
        viewModel.dialogVisible.observe(owner, Observer {
            if (it) {
                showDialog()
            } else {
                dismissDialog()
            }
        })

        viewModel.toolbarModel?.backLiveData?.observe(owner, Observer {
            navigateUp()
        })
        viewModel.finish.observe(owner, Observer {
            navigateUp()
        })
        viewModel.viewsClickLiveData.observe(owner, Observer {
            api.viewClick(it)
        })
    }

    private fun navigateUp() {
        api.navigateUp()
    }

    fun destroy() {
        api.getLifecycle().removeObserver(api.viewModel())
    }

    fun showDialog() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun dismissDialog() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun initStatusBar(activity: Activity) {
        if (barConfig.statusColor != -2) {
            BarUtils.setStatusBarColor(activity, barConfig.statusColor)
            BarUtils.setStatusBarLightMode(activity, barConfig.isLightMode)
        }
        if (barConfig.navColor != -2) {
            BarUtils.setNavBarColor(activity, barConfig.navColor)
            BarUtils.setNavBarLightMode(activity, barConfig.navLightMode)
        }
        if (barConfig.isStatusTransparent) {
            BarUtils.transparentStatusBar(activity)
        }
    }
}