package com.boilerplate.kotlin.architecture.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.boilerplate.kotlin.architecture.AppApplication
import com.boilerplate.kotlin.architecture.R
import com.boilerplate.kotlin.architecture.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import android.arch.lifecycle.ViewModelProviders
import com.boilerplate.kotlin.architecture.models.ServerAnsver
import com.boilerplate.kotlin.architecture.ui.viewModels.MainActivityVM


class MainActivity : BaseActivity() {

    @field:[Inject Named("something")]
    lateinit var somethingElse: String
    private lateinit var viewModel: MainActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppApplication.component.inject(this)
        viewModel = ViewModelProviders.of(this).get(MainActivityVM::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getServerAnswer().observe(this, Observer<ServerAnsver> { mainText.text = it.toString() })
        Timber.d(somethingElse)
    }
}
