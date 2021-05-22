package com.runnchild.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding> : Fragment(), IUI<B> {

    protected lateinit var mBinding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = binding(inflater, container)
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = this
        }
        mBinding = binding
        return mBinding.root
    }

    override fun onDestroyView() {
//        mBinding.unbind()
        super.onDestroyView()
    }
}