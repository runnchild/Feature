package com.rongc.demo.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.rongc.demo.ProgressAbility
import com.rongc.demo.R
import com.rongc.demo.ui.binders.UserItemBinder
import com.rongc.demo.ui.binders.UserRepoItemBinder
import com.rongc.demo.viewmodel.UserViewModel
import com.rongc.feature.ability.IRecyclerList
import com.rongc.feature.ability.impl.ListAbility
import com.rongc.feature.ability.impl.ToolbarAbility
import com.rongc.feature.ability.impl.showProgressIfLoading
import com.rongc.feature.databinding.BaseRecyclerWithRefreshBinding
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.refresh.ItemDecoration
import com.rongc.feature.ui.BaseFragment
import com.rongc.feature.utils.idp

class UserFragment : BaseFragment<BaseRecyclerWithRefreshBinding, UserViewModel>(), IRecyclerList {

    private val args by navArgs<UserFragmentArgs>()

    override val recyclerView: RecyclerView
        get() = mBinding.recyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerAbility(ProgressAbility(requireContext()))
        registerAbility(ListAbility(this, this))
        registerAbility(ToolbarAbility(this) {
            title = "UserRepository"
            menu {
                text = "more"
                setOnClickListener {
                    findNavController().navigate(R.id.demo_dialog)
//                    viewModel.result.value.data
                }
            }
        })

        viewModel.result.observe(viewLifecycleOwner) {
            showProgressIfLoading(it)
        }
    }

    override fun autoRefresh(): Boolean {
        viewModel.login.value = args.login
        viewModel.avatar = args.avatarUrl
        return super.autoRefresh()
    }

    override fun registerItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
        binders.add(UserRepoItemBinder())
        binders.add(UserItemBinder())
    }

    override fun decorationBuilder(): ItemDecoration.Builder.() -> Unit {
        return {
            setVerticalLineWidth(1.idp)
            setLineColor(Color.LTGRAY)
        }
    }
}