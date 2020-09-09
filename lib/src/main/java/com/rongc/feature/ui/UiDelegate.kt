package com.rongc.feature.ui

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.blankj.utilcode.util.BarUtils
import com.rongc.feature.model.BaseModel
import com.rongc.feature.ui.toolbar.BarConfig
import com.rongc.feature.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * 页面基本业务代理类，维护页面方法执行顺序，监听基本ui Observer变化
 */
open class UiDelegate<M : BaseViewModel<out BaseModel>>(val api: IUI<M>, action: (M) -> Unit) {

    private val barConfig by lazy { BarConfig() }
//    var toolBar: PsnToolbar? = null

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

    fun init(owner: LifecycleOwner, root: View) {
        initObserver(owner, api.viewModel())

        api.initObserver()
        api.initView(root)
        api.initData()
    }

    open fun initObserver(owner: LifecycleOwner, viewModel: M) {
        viewModel.mainScope.onCreate()
        api.getLifecycle().addObserver(viewModel)
        viewModel.dialogVisible.observe(owner, Observer {
            if (it) {
                showDialog()
            } else {
                dismissDialog()
            }
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

    fun setupStatusBar(activity: Activity) {
        barConfig.apply { statusColor = -1 }.apply(api.getBarConfig())

        if (barConfig.isStatusTransparent) {
            BarUtils.transparentStatusBar(activity)
            hideStatusBarView(activity)
        } else if (barConfig.statusColor != BarConfig.UNDEFINE) {
            BarUtils.setStatusBarColor(activity, barConfig.statusColor)
        }
        BarUtils.setStatusBarLightMode(activity, barConfig.isLightMode)
        if (barConfig.navColor != BarConfig.UNDEFINE) {
            BarUtils.setNavBarColor(activity, barConfig.navColor)
            BarUtils.setNavBarLightMode(activity, barConfig.navLightMode)
        }

        activity.findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)?.let {
            if (!barConfig.isStatusTransparent) {
                BarUtils.addMarginTopEqualStatusBarHeight(it)
            } else {
                BarUtils.subtractMarginTopEqualStatusBarHeight(it)
            }
        }
    }

    fun refreshConfig(activity: Activity) {
        setupStatusBar(activity)
        api.viewModel().toolbarModel?.setConfig(barConfig)
    }

    private fun hideStatusBarView(activity: Activity) {
        val decorView = activity.window.decorView.findViewWithTag<View>("TAG_STATUS_BAR")
        decorView?.visibility = View.GONE
    }
}