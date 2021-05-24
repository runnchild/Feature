package com.rongc.demo.fragment

import android.os.Bundle
import android.view.View
import com.rongc.demo.databinding.FragmentHomeBinding
import com.rongc.demo.viewmodel.HomeViewModel
import com.rongc.feature.ui.fragment.BaseFragment
import com.rongc.feature.utils.autoCleared

class Home : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    var autoField by autoCleared<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autoField = "try to rotate screen, times = ${viewModel.count}"

        mBinding.tvField.text = autoField
        viewModel.setCount(viewModel.count + 1)

        mBinding.btnList.setOnClickListener {

        }
    }

    /**
     * 以自己的方式创建ViewModel
     */
//    override fun viewModelCreator(cls: Class<HomeViewModel>): () -> HomeViewModel {
//        return {
//            HomeViewModel(...)
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        // autoField.length
        // accessing autoField while the fragment`s view is destroyed will be throw exception
    }
}