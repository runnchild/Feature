package com.rongc.demo.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.rongc.demo.ProgressAbility
import com.rongc.demo.adapter.RepoListAdapter
import com.rongc.demo.databinding.FragmentUserBinding
import com.rongc.demo.viewmodel.UserViewModel
import com.rongc.feature.ability.showProgressIfLoading
import com.rongc.feature.ui.BaseFragment

class UserFragment : BaseFragment<FragmentUserBinding, UserViewModel>() {

    private val args by navArgs<UserFragmentArgs>()
    private val progress by lazy { ProgressAbility(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerAbility(progress)
        mBinding.name.text = args.login
        viewModel.login.value = args.login

        val adapter = RepoListAdapter {}
        mBinding.repoList.adapter = adapter

        viewModel.repositories.observe(viewLifecycleOwner) {
            progress.showProgressIfLoading(it)
            adapter.submitList(it.data)
        }
    }
}