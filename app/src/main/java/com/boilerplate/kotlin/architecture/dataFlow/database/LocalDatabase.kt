package com.boilerplate.kotlin.architecture.dataFlow.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by a.belichenko@gmail.com on 02.10.17.
 */

@Database(version = 1, entities = arrayOf(User::class))
abstract class LocalDatabase :RoomDatabase(){
    abstract fun localDao(): LocalDao
}