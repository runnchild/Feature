package com.rongc.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.rongc.feature.ability.BindingAbility
import com.rongc.feature.ability.IAbility
import com.rongc.feature.ui.host.FragmentHost
import com.rongc.feature.ui.host.Host
import com.rongc.feature.ui.host.IHost
import com.rongc.feature.utils.autoCleared
import com.rongc.feature.viewmodel.BaseViewModel

abstract class BaseFragment<B : ViewBinding, M : BaseViewModel> : Fragment(), IHost<M> {

    private var bindingAbility by autoCleared<BindingAbility<B>>()

    protected val mBinding: B get() = bindingAbility.mBinding!!

    override val viewModel: M by lazy {
        viewModelProvider()
    }

    final override val lifecycleOwner: LifecycleOwner get() = viewLifecycleOwner

    final override val host: Host get() = FragmentHost

    final override val abilities: ArrayList<IAbility> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingAbility = BindingAbility()
        registerAbility(bindingAbility)
        bindingAbility.onCreateImmediately(this, inflater, container)
        return mBinding.root
    }

    final override fun viewModelProvider(): M {
        return super.viewModelProvider()
    }

    final override fun registerAbility(ability: IAbility) {
        super.registerAbility(ability)
        viewLifecycleOwner.lifecycle.addObserver(ability)
    }

    override fun viewModelCreator(cls: Class<M>): () -> M {
        return {
            defaultViewModelProviderFactory.create(cls)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onHostDestroy()
    }
}