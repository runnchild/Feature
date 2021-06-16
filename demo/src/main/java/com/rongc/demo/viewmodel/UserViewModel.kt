package com.rongc.demo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.rongc.demo.api.RepoServiceProvider
import com.rongc.demo.vo.Owner
import com.rongc.feature.utils.AbsentLiveData
import com.rongc.feature.vo.Resource
import com.rongc.list.viewmodel.BaseListViewModel

class UserViewModel : BaseListViewModel<Any>() {
    var avatar: String? = null
    val login = MutableLiveData<String>()
    private val repository = RepoServiceProvider.repoRepository

    init {
        // 关闭下拉刷新和加载更多功能
        enableRefresh.set(false)
        enableLoadMore.set(false)
    }

    override fun loadListData(page: Int): LiveData<Resource<List<Any>>> {
        val it = login.value
        return if (it.isNullOrEmpty()) {
            AbsentLiveData.create()
        } else {
            repository.getRepos(it).map {
                // 转换成Any类型List并添加其他类型元素
                val list = arrayListOf<Any>()
                list.add(Owner(login.value, avatar))
                list.addAll(it.data ?: arrayListOf())
                Resource(it.status, list, it.error)
            }
        }
    }
}