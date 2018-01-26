package com.boilerplate.kotlin.architecture.dataFlow

import com.boilerplate.kotlin.architecture.models.ServerAnsver
import io.reactivex.Flowable

/**
 * Created by a.belichenko on 27.09.2017.
 * mail: a.belichenko@gmail.com
 */
interface IDataManager {
    fun getServerResponse(): Flowable<ServerAnsver>
}