package com.boilerplate.kotlin.architecture.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by a.belichenko@gmail.com on 27.09.17.
 */

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    @Named("something")
    fun provideSomething(): String = "something"

}