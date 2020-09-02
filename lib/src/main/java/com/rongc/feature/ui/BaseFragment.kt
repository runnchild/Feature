package com.rongc.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rongc.feature.model.BaseModel
import com.rongc.feature.viewmodel.BaseViewModel

/**
 * Fragment的基类
 */
abstract class BaseFragment<M : BaseViewModel<out BaseModel>> : Fragment(), IUI<M> {

    protected lateinit var mView: View
    protected lateinit var viewModel: M
    private lateinit var delegate: UiDelegate<M>

    private val refreshDelegate by lazy {
        this as? IRefreshDelegate
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return if (::mView.isInitialized) {
            mView
        } else {
            delegate = getUiDelegate {
                viewModel = it
            }
            mView = inflate(inflater, container)

            refreshDelegate?.run {
                init(viewModel, this@BaseFragment, mView)
            }

            delegate.initToolBar(this, mView)
            delegate.init(this, mView)

            mView
        }
    }

    override fun getUiDelegate(action: (M) -> Unit): UiDelegate<M> {
        return UiDelegate(this, action)
    }

    override fun generateViewModel(modelClass: Class<M>): M {
        return modelClass.newInstance()
    }

    /**
     * inflate view后调用，先于{@link #initData()}
     */
    override fun initView(view: View) {
    }

    override fun initData() {
    }

    abstract fun inflate(inflater: LayoutInflater, container: ViewGroup?): View

    override fun viewModel() = viewModel

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

    /**
     * 退出页面，由页面决定弹出Fragment Stack还出关闭附属的Activity
     */
    override fun navigateUp() {
    }

    override fun refreshConfig() {
        delegate.refreshConfig(requireActivity())
    }
}