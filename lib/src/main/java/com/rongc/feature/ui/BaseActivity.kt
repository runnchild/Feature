package com.rongc.feature.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rongc.feature.model.BaseModel
import com.rongc.feature.viewmodel.BaseViewModel

/**
 * 所有Activity的基类, 继承者需要提供ViewModel， 默认会反射创建该实例
 * 设置contentView的方式有两种
 * 如果{@link #getContentViewRes()}返回0则会获取#getContentView()的View作为ContentView
 */
abstract class BaseActivity<M : BaseViewModel<out BaseModel>> : AppCompatActivity(), IUI<M> {

    private lateinit var delegate: UiDelegate<M>
    protected lateinit var viewModel: M

    private val refreshDelegate by lazy {
        this as? IRefreshDelegate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate = getUiDelegate {
            viewModel = it
        }
        val view = when {
            getContentViewRes() > 0 -> {
                View.inflate(this, getContentViewRes(), null)
            }
            else -> {
                getContentView()!!
            }
        }
        setContentView(view)

        delegate.init(this, view)
        viewModel.toolbarModel?.title?.set(title)

        refreshDelegate?.run {
            init(viewModel, this@BaseActivity, view)
        }
    }


    override fun getUiDelegate(action: (M) -> Unit): UiDelegate<M> {
        return UiDelegate(this, action)
    }

    /**
     * 如果{@link #getContentViewRes()} 返回0,则取此方法的返回值作为ContentView
     */
    open fun getContentView(): View? = null

    /**
     * 返回ContentView资源id
     */
    abstract fun getContentViewRes(): Int

    /**
     * setContentView设置好后调用, 优先{@link #initData()}
     */
    override fun initView(view: View) {
    }

    /**
     * setContentView设置好后, 晚于{@link #initView(android.View)}调用
     */
    override fun initData() {
    }

    override fun generateViewModel(modelClass: Class<M>): M {
        return modelClass.newInstance()
    }

    override fun viewModel() = viewModel

    override fun getContext(): Context? {
        return this
    }

    override fun onDestroy() {
        super.onDestroy()
        delegate.destroy()
    }

    override fun showDialog() {
        delegate.showDialog()
    }

    override fun dismissDialog() {
        delegate.dismissDialog()
    }

    override fun navigateUp() {
        finish()
    }

    override fun initObserver() {
    }

    fun <T : BaseViewModel<*>> obtainSubViewModel(viewModel: Class<T>): T {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[viewModel].apply {
            lifecycle.addObserver(this)
            mainScope.onCreate()
        }
    }

    override fun refreshConfig() {
        delegate.refreshConfig(this)
    }
}