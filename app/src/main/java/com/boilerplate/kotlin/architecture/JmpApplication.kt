package com.boilerplate.kotlin.architecture

import android.app.Application
import com.boilerplate.kotlin.architecture.di.AppComponent
import com.boilerplate.kotlin.architecture.di.AppModule
import com.boilerplate.kotlin.architecture.di.DaggerAppComponent
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import timber.log.Timber

/**
 * Created by a.belichenko@gmail.com on 27.09.17.
 */
class JmpApplication : Application() {

    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        component.inject(this)

        if(BuildConfig.DEBUG){
            Stetho.initializeWithDefaults(this)
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) +"::Line:" + element.lineNumber +"::" + element.methodName + "()"
                }
            })
        }
        Fresco.initialize(this)
    }
}