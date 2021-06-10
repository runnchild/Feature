package com.rongc.demo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.rongc.demo.databinding.DialogDemoFragmentBinding
import com.rongc.demo.viewmodel.DemoDialogViewModel
import com.rongc.feature.ui.BaseDialogFragment
import com.rongc.list.ability.IPagerList
import com.rongc.list.ability.PagerAbility

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/5/30
 */
class PagerDialogFragment : BaseDialogFragment<DialogDemoFragmentBinding, DemoDialogViewModel>(),
    IPagerList {

    override val viewPager: ViewPager2 get() = mBinding.viewPager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.requestBanners()
        registerAbility(PagerAbility(this))
    }
}