package com.rongc.demo.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.rongc.demo.ProgressAbility
import com.rongc.demo.viewmodel.UserViewModel
import com.rongc.feature.ability.IListAbility
import com.rongc.feature.ability.ListAbility
import com.rongc.feature.databinding.BaseRecyclerWithRefreshBinding
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.refresh.ItemDecoration
import com.rongc.feature.ui.BaseFragment
import com.rongc.feature.ui.kt.showProgressIfLoading
import com.rongc.feature.utils.idp

class UserFragment : BaseFragment<BaseRecyclerWithRefreshBinding, UserViewModel>(), IListAbility {

    private val args by navArgs<UserFragmentArgs>()

    override val recyclerView: RecyclerView
        get() = mBinding.recyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerAbility(ProgressAbility(requireContext()))
        registerAbility(ListAbility(this, this))

        viewModel.result.observe(viewLifecycleOwner) {
            showProgressIfLoading(it)
        }
    }

    override fun autoRefresh(): Boolean {
        viewModel.login.value = args.login
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