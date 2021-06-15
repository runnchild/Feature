package com.rongc.demo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ColorUtils
import com.rongc.demo.vo.Banner
import com.rongc.feature.vo.Resource
import com.rongc.list.viewmodel.BaseListViewModel

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/5/30
 */
class DemoDialogViewModel: BaseListViewModel<String>() {
    val banners = MutableLiveData<List<Banner>>()

    fun requestBanners() {
        val list = arrayListOf<Banner>()
        repeat(10) {
            list.add(Banner(ColorUtils.getRandomColor(), "item: $it"))
        }
        banners.value = list
    }

    override fun loadListData(page: Int): LiveData<Resource<List<String>>> {
        return MutableLiveData(Resource.success(arrayListOf("")))
    }
}