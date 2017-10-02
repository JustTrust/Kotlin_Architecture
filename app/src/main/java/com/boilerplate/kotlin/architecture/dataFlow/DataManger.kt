package com.boilerplate.kotlin.architecture.dataFlow

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import com.boilerplate.kotlin.architecture.dataFlow.network.Api
import com.boilerplate.kotlin.architecture.models.ServerAnsver
import javax.inject.Inject

/**
 * Created by a.belichenko on 27.09.2017.
 * mail: a.belichenko@gmail.com
 */
class DataManger @Inject constructor(private val api: Api): IDataManager {

    override fun getServerResponse(): LiveData<ServerAnsver> {
        return LiveDataReactiveStreams.fromPublisher(api.check())
    }
}