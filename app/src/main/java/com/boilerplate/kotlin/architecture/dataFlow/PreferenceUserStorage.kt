package com.boilerplate.kotlin.architecture.dataFlow

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by a.belichenko on 27.09.2017.
 * mail: a.belichenko@gmail.com
 */
class PreferenceUserStorage(var context: Context) : IUserStorage {

    companion object {
        private const val NAME = "app_preference_name"
    }

    private val preference: SharedPreferences

    init {
        preference = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    override fun getName() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setName() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}