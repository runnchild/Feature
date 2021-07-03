package com.rongc.wan.ui

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rongc.demo.databinding.ItemProjectListBinding
import com.rongc.list.adapter.BaseItemBindingBinder
import com.rongc.wan.ProjectList

/**
 * <p>
 * describe:
 *
 * </p>
 * @author qiurong
 * @date 2021/6/16
 */
class ProjectItemBinder : BaseItemBindingBinder<ItemProjectListBinding, ProjectList>() {

    override fun convert(
        binding: ItemProjectListBinding, holder: BaseViewHolder, data: ProjectList
    ) {
        // ui绑定都在xml中做好，这里不需要其他实现
    }
}