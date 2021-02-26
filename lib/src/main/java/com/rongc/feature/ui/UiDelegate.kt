package com.rongc.feature.ui

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.lifecycle.*
import com.blankj.utilcode.util.BarUtils
import com.rongc.feature.model.BaseModel
import com.rongc.feature.ui.toolbar.BarConfig
import com.rongc.feature.ui.toolbar.PsnToolbar
import com.rongc.feature.ui.toolbar.addPaddingTopEqualStatusBar
import com.rongc.feature.viewmodel.BaseViewModel
import kotlinx.coroutines.*
import java.lang.reflect.ParameterizedType

/**
 * 页面基本业务代理类，维护页面方法执行顺序，监听基本ui Observer变化
 */
open class UiDelegate<M : BaseViewModel<out BaseModel>>(val api: IUI<M>, action: (M) -> Unit) {

    val barConfig by lazy { BarConfig() }

    var toolBar: PsnToolbar? = null
    private var dialogJob: Job? = null

    open val dialog by lazy {
        AlertDialog.Builder(api.getContext()!!)
            .setView(ProgressBar(api.getContext()))
            .create()
    }

    init {
        val provideViewModel = provideViewModel(api)
        action(provideViewModel)
        barConfig.apply { statusColor = -1 }.apply(api.getBarConfig())
    }

    @Suppress("UNCHECKED_CAST")
    fun provideViewModel(owner: ViewModelStoreOwner): M {
        var cls: Class<*> = owner.javaClass
        while (cls.genericSuperclass !is ParameterizedType
            || !BaseViewModel::class.java.isAssignableFrom((cls.genericSuperclass as ParameterizedType)
                .actualTypeArguments.last() as Class<*>)
        ) {
            cls = cls.superclass as Class<*>
        }
        val modelClass = (cls.genericSuperclass as ParameterizedType)
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
        viewModel.onCreate()
        api.getLifecycle().addObserver(viewModel)
        viewModel.dialogVisible.observe(owner, Observer {
            if (it) {
                showDialog()
            } else {
                dismissDialog()
            }
        })

        viewModel.backPressed.observe(owner, Observer {
            api.onBackPressed()
        })
        viewModel.finish.observe(owner, Observer {
            api.finish()
        })
        viewModel.viewsClickLiveData.observe(owner, Observer {
            api.viewClick(it)
        })
    }

    fun findToolBar(view: View): PsnToolbar? {
        if (view is PsnToolbar) {
            toolBar = view
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

    fun destroy() {
        api.getLifecycle().removeObserver(api.viewModel())
        dismissDialog()
    }

    fun showDialog() {
        dialogJob?.cancel()
        dialogJob = GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                delay(600)
            }
            if (!dialog.isShowing) {
                dialog.show()
            }
        }
    }

    fun dismissDialog() {
        dialogJob?.cancel()
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun setupStatusBar(activity: Activity) {
        if (barConfig.isStatusTransparent) {
            transparentStatusBar(activity)
        } else if (barConfig.statusColor != BarConfig.UNDEFINE) {
            if (barConfig.statusColor == barConfig.toolBarBackground) {
                transparentStatusBar(activity)
            } else {
                BarUtils.setStatusBarColor(activity, barConfig.statusColor)
            }
        }
        BarUtils.setStatusBarLightMode(activity, barConfig.isLightMode)

        BarUtils.setNavBarColor(activity, barConfig.navColor)
        BarUtils.setNavBarLightMode(activity, barConfig.navLightMode)

//        activity.findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)?.let {
//            if (!barConfig.isStatusTransparent) {
//                BarUtils.addMarginTopEqualStatusBarHeight(it)
//            } else {
//                BarUtils.subtractMarginTopEqualStatusBarHeight(it)
//            }
//        }
    }

    fun transparentStatusBar(activity: Activity) {
        BarUtils.transparentStatusBar(activity)
        hideStatusBarView(activity)

        barConfig.isStatusTransparent = true
        toolBar?.get(0)?.addPaddingTopEqualStatusBar(true)
//        BarUtils.subtractMarginTopEqualStatusBarHeight(
//            activity.findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0) ?: return
//        )
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