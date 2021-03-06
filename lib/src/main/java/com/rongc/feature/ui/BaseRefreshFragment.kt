package com.rongc.feature.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.databinding.BaseRefreshLayoutBinding
import com.rongc.feature.model.BaseModel
import com.rongc.feature.viewmodel.BaseViewModel

open class BaseRefreshFragment<M : BaseViewModel<out BaseModel>> :
    BaseBindingFragment<BaseRefreshLayoutBinding, M>(), IRefreshDelegate {

    lateinit var mRecyclerView: RecyclerView

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): BaseRefreshLayoutBinding {
        return BaseRefreshLayoutBinding.inflate(inflater).apply {
            mRecyclerView = includeRecycler.baseRecyclerView
        }
    }

}