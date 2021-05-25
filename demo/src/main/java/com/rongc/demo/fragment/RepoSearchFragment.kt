package com.rongc.demo.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.rongc.demo.ProgressAbility
import com.rongc.demo.adapter.RepoListAdapter
import com.rongc.demo.api.RepoServiceProvider
import com.rongc.demo.databinding.FragmentListBinding
import com.rongc.demo.viewmodel.RepoSearchViewModel
import com.rongc.feature.ability.IListAbility
import com.rongc.feature.ability.ListAbility
import com.rongc.feature.ability.observeResource
import com.rongc.feature.ui.BaseFragment
import com.rongc.feature.ui.kt.showProgressIfLoading

class RepoSearchFragment : BaseFragment<FragmentListBinding, RepoSearchViewModel>(), IListAbility {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerAbility(ProgressAbility(requireContext()))
        registerAbility(ListAbility(this, mBinding.recyclerView.baseRecyclerView))

        observeResource(viewModel)
        
        viewModel.result.observe(viewLifecycleOwner) {
            showProgressIfLoading(it)
            KeyboardUtils.hideSoftInput(mBinding.edtQuery)
        }

        mBinding.btnQuery.setOnClickListener {
            viewModel.setQuery(mBinding.edtQuery.text.toString())
        }
    }

    /**
     * 以自己的方式创建ViewModel
     */
    override fun viewModelCreator(cls: Class<RepoSearchViewModel>): () -> RepoSearchViewModel {
        return {
            RepoSearchViewModel(RepoServiceProvider.repoRepository)
        }
    }

    override fun providerAdapter(): RecyclerView.Adapter<*> {
        return RepoListAdapter {
            findNavController().navigate(RepoSearchFragmentDirections.showUser(it.owner.login, it.owner.url))
        }
    }

    override fun providerLayoutManager(context: Context): RecyclerView.LayoutManager {
        return super.providerLayoutManager(context)
    }
}