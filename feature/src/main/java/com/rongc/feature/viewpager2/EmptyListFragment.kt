package com.rongc.feature.viewpager2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rongc.feature.viewmodel.RefreshEmptyViewModel
import com.rongc.feature.widget.EmptyView
import com.rongc.feature.widget.IEmptyView

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/28
 */
class EmptyListFragment : Fragment(), IPagerItem<RefreshEmptyViewModel> {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return EmptyView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(-1, -1)
        }
    }

    override fun convert(position: Int, item: RefreshEmptyViewModel, payloads: MutableList<Any>?) {
        (view as IEmptyView).setViewModel(item)
    }
}