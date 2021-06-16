package com.rongc.wan.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.switchMap
import com.rongc.feature.vo.Resource
import com.rongc.list.viewmodel.BaseListViewModel
import com.rongc.wan.ProjectTree
import com.rongc.wan.service.WanServiceProvider

class WanHomeViewModel : BaseListViewModel<ProjectTree>() {
    private val repository = WanServiceProvider.wanRepository

    private val _banners = MediatorLiveData<Int>()

    val banners = _banners.switchMap {
        repository.fetchBanners()
    }

    init {
        _banners.value = 1
    }

    override fun loadListData(page: Int): LiveData<Resource<List<ProjectTree>>> {
        return repository.getProjectTree()
    }

}