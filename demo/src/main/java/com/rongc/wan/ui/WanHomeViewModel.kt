package com.rongc.wan.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.rongc.feature.vo.Resource
import com.rongc.list.viewmodel.BaseListViewModel
import com.rongc.wan.ProjectTree
import com.rongc.wan.service.WanServiceProvider

class WanHomeViewModel : BaseListViewModel<ProjectTree>() {
    private val repository = WanServiceProvider.wanRepository

    /**
     * 正常情况下（非业务需要多次调接口刷新数据）在ViewModel的生命周期内应该只调一次接口，
     * 避免在可能会多次执行的方法内直接获取（或可在请求前判断是否已有数据）接口数据，
     * 比如Fragment的onViewCreated/onCreateView，但可在这些方法内订阅，如有数据将立即得到通知。
     */
    val banners by lazy {
        repository.fetchBanners()
    }

    /**
     * 获取到分类数据后将原始数据变换为需要关注的分类id数组
     * 此处的result为#loadListData的返回结果
     */
    val tabs = result.map {
        it.data?.map { project ->
            project.id.toString()
        }
    }

    override fun loadListData(page: Int): LiveData<Resource<List<ProjectTree>>> {
        return repository.getProjectTree()
    }
}