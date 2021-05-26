package com.rongc.demo.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.navigation.fragment.findNavController
import com.rongc.demo.R
import com.rongc.demo.databinding.FragmentHomeBinding
import com.rongc.demo.viewmodel.HomeViewModel
import com.rongc.feature.ui.BaseFragment
import com.rongc.feature.utils.autoCleared
import com.rongc.feature.utils.logd

class Home : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    var autoField by autoCleared<String>()

    private val que = MutableLiveData<Int>()

    private val result = que.switchMap {
        "home result = $it".logd()
        liveData {
            kotlinx.coroutines.delay(3000)
            emit("$it")
        }

//        MutableLiveData<String>()
    }

    private val status = result.map {
        "home status = $it".logd()
        it.toFloat()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autoField = "try to create view, times = ${viewModel.count + 1}"

        mBinding.tvField.text = autoField
        viewModel.setCount(viewModel.count + 1)

        mBinding.btnList.setOnClickListener {
            val toInt = Math.random().toInt()
            "home que = $toInt".logd()
            que.value = toInt

            findNavController().navigate(R.id.list)
        }
        result.observe(viewLifecycleOwner) {
            "result observe: $it".logd()
        }
        status.observe(viewLifecycleOwner) {
            "status observe: $it".logd()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // autoField.length
        // accessing autoField while the fragment`s view is destroyed will be throw exception
    }
}