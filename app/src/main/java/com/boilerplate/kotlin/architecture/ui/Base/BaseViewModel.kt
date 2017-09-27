package com.boilerplate.kotlin.architecture.ui.Base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by a.belichenko@gmail.com on 27.09.17.
 */
abstract class BaseViewModel : ViewModel() {
    val disposal : CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        if (!disposal.isDisposed) disposal.dispose()
    }
}