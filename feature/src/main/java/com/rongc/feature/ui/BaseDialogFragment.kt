package com.rongc.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rongc.feature.ability.IAbility
import com.rongc.feature.ability.impl.BindingAbility
import com.rongc.feature.ui.host.DialogFragmentHost
import com.rongc.feature.ui.host.IAbilityList
import com.rongc.feature.ui.host.IViewModelProvider
import com.rongc.feature.viewmodel.BaseViewModel

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/5/30
 */
abstract class BaseDialogFragment<B : ViewBinding, M : BaseViewModel> : BottomSheetDialogFragment(),
    IViewModelProvider<M>, IAbilityList {

    private lateinit var bindingAbility :BindingAbility<B>
    protected val mBinding: B get() = bindingAbility.mBinding!!

    override val abilities = ArrayList<IAbility>()
    override val host = DialogFragmentHost
    override val viewModel: M by lazy { viewModelProvider() }
    override val lifecycleOwner: LifecycleOwner get() = viewLifecycleOwner

    override fun viewModelCreator(cls: Class<M>): M {
        return defaultViewModelProviderFactory.create(cls)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        bindingAbility = BindingAbility(viewModel)
        registerAbility(bindingAbility)
        bindingAbility.onCreateImmediately(this, inflater, container)

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        return mBinding.root
    }

    final override fun viewModelProvider(): M {
        return super.viewModelProvider()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onHostDestroy()
    }
}