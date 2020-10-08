package com.rongc.feature.ui

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.KeyboardUtils
import com.rongc.feature.model.BaseModel
import com.rongc.feature.ui.toolbar.PsnToolbar
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.feature.viewmodel.ToolBarViewModel

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
        if (!::delegate.isInitialized) {
            delegate = getUiDelegate {
                viewModel = it
            }
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

        if (savedInstanceState == null) {
            findToolBar(view)?.let {
                val toolBarViewModel by viewModels<ToolBarViewModel>()
                viewModel.toolbarModel = toolBarViewModel
                it.setViewModel(toolBarViewModel)
            }
            delegate.init(this, view)
        }

        refreshConfig()

        viewModel.toolbarModel?.title?.set(title)
        viewModel.toolbarModel?.backLiveData?.observe(this, Observer {
            onBackPressed()
        })

        refreshDelegate?.run {
            init(viewModel, this@BaseActivity, view)
        }
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

    override fun initObserver() {
    }

    fun <T : ViewModel> obtainSubViewModel(viewModel: Class<T>): T {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[viewModel].apply {
            @Suppress("UNCHECKED_CAST")
            delegate.initObserver(this@BaseActivity, this as M)
        }
    }

    override fun refreshConfig() {
        delegate.refreshConfig(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent):Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                KeyboardUtils.hideSoftInput(this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // Return whether touch the view.
    private fun isShouldHideKeyboard(v:View?, event:MotionEvent):Boolean {
        if ((v is EditText)) {
            val l = IntArray(2)
            v.getLocationOnScreen(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.rawX > left && event.rawX < right
                    && event.rawY > top && event.rawY < bottom)
        }
        return false
    }
}