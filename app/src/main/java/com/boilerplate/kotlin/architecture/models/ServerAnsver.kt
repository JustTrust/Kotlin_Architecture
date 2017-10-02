package com.boilerplate.kotlin.architecture.models

import com.google.gson.annotations.SerializedName



/**
 * Created by a.belichenko@gmail.com on 02.10.17.
 */
data class ServerAnsver(
    @SerializedName("args") val args: Args,
    @SerializedName("headers") var headers: Headers,
    @SerializedName("url") var url: String
)