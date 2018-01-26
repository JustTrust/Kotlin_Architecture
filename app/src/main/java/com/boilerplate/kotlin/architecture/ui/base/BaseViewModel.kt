package com.boilerplate.kotlin.architecture.ui.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by a.belichenko@gmail.com on 27.09.17.
 */
abstract class BaseViewModel : ViewModel() {

    protected val disposal : CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        if (!disposal.isDisposed) disposal.dispose()
    }
}