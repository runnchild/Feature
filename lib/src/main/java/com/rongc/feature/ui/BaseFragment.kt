package com.rongc.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rongc.feature.model.BaseModel
import com.rongc.feature.ui.toolbar.PsnToolbar
import com.rongc.feature.utils.Compat.removeFromParent
import com.rongc.feature.viewmodel.BaseViewModel
import com.rongc.feature.viewmodel.ToolBarViewModel

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate = getUiDelegate {
            viewModel = it
        }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflate(inflater, container)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return if (::mView.isInitialized) {
            mView.removeFromParent()
            mView
        } else {
            delegate = getUiDelegate {
                viewModel = it
            }
            val view = inflate(inflater, container)

            refreshDelegate?.run {
                init(viewModel, this@BaseFragment, view)
            }

            mView = if (delegate.barConfig.toolbarVisible) {
                LinearLayoutCompat(view.context).apply {
                    orientation = LinearLayoutCompat.VERTICAL
                    addView(
                        PsnToolbar(context),
                        ViewGroup.LayoutParams(-1, -2)
                    )
                    addView(view, LinearLayoutCompat.LayoutParams(-1, 0, 1f))
                }
            } else {
                view
            }

            delegate.findToolBar(mView)?.run {
                val toolbarViewModel by viewModels<ToolBarViewModel>()
                viewModel.toolbarModel = toolbarViewModel
                setViewModel(toolbarViewModel)
                viewModel.toolbarModel?.backLiveData?.observe(this@BaseFragment, {
                    onBackPressed()
                })
            }

            refreshConfig()

            delegate.init(this, mView)

            mView
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        refreshDelegate?.run {
//            init(viewModel, this@BaseFragment, mView)
//        }
//
//        val toolbarViewModel by activityViewModels<ToolBarViewModel>()
//        viewModel.toolbarModel = toolbarViewModel
//        refreshConfig()
//
//        delegate.init(this, mView)
//    }

    override fun getUiDelegate(action: (M) -> Unit): UiDelegate<M> {
        return UiDelegate(this, action)
    }

    override fun generateViewModel(modelClass: Class<M>): M {
        return modelClass.newInstance()
    }

    fun <T : ViewModel> obtainSubViewModel(viewModel: Class<T>): T {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[viewModel].apply {
            @Suppress("UNCHECKED_CAST")
            delegate.initObserver(this@BaseFragment, this as M)
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        mView.removeFromParent()
    }

    /**
     * 退出页面，由页面决定弹出Fragment Stack还出关闭附属的Activity
     */
    override fun onBackPressed() {
        dismissDialog()
    }

    override fun finish() {
        dismissDialog()
        requireActivity().finish()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            refreshConfig()
        }
    }

    override fun refreshConfig() {
        delegate.refreshConfig(activity ?: return)
    }
}