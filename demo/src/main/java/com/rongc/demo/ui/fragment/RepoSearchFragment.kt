package com.rongc.demo.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.KeyboardUtils
import com.rongc.demo.ProgressAbility
import com.rongc.demo.R
import com.rongc.demo.adapter.RepoListAdapter
import com.rongc.demo.api.RepoServiceProvider
import com.rongc.demo.databinding.FragmentListBinding
import com.rongc.demo.viewmodel.RepoSearchViewModel
import com.rongc.demo.vo.Repo
import com.rongc.feature.ui.BaseFragment
import com.rongc.feature.utils.idp
import com.rongc.feature.utils.logd
import com.rongc.list.ItemDecoration
import com.rongc.list.ability.IRecyclerHost
import com.rongc.list.ability.ListAbility
import com.rongc.list.binding.doOnDefaultAdapter
import com.rongc.list.viewmodel.EmptyBuilder
import com.rongc.list.viewmodel.whenDataIsEmpty
import com.rongc.list.widget.IEmptyView

class RepoSearchFragment : BaseFragment<FragmentListBinding, RepoSearchViewModel>(),
    IRecyclerHost {

    override val recyclerView: RecyclerView
        get() = mBinding.refreshGroup.recyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerAbility(ProgressAbility(requireContext()))
        registerAbility(ListAbility(viewModel, this))

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
            findNavController().navigate(
                RepoSearchFragmentDirections.showUser(
                    it.owner.login,
                    it.owner.url
                )
            )
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

    override fun setupEmptyView(builder: EmptyBuilder) {
        // 只设置空数据，其他情况使用默认配置
        builder.whenDataIsEmpty {
            icon {
                setImageResource(R.mipmap.ic_launcher)
            }
            //or iconDrawable = Color.GREEN.toDrawable()
            tip {
                text = "no repository found"
            }
            //or  tip = "no data found"
            subTip {
                text = "try again"
            }
            //or subTip = "try again"
            refreshBtn {
                text = "retry"
            }
            //or btnText = "retry"
        }
    }
}