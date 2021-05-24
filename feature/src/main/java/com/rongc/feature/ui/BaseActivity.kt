package com.rongc.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.rongc.feature.ability.BindingAbility
import com.rongc.feature.ability.IAbility
import com.rongc.feature.utils.autoCleared
import com.rongc.feature.viewmodel.BaseViewModel

abstract class BaseActivity<B : ViewBinding, M : BaseViewModel> : AppCompatActivity(), IHost<M> {

    private var bindingAbility by autoCleared<BindingAbility<B>>()

    protected val mBinding: B get() = bindingAbility.mBinding!!

    override val viewModel by lazy {
        viewModelProvider()
    }

    override val lifecycleOwner: LifecycleOwner get() = this

    override val host: Host get() = ActivityHost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingAbility = BindingAbility()
        registerAbility(bindingAbility)
        bindingAbility.onCreateImmediately(this, LayoutInflater.from(this))

        setContentView(mBinding.root)
    }

    override fun registerAbility(ability: IAbility) {
        lifecycle.addObserver(ability)
    }

    final override fun viewModelProvider(): M {
        return super.viewModelProvider()
    }

    override fun viewModelCreator(cls: Class<M>): () -> M {
        return {
            defaultViewModelProviderFactory.create(cls)
        }
    }
}