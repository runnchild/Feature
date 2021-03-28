package com.rongc.feature.app.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rongc.feature.ui.ability.IPagerItem

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/21
 * @since 2.1.4
 */
class EmptyViewPagerFragmentItem : Fragment(), IPagerItem<String> {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TextView(context).apply {
            setBackgroundColor(Color.BLUE)
        }
    }

    override fun convert(position: Int, item: String, payloads: MutableList<Any>?) {
        (view as TextView).text = item
    }
}