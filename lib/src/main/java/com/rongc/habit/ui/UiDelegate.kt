package com.rongc.habit.ui

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.rongc.habit.model.BaseModel
import com.rongc.habit.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * 页面基本业务代理类，维护页面方法执行顺序，监听基本ui Observer变化
 */
open class UiDelegate<M : BaseViewModel<out BaseModel>>(val api: IUI<M>, action: (M) -> Unit) {

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
        api.initView(root)
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

        viewModel.toastVisible.observe(owner, Observer {
            Toast.makeText(api.getContext(), it, Toast.LENGTH_SHORT).show()
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
}