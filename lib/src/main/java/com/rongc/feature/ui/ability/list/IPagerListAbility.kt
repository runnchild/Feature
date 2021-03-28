package com.rongc.feature.ui.ability.list

import androidx.viewpager2.widget.ViewPager2

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/28
 * @since 2.1.4
 */
interface IPagerListAbility: IListAbility {
    fun returnPagerView(): ViewPager2? = null
}