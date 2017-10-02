package com.boilerplate.kotlin.architecture.dataFlow.network

import com.boilerplate.kotlin.architecture.models.ServerAnsver
import io.reactivex.Flowable
import retrofit2.http.GET

/**
 * Created by a.belichenko on 27.09.2017.
 * mail: a.belichenko@gmail.com
 */
interface Api {
    @GET("get")
    fun check(): Flowable<ServerAnsver>
}