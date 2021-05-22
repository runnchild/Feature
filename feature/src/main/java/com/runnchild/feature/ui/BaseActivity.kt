package com.runnchild.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.runnchild.feature.ability.AbstractAbility

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity(), IUI<B> {

    protected lateinit var mBinding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = binding(LayoutInflater.from(this), null)
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = this
        }
        mBinding = binding
        setContentView(binding.root)
    }

    fun registerAbility(ability: AbstractAbility) {
        lifecycle.addObserver(ability)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}