package com.boilerplate.kotlin.architecture.di

import com.boilerplate.kotlin.architecture.AndroidApplication
import com.boilerplate.kotlin.architecture.ui.MainActivity
import com.boilerplate.kotlin.architecture.ui.viewModels.MainActivityVM
import dagger.Component
import javax.inject.Singleton

/**
 * Created by a.belichenko@gmail.com on 27.09.17.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(application: AndroidApplication)

    fun inject(mainActivity: MainActivity)

    fun inject(mainActivityVM: MainActivityVM)
}