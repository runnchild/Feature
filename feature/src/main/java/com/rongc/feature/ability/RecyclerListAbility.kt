package com.rongc.feature.ability

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

class RecyclerListAbility(private val host: IListAbility, private val recyclerView: RecyclerView) : IAbility {

    override fun onCreate(owner: LifecycleOwner) {
        recyclerView.layoutManager = host.providerLayoutManager(recyclerView.context)
        val providerAdapter = host.providerAdapter()
        recyclerView.adapter = providerAdapter
    }
}