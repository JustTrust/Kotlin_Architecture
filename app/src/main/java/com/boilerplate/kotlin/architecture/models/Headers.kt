package com.boilerplate.kotlin.architecture.models

import com.google.gson.annotations.SerializedName


/**
 * Created by a.belichenko@gmail.com on 02.10.17.
 */
data class Headers(
    @SerializedName("host") val host: String,

    @SerializedName("user-agent") val userAgent: String,

    @SerializedName("upgrade-insecure-requests") val upgradeInsecureRequests: String,

    @SerializedName("accept") val accept: String,

    @SerializedName("dnt") val dnt: String,

    @SerializedName("accept-encoding") val acceptEncoding: String,

    @SerializedName("accept-language") val acceptLanguage: String,

    @SerializedName("cookie") val cookie: String)