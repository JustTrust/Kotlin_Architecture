package com.boilerplate.kotlin.architecture.di

import android.app.Application
import android.content.Context
import com.boilerplate.kotlin.architecture.dataFlow.IDataManager
import com.boilerplate.kotlin.architecture.dataFlow.DataManger
import com.boilerplate.kotlin.architecture.dataFlow.IUserStorage
import com.boilerplate.kotlin.architecture.dataFlow.PreferenceUserStorage
import com.boilerplate.kotlin.architecture.dataFlow.network.Api
import com.boilerplate.kotlin.architecture.utils.BlueManager
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by a.belichenko@gmail.com on 27.09.17.
 */

@Module(includes = arrayOf(NetworkModule::class))
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideDataManager(api : Api): IDataManager{
        return DataManger(api)
    }

    @Provides
    @Singleton
    fun provideUserStorage(): IUserStorage = PreferenceUserStorage(provideApplicationContext())

    @Provides
    @Singleton
    @Named("something")
    fun provideSomething(): String = "something"
}