package com.rongc.demo.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.rongc.demo.R
import com.rongc.demo.databinding.FragmentHomeBinding
import com.rongc.demo.viewmodel.HomeViewModel
import com.rongc.feature.ui.BaseFragment
import com.rongc.feature.utils.autoCleared

class Home : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    var autoField by autoCleared<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autoField = "try to create view, times = ${viewModel.count + 1}"

        mBinding.tvField.text = autoField
        viewModel.setCount(viewModel.count + 1)

        mBinding.btnList.setOnClickListener {
            findNavController().navigate(R.id.list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // autoField.length
        // accessing autoField while the fragment`s view is destroyed will be throw exception
    }
}