package com.rongc.wan.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.rongc.feature.vo.Resource
import com.rongc.list.viewmodel.BaseListViewModel
import com.rongc.wan.ProjectList
import com.rongc.wan.service.WanServiceProvider

class ProjectListViewModel : BaseListViewModel<ProjectList>() {

    val cid = MutableLiveData<String>()

    private val repository = WanServiceProvider.wanRepository

    override fun loadListData(page: Int): LiveData<Resource<List<ProjectList>>> {
        return repository.getProjectList(page, cid.value!!).map {
            Resource(it.status, it.data?.datas, it.error)
        }
    }
}
