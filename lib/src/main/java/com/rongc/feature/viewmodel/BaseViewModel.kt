package com.rongc.feature.viewmodel

import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.*
import com.rongc.feature.SingleLiveData
import com.rongc.feature.model.BaseModel
import com.rongc.feature.network.MainScope
import com.rongc.feature.network.ServicesException
import com.rongc.feature.utils.Compat.toast
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType
import java.net.ConnectException

/**
 * 含有生命周期感知能力的ViewModel， 继承者也需要感知生命周期方法，重写对应方法即可
 * 内置了协程处理线程切换能力，默认在子线程执行。@see #launch(), 任务将会在页面销毁时自动销毁
 */
abstract class BaseViewModel<M : BaseModel> : ViewModel(), LifecycleObserver {

    open lateinit var model: M

    var toolbarModel: ToolBarViewModel? = null

    var mainScope = MainScope()
    val dialogVisible = SingleLiveData<Boolean>()
    val backPressed = SingleLiveData<Boolean>()
    val finish = SingleLiveData<Boolean>()

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
        val modelCls = findModelType(this::class.java)?:throw IllegalStateException("没有找到申明的Model")

        @Suppress("UNCHECKED_CAST")
        model = modelCls.newInstance() as M
    }

    private fun findModelType(clazz: Class<*>?): Class<*>? {
        val type = clazz?.genericSuperclass ?: return null

        if (type is ParameterizedType) {
            (type.actualTypeArguments.lastOrNull() as? Class<*>)?.run {
                if (BaseModel::class.java.isAssignableFrom(this)) {
                    return this
                }
            }
        }
        return findModelType(clazz.superclass as? Class<*>)
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
        scope: suspend (coroutineScope: CoroutineScope) -> Unit,
        failed: ((ServicesException) -> Unit)? = null,
        showDialog: Boolean = true,
        showToast: Boolean = true
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            dialogVisible(false)
            e.printStackTrace()
            val error = when (e) {
                is ServicesException -> {
                    e
                }
                is ConnectException -> {
                    ServicesException(ServicesException.CODE_CONNECTED, "网络连接失败")
                }
                else -> {
                    ServicesException(ServicesException.CODE_OTHER, "服务器错误")
                }
            }
            if (showToast) {
                error.message.toast()
            }

            failed?.invoke(error)
        }
        return viewModelScope.launch(exceptionHandler) {
            dialogVisible(showDialog)
            scope(this)
            dialogVisible(false)
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

    open fun finish() {
        finish.value = true
    }

    fun onBackPressed() {
        backPressed.value = true
    }

    /**
     * 点击提供土司响应
     */
    fun toast(params: String): () -> Unit = {
        params.toast()
    }
}