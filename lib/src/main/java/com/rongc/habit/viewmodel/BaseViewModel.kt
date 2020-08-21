package com.rongc.habit.viewmodel

import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.rongc.habit.SingleLiveData
import com.rongc.habit.model.BaseModel
import com.rongc.habit.network.MainScope
import com.rongc.habit.network.ServicesException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.ParameterizedType
import java.net.ConnectException

/**
 * 含有生命周期感知能力的ViewModel， 继承者也需要感知生命周期方法，重写对应方法即可
 * 内置了协程处理线程切换能力，默认在子线程执行。@see #launch(), 任务将会在页面销毁时自动销毁
 */
abstract class BaseViewModel<M : BaseModel> : ViewModel(), LifecycleObserver {

    open lateinit var model: M

    var mainScope = MainScope()
    val dialogVisible = SingleLiveData<Boolean>()
    val toastVisible = SingleLiveData<String>()
    val finish = SingleLiveData<Boolean>()

    val finishClick = {
        finish()
    }

    /**
     * 页面控件的点击监听
     */
    val viewsClickLiveData = SingleLiveData<View>()

    /**
     * 页面控件的点击回调
     */
    val viewsClick = { v: View -> viewsClickLiveData.value = v }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        mainScope.onCreate()
        val modelCls = (this::class.java.genericSuperclass as ParameterizedType)
            .actualTypeArguments.lastOrNull() as? Class<*>
        @Suppress("UNCHECKED_CAST")
        model = modelCls?.newInstance() as M
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        mainScope.onDestroy()
    }

    /**
     * 发起协程任务， 将会在子线程执行， 主线程处理结果
     * @param scope 被声明为suspend的任务体，将会在子线程执行
     * @param failed 任务异常回调
     * @param showDialog 发起任务时是否弹出加载框，默认为true
     * @param showToast 发起任务时是否弹出提示，默认为true
     *
     */
    fun launch(
        scope: suspend () -> Unit,
        failed: ((Exception) -> Unit)? = null,
        showDialog: Boolean = true,
        showToast: Boolean = true
    ) {
        mainScope.launch {
            try {
                dialogVisible(showDialog)
                withContext(Dispatchers.IO) {
                    scope()
                }
                dialogVisible(false)
            } catch (e: Exception) {
                dialogVisible(false)
                failed?.invoke(e)
                if (showToast) {
                    if (e is ServicesException && !e.message.isNullOrEmpty()) {
                        showToast(e.message!!)
                    } else if (e is ConnectException) {
                        showToast("网络连接失败")
                    }
                }
            }
        }
    }

    private fun dialogVisible(visible: Boolean) {
        dialogVisible.value = visible
    }

    fun showDialog() {
        dialogVisible(true)
    }

    fun dismissDialog() {
        dialogVisible(false)
    }

    private fun showToast(toast: String) {
        toastVisible.value = toast
    }

    fun finish() {
        finish.value = true
    }
}