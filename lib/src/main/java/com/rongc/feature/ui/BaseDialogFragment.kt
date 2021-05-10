package com.rongc.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rongc.feature.BR
import com.rongc.feature.model.BaseModel
import com.rongc.feature.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/1/1
 * @since 2.1.4
 */
abstract class BaseDialogFragment<B : ViewDataBinding, M : BaseViewModel<out BaseModel>> : IUI<M>,
    BottomSheetDialogFragment() {

    protected lateinit var binding: B
    protected lateinit var viewModel: M
    private lateinit var delegate: UiDelegate<M>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate = getUiDelegate {
            viewModel = it
        }
        viewModel.onCreate()
        lifecycle.addObserver(viewModel)
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflate(inflater, container)
    }

    private fun inflate(inflater: LayoutInflater, container: ViewGroup?): View {
        return binding(inflater, container).apply {
            binding = this
            binding.lifecycleOwner = this@BaseDialogFragment

            binding.setVariable(BR.viewModel, viewModel)
            try {
                // 如果xml中没定义mUi
                this::class.java.superclass?.getDeclaredField("mUi")
                setVariable(BR.ui, this@BaseDialogFragment)
            } catch (e: Exception) {
            }
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        delegate.initObserver(this, viewModel)
        initObserver()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackPressed()
        }
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    open fun binding(inflater: LayoutInflater, container: ViewGroup?): B {
        val bindingClass =
            (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        val method = bindingClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return (method.invoke(null, inflater, container, false) as B)
    }

    override fun dismissDialog() {
        delegate.dismissDialog()
    }

    override fun finish() {
        onBackPressed()
    }

    override fun generateViewModel(modelClass: Class<M>): M {
        return modelClass.newInstance()
    }

    override fun getUiDelegate(action: (M) -> Unit): UiDelegate<M> {
        return UiDelegate(this, action)
    }

    override fun initData() {
    }

    override fun initObserver() {
    }

    override fun initView(view: View) {
    }

    override fun onBackPressed() {
    }

    override fun refreshConfig() {
        delegate.refreshConfig(requireActivity())
    }

    override fun showDialog() {
        delegate.showDialog()
    }

    override fun viewModel(): M {
        return viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        delegate.destroy()
    }
}