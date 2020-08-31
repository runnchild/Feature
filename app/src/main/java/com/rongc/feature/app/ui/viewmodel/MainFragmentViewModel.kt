package com.rongc.feature.app.ui.viewmodel

import android.content.Intent
import android.view.View
import com.rongc.feature.app.model.MainModel
import com.rongc.feature.app.ui.MainRefreshActivity
import com.rongc.feature.utils.Compat.logw
import com.rongc.feature.viewmodel.BaseViewModel

class MainFragmentViewModel : BaseViewModel<MainModel>() {

    val openListPage = { view: View ->
        view.context.startActivity(Intent(view.context, MainRefreshActivity::class.java))
    }
    
    val click = {
        
    }

    fun click(pa: Int):()->Unit {
        "before return $pa".logw()
        return {
            "return block $pa".logw()
        }
    }
}