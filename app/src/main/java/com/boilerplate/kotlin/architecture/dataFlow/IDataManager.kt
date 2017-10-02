package com.boilerplate.kotlin.architecture.dataFlow

import android.arch.lifecycle.LiveData
import com.boilerplate.kotlin.architecture.models.ServerAnsver

/**
 * Created by a.belichenko on 27.09.2017.
 * mail: a.belichenko@gmail.com
 */
interface IDataManager {
    fun getServerResponse(): LiveData<ServerAnsver>
}