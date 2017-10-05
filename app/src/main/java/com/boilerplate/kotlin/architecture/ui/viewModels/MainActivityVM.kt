package com.boilerplate.kotlin.architecture.ui.viewModels

import android.arch.lifecycle.LiveData
import com.boilerplate.kotlin.architecture.AppApplication
import com.boilerplate.kotlin.architecture.dataFlow.IDataManager
import com.boilerplate.kotlin.architecture.models.ServerAnsver
import com.boilerplate.kotlin.architecture.ui.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by a.belichenko@gmail.com on 29.09.17.
 */
class MainActivityVM : BaseViewModel() {

    @Inject lateinit var dataManger: IDataManager
    private lateinit var answer: LiveData<ServerAnsver>

    init {
        AppApplication.component.inject(this)
    }

    fun getServerAnswer() : LiveData<ServerAnsver> {
        answer = dataManger.getServerResponse()
        return answer
    }
}