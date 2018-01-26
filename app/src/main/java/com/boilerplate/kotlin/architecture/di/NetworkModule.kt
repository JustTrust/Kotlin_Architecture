package com.boilerplate.kotlin.architecture.di

import com.boilerplate.kotlin.architecture.BuildConfig
import com.boilerplate.kotlin.architecture.dataFlow.network.Api
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Created by a.belichenko on 27.09.2017.
 * mail: a.belichenko@gmail.com
 */

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(httpUrl: HttpUrl,
                        client: OkHttpClient,
                        gson: Gson): Retrofit {
        return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
    }

    @Singleton
    @Provides
    fun provideHttpUrl() = HttpUrl.parse(BuildConfig.API_BASE_URL)!!

    @Singleton
    @Provides
    fun provideOkHttpClient(loggerInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor { it ->
                val original = it.request()
                val request = original.newBuilder()
                        .header("Authorization", "73dd89f934c0ca99d66b2ddd")
                        .method(original.method(), original.body())
                        .build()

                it.proceed(request)
            }
            .addNetworkInterceptor(loggerInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideHttpLoggerInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.tag(NETWORK_TAG).d(it)
        })
        logger.level = HttpLoggingInterceptor.Level.BODY

        return logger
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
            .serializeNulls()
            .create()

    companion object {
        private const val NETWORK_TAG = "Network"
    }
}