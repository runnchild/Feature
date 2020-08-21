package com.rongc.habit.app.viewmodel

import android.content.Intent
import android.view.View
import com.rongc.habit.app.model.MainModel
import com.rongc.habit.model.BaseModel
import com.rongc.habit.app.ui.MainRefreshActivity
import com.rongc.habit.utils.Compat.logw
import com.rongc.habit.utils.Compat.toast
import com.rongc.habit.viewmodel.BaseViewModel

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