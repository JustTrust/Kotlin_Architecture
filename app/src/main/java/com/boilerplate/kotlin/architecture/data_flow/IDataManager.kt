package com.boilerplate.kotlin.architecture.data_flow

import android.arch.lifecycle.LiveData

/**
 * Created by a.belichenko on 27.09.2017.
 * mail: a.belichenko@gmail.com
 */
interface IDataManager {
    fun getServerResponse(): LiveData<String>
}