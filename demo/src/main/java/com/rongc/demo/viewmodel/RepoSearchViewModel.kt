package com.rongc.demo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rongc.demo.repository.RepoRepository
import com.rongc.demo.vo.Repo
import com.rongc.feature.utils.AbsentLiveData
import com.rongc.feature.viewmodel.BaseListViewModel
import com.rongc.feature.vo.Resource

class RepoSearchViewModel(private val repository: RepoRepository) : BaseListViewModel<Repo>() {

    val query = MutableLiveData<String>()

    init {
        query.value = "runnchild/Navigation"
    }

    fun setQuery(queryInput: String) {
        if (queryInput.trim() == query.value) {
            return
        }
        query.value = queryInput
        refresh()
    }

    override fun loadListData(page: Int): LiveData<Resource<List<Repo>>> {
        val it = query.value
        return  if (it.isNullOrEmpty()) {
            AbsentLiveData.create()
        } else {
            repository.searchRepo(it, page)
        }
    }
}