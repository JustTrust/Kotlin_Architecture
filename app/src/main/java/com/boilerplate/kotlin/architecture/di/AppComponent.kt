package com.boilerplate.kotlin.architecture.di

import com.boilerplate.kotlin.architecture.AppApplication
import com.boilerplate.kotlin.architecture.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by a.belichenko@gmail.com on 27.09.17.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(application: AppApplication)

    fun inject(mainActivity: MainActivity)
}