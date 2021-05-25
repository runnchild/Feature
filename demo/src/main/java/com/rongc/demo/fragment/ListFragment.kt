package com.rongc.demo.fragment

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.KeyboardUtils
import com.rongc.demo.ProgressAbility
import com.rongc.demo.adapter.RepoListAdapter
import com.rongc.demo.databinding.FragmentListBinding
import com.rongc.demo.repository.RepoRepository
import com.rongc.demo.viewmodel.ListViewModel
import com.rongc.feature.ability.showIfLoading
import com.rongc.feature.ui.fragment.BaseFragment

class ListFragment : BaseFragment<FragmentListBinding, ListViewModel>() {

    private val progress by lazy { ProgressAbility(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerAbility(progress)

        val adapter = RepoListAdapter()
        mBinding.recyclerView.adapter = adapter

        viewModel.result.observe(viewLifecycleOwner) {
            progress.showIfLoading(it)

            if (it?.data != null) {
                adapter.submitList(it.data)
            }

            KeyboardUtils.hideSoftInput(mBinding.edtQuery)
        }

        mBinding.btnQuery.setOnClickListener {
            viewModel.setQuery(mBinding.edtQuery.text.toString())
        }
    }

    /**
     * 以自己的方式创建ViewModel
     */
    override fun viewModelCreator(cls: Class<ListViewModel>): () -> ListViewModel {
        return {
            ListViewModel(RepoRepository())
        }
    }
}