package com.boilerplate.kotlin.architecture.dataFlow.database

import android.arch.persistence.room.*
import io.reactivex.Flowable


/**
 * Created by a.belichenko@gmail.com on 02.10.17.
 */

@Dao
interface LocalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(vararg user: User)

    @Update
    fun updateNews(vararg user: User)

    @Delete
    fun deleteNews(vararg user: User)

    @Query("SELECT * FROM user_table")
    fun getNewsList(): Flowable<List<User>>

    @Query("SELECT * FROM user_table WHERE _id = :userId")
    fun getOneNews(userId: Int): Flowable<User>
}