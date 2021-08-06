package com.rongc.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.rongc.feature.ability.IAbility
import com.rongc.feature.ability.impl.AbsProgressAbility
import com.rongc.feature.ability.impl.BindingAbility
import com.rongc.feature.ability.impl.ViewTreeProgressAbilityOwner.set
import com.rongc.feature.ui.host.FragmentHost
import com.rongc.feature.ui.host.Host
import com.rongc.feature.ui.host.IAbilityList
import com.rongc.feature.ui.host.IViewModelProvider
import com.rongc.feature.viewmodel.BaseViewModel

abstract class BaseFragment<B : ViewBinding, M : BaseViewModel> : Fragment(),
    IViewModelProvider<M>, IAbilityList {

    private lateinit var bindingAbility :BindingAbility<B>

    protected val mBinding: B get() = bindingAbility.mBinding!!

    override val viewModel: M by lazy {
        viewModelProvider()
    }

    final override val lifecycleOwner: LifecycleOwner get() = viewLifecycleOwner

    final override val host: Host get() = FragmentHost()

    final override val abilities: ArrayList<IAbility> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        bindingAbility = BindingAbility(viewModel)
        registerAbility(bindingAbility)
        bindingAbility.onCreateImmediately(this, inflater, container)
        return bindingAbility.mBinding?.root
    }

//    final override fun viewModelProvider(): M {
//        return super.viewModelProvider()
//    }

    override fun viewModelCreator(cls: Class<M>):  M {
        return defaultViewModelProviderFactory.create(cls)
    }

    override fun registerAbility(ability: IAbility) {
        super.registerAbility(ability)
        if (ability is AbsProgressAbility) {
            ability.set(requireView())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onHostDestroy()
    }
}