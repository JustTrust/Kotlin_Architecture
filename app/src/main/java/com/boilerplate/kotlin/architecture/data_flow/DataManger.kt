package com.boilerplate.kotlin.architecture.data_flow

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import com.boilerplate.kotlin.architecture.data_flow.network.Api
import javax.inject.Inject

/**
 * Created by a.belichenko on 27.09.2017.
 * mail: a.belichenko@gmail.com
 */
class DataManger @Inject constructor(val api: Api): IDataManager {

    override fun getServerResponse(): LiveData<String> {
        return LiveDataReactiveStreams.fromPublisher(api.check())
    }
}