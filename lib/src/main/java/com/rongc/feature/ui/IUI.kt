package com.rongc.feature.ui

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStoreOwner
import com.rongc.feature.model.BaseModel
import com.rongc.feature.ui.toolbar.BarConfig
import com.rongc.feature.viewmodel.BaseViewModel

interface IUI<M : BaseViewModel<out BaseModel>> : ViewModelStoreOwner {
    /**
     * 页面View设置好后调用, 优先{@link #initData()}
     */
    fun initView(view: View)

    /**
     * 页面View设置好后, 晚于{@link #initView(android.View)}调用
     */
    fun initData()

    /**
     * 创建ViewModel, 默认创建泛型提供的ViewModel。
     */
    fun generateViewModel(modelClass: Class<M>): M

    fun viewModel(): M

    fun getLifecycle(): Lifecycle

    fun getContext(): Context?

    /**
     * @description  显示dialog
     * @author rongc
     * @date 20-8-19
     */
    fun showDialog()

    /**
     * @description 隐藏dialog
     * @author rongc
     * @date 20-8-19
     */
    fun dismissDialog()

    /**
     * @description 退出页面
     * @author rongc
     * @date 20-8-20
     */
    fun navigateUp()

    /**
     * @description 控件点击回调
     * @author rongc
     * @date 20-8-21
     * @param view 被点击的View
     */
    fun viewClick(view: View) {
    }

    fun getUiDelegate(action: (M) -> Unit): UiDelegate<M>
    fun initObserver()

    /**
     * 初始化状态栏和导航栏信息
     */
    fun getBarConfig(): BarConfig.() -> Unit = { statusColor = -1 }

    fun refreshConfig()
}