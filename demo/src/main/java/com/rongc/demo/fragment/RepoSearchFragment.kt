package com.rongc.demo.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.rongc.demo.ProgressAbility
import com.rongc.demo.adapter.RepoListAdapter
import com.rongc.demo.api.RepoServiceProvider
import com.rongc.demo.databinding.FragmentListBinding
import com.rongc.demo.viewmodel.RepoSearchViewModel
import com.rongc.demo.vo.Repo
import com.rongc.feature.ability.IListAbility
import com.rongc.feature.ability.ListAbility
import com.rongc.feature.binding.doOnDefaultAdapter
import com.rongc.feature.refresh.ItemDecoration
import com.rongc.feature.ui.BaseFragment
import com.rongc.feature.utils.idp
import com.rongc.feature.utils.logd
import com.rongc.feature.viewmodel.EmptyBuilder
import com.rongc.feature.widget.IEmptyView

class RepoSearchFragment : BaseFragment<FragmentListBinding, RepoSearchViewModel>(), IListAbility {

    override val recyclerView: RecyclerView
        get() = mBinding.refreshGroup.recyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerAbility(ProgressAbility(requireContext()))
        registerAbility(ListAbility(this, this))

        recyclerView.doOnDefaultAdapter<Repo> {
            // 有设置adapter时立即回调
            it.logd()
        }
        
        viewModel.result.observe(viewLifecycleOwner) {
//            showProgressIfLoading(it)
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
        // default is LinearLayoutManager
        return super.providerLayoutManager(context)
    }

    override fun decorationBuilder(): ItemDecoration.Builder.() -> Unit {
        return {
            setVerticalLineWidth(1.idp)
            setLineColor(Color.GRAY)
        }
    }

    override fun providerEmptyView(context: Context): IEmptyView? {
        // default is EmptyView
        return super.providerEmptyView(context)
    }

    override fun setupEmptyView(state: Int): EmptyBuilder.() -> Unit {
        return {
            iconDrawable = Color.BLUE.toDrawable()

            tip {
                text = "no data found"
            }
            subTip {
                text = "try again"
            }
            refreshBtn {
                text = "retry"
                isVisible = true
            }
        }
    }
}