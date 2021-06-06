package com.rongc.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ColorUtils
import com.rongc.demo.vo.Banner
import com.rongc.feature.viewmodel.BaseViewModel

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/5/30
 */
class DemoDialogViewModel: BaseViewModel() {
    val banners = MutableLiveData<List<Banner>>()

    fun requestBanners() {
        val list = arrayListOf<Banner>()
        repeat(10) {
            list.add(Banner(ColorUtils.getRandomColor(), "item: $it"))
        }
        banners.value = list
    }
}