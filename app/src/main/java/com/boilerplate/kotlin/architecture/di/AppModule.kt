package com.boilerplate.kotlin.architecture.di

import android.app.Application
import android.content.Context
import com.boilerplate.kotlin.architecture.data_flow.IDataManager
import com.boilerplate.kotlin.architecture.data_flow.DataManger
import com.boilerplate.kotlin.architecture.data_flow.IUserStorage
import com.boilerplate.kotlin.architecture.data_flow.PreferenceUserStorage
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
    fun provideDataManager(): IDataManager = DataManger()

    @Provides
    @Singleton
    fun provideUserStorage(): IUserStorage = PreferenceUserStorage(context = provideApplicationContext())

    @Provides
    @Singleton
    @Named("something")
    fun provideSomething(): String = "something"

}