package com.rongc.feature.app.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rongc.feature.ui.IPagerItem

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/3/21
 * @since 2.1.4
 */
class EmptyViewPagerFragmentItem : Fragment(), IPagerItem<Any> {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(context).apply {
            text = "Hello ViewPager"
//            layoutParams = ViewGroup.LayoutParams(200.idp, 300.idp)
            setBackgroundColor(Color.BLUE)
        }
    }

    override fun convert(position: Int, item: Any, payloads: MutableList<Any>?) {
    }
}