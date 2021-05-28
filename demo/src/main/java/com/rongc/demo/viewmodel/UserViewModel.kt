package com.rongc.demo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.rongc.demo.api.RepoServiceProvider
import com.rongc.feature.utils.AbsentLiveData
import com.rongc.feature.viewmodel.BaseListViewModel
import com.rongc.feature.vo.Resource

class UserViewModel : BaseListViewModel<Any>() {
    val login = MutableLiveData<String>()
    private val repository = RepoServiceProvider.repoRepository

    override fun loadListData(page: Int): LiveData<Resource<List<Any>>> {
        val it = login.value
        return if (it.isNullOrEmpty()) {
            AbsentLiveData.create()
        } else {
            repository.getRepos(it).map {
                val list = arrayListOf<Any>()
                list.add(login.value!!)
                list.addAll(it.data ?: arrayListOf())
                Resource(it.status, list, it.message)
            }
        }
    }
}