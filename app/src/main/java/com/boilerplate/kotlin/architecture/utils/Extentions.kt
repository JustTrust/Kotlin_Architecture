package com.boilerplate.kotlin.architecture.utils

import android.view.View


/**
 * Created by a.belichenko@gmail.com on 05.10.17.
 */

fun View.onClick(body: () -> Unit) {
    setOnClickListener { body() }
}