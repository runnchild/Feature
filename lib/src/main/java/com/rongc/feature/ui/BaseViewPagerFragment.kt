package com.rongc.feature.ui

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/21
 * @since 2.1.4
 */
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.databinding.BaseViewpagerWithRefreshBinding
import com.rongc.feature.model.BaseModel
import com.rongc.feature.viewmodel.BaseListViewModel

abstract class BaseViewPagerFragment<M : BaseListViewModel<*, out BaseModel>> :
    BaseBindingFragment<BaseViewpagerWithRefreshBinding, M>() {

    override fun binding(inflater: LayoutInflater, container: ViewGroup?) =
        BaseViewpagerWithRefreshBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.autoRefresh = autoRefresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.baseViewPager.adapter = obtainAdapter()
    }

    open fun obtainAdapter(): RecyclerView.Adapter<*>? = null

    open fun autoRefresh() = true
}