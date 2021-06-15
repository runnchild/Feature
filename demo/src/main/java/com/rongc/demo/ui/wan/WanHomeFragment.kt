package com.rongc.demo.ui.wan

import android.os.Bundle
import android.view.View
import com.rongc.demo.databinding.FragmentWanHomeBinding
import com.rongc.feature.ui.BaseFragment
import com.rongc.list.binding.items

class WanHomeFragment : BaseFragment<FragmentWanHomeBinding, WanHomeViewModel>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.fetchBanners().observe(lifecycleOwner) {
            mBinding.viewPager.items(it.data)
        }
    }
}