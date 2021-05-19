package com.rongc.feature.app.ui

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.rongc.feature.app.ui.binders.MainItemHolder
import com.rongc.feature.app.ui.binders.MainOtherItemHolder
import com.rongc.feature.app.ui.viewmodel.RefreshListViewModel
import com.rongc.feature.databinding.BaseRefreshListLayoutBinding
import com.rongc.feature.refresh.BaseRecyclerItemBinder
import com.rongc.feature.ui.BaseBindingActivity
import com.rongc.feature.ui.ability.list.IRecyclerListAbility
import com.rongc.feature.ui.ability.list.RecyclerListAbility
import com.rongc.feature.ui.toolbar.BarConfig
import com.rongc.feature.utils.idp
import com.rongc.feature.utils.singleClick
import com.rongc.feature.viewmodel.EmptyBuilder
import com.rongc.feature.widget.IEmptyView
import com.rongc.feature.widget.ItemDecoration

class MainRefreshActivity :
    BaseBindingActivity<BaseRefreshListLayoutBinding, RefreshListViewModel>(),
    IRecyclerListAbility by RecyclerListAbility() {

    override fun initView(view: View) {
        super.initView(view)
//        binding.btnAdd.singleClick {
//            viewModel.items[1] = ""
//        }
//        binding.btnRemove.singleClick {
//            viewModel.items.removeAt(viewModel.items.size - 1)
//        }
    }

    override fun returnRecyclerView(): RecyclerView {
        return binding.includeRecycler.baseRecyclerView
    }

    override fun registerItemBinders(binders: ArrayList<BaseRecyclerItemBinder<out Any>>) {
        binders.add(MainItemHolder())
        binders.add(MainOtherItemHolder())
    }

    override fun getBarConfig(): BarConfig.() -> Unit {
        return {
            menu {
                text = "刷新"
                setOnClickListener {
                    viewModel.refresh(true)
                }
            }
            menu {
                text = "Clear"
                setOnClickListener {
                    viewModel.emptyList()
                }
            }
        }
    }

    override fun decorationBuilder(): ItemDecoration.Builder.() -> Unit {
        return {
            setVerticalLineWidth(5.idp)
        }
    }

    override fun autoRefresh(): Boolean {
        return true
    }

    override fun setupEmptyView(state: Int): EmptyBuilder.() -> Unit {
        return {
            tip = "empty data"
//            tip {
//                text = "empty data"
//            }

            subTip = "pull to refresh"
//            subTip {
//                text = "pull to refresh"
//            }

//            btnText = "refresh"
//            btnVisible = true
            refreshBtn {
                text = "refresh"
                this.isVisible = true
                singleClick {
                    viewModel.refresh()
                }
            }
        }
    }

    override fun providerEmptyView(context: Context): IEmptyView? {
        return super.providerEmptyView(context)
    }
}