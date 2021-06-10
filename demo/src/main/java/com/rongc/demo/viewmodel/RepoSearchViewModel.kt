package com.rongc.demo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.rongc.demo.repository.RepoRepository
import com.rongc.demo.vo.Repo
import com.rongc.feature.utils.AbsentLiveData
import com.rongc.feature.vo.Resource
import com.rongc.list.viewmodel.BaseListViewModel

class RepoSearchViewModel(private val repository: RepoRepository) : BaseListViewModel<Repo>() {

    private val _query = MutableLiveData<String>()

    val query = _query.map {
        refresh()
        it
    }

    init {
        this._query.value = "runnchild/Navigation"
    }

    fun setQuery(queryInput: String) {
        if (queryInput.trim() == this._query.value) {
            return
        }
        this._query.value = queryInput
    }

    override fun loadListData(page: Int): LiveData<Resource<List<Repo>>> {
        val it = this._query.value
        return  if (it.isNullOrEmpty()) {
            AbsentLiveData.create()
        } else {
            repository.searchRepo(it, page)
        }
    }
}