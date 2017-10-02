package com.boilerplate.kotlin.architecture.dataFlow.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by a.belichenko on 27.09.2017.
 * mail: a.belichenko@gmail.com
 */
@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id : Long,
    @ColumnInfo(name = "user_name")
    val name : String
)