package com.rongc.feature.app.ui

import androidx.viewpager2.widget.ViewPager2
import com.rongc.feature.app.ui.viewmodel.ViewPagerViewModel
import com.rongc.feature.databinding.BaseViewpagerWithRefreshBinding
import com.rongc.feature.ui.BaseBindingActivity
import com.rongc.feature.ui.ability.IAbility
import com.rongc.feature.ui.ability.IPagerItem
import com.rongc.feature.ui.ability.list.IPagerListAbility
import com.rongc.feature.ui.ability.list.PagerListAbility
import com.rongc.feature.ui.adapter.BaseViewPagerAdapter
import com.rongc.feature.ui.toolbar.BarConfig
import com.rongc.feature.utils.idp
import com.rongc.feature.utils.singleClick
import com.rongc.feature.viewmodel.EmptyBuilder
import com.rongc.feature.widget.ItemDecoration

class ViewPager2Activity :
    BaseBindingActivity<BaseViewpagerWithRefreshBinding, ViewPagerViewModel>(),
    IPagerListAbility by PagerListAbility() {

    override fun obtainAbility(abilities: ArrayList<IAbility>) {
//        abilities.add(PagerListAbility())
    }

    override fun returnPagerView(): ViewPager2 {
        return binding.baseViewPager
    }

    override fun providerAdapter() = object : BaseViewPagerAdapter<String>(this) {
        override fun createItemFragment(position: Int): IPagerItem<String> {
            return EmptyViewPagerFragmentItem()
        }
    }

//    override fun registerItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
//        binders.add(PagerSimpleBinder())
//    }

    override fun decorationBuilder(): ItemDecoration.Builder.() -> Unit {
        return {
            setHorizontalLineWidth(10.idp)
            setHorizontalStartWidth(15.idp)
        }
    }

    override fun getBarConfig(): BarConfig.() -> Unit {
        return {
            menu {
                text = "clear"
                singleClick {
                    viewModel.clearData()
                }
            }
        }
    }

    override fun autoRefresh(): Boolean {
        return true
    }

    override fun setupEmptyView(state: Int): EmptyBuilder.() -> Unit {
        return {
            tip = "empty pager"
            subTip = "pull to refresh"
        }
    }
}