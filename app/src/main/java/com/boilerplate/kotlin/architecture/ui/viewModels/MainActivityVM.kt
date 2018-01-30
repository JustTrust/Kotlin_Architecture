package com.boilerplate.kotlin.architecture.ui.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.boilerplate.kotlin.architecture.AndroidApplication
import com.boilerplate.kotlin.architecture.dataFlow.IDataManager
import com.boilerplate.kotlin.architecture.models.ServerAnsver
import com.boilerplate.kotlin.architecture.ui.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by a.belichenko@gmail.com on 29.09.17.
 */
class MainActivityVM : BaseViewModel() {

    @Inject lateinit var dataManger: IDataManager
    private var answer: MutableLiveData<ServerAnsver>? = null

    init {
        AndroidApplication.component.inject(this)
    }

    fun getServerAnswer() : LiveData<ServerAnsver>? {
        if (answer == null){
            answer = MutableLiveData()
            disposal.add(dataManger.getServerResponse().subscribe({ it: ServerAnsver ->  answer?.postValue(it)}))
        }
        return answer
    }
}