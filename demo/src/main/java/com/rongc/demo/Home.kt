package com.rongc.demo

import android.os.Bundle
import android.view.View
import com.rongc.demo.databinding.FragmentHomeBinding
import com.rongc.feature.ui.fragment.BaseFragment
import com.rongc.feature.utils.autoCleared

class Home : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    var autoField by autoCleared<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autoField = "345"
    }

    override fun viewModelCreator(cls: Class<HomeViewModel>): () -> HomeViewModel {
        return {
            HomeViewModel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // autoField.length
        // accessing autoField while the fragment`s view is destroyed will be throw exception
    }
}