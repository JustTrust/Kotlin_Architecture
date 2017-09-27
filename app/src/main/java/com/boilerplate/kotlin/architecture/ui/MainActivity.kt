package com.boilerplate.kotlin.architecture.ui

import android.os.Bundle
import com.boilerplate.kotlin.architecture.AppApplication
import com.boilerplate.kotlin.architecture.R
import com.boilerplate.kotlin.architecture.ui.Base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class MainActivity : BaseActivity() {

    @field:[Inject Named("something")]
    lateinit var somethingElse: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppApplication.component.inject(this)
        mainText.text = getString(R.string.greteeng_string)
    }

    override fun onResume() {
        super.onResume()
        Timber.d(somethingElse)
    }
}
