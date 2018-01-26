package com.boilerplate.kotlin.architecture.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.boilerplate.kotlin.architecture.JmpApplication
import com.boilerplate.kotlin.architecture.R
import com.boilerplate.kotlin.architecture.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import com.boilerplate.kotlin.architecture.models.ServerAnsver
import com.boilerplate.kotlin.architecture.ui.adapters.GalleryAdapter
import com.boilerplate.kotlin.architecture.ui.viewModels.MainActivityVM
import com.boilerplate.kotlin.architecture.utils.onClick
import java.util.ArrayList
import com.boilerplate.kotlin.architecture.utils.SpeedyLinearLayoutManager


class MainActivity : BaseActivity() {

    @field:[Inject Named("something")]
    lateinit var somethingElse: String
    private lateinit var viewModel: MainActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JmpApplication.component.inject(this)

        viewModel = ViewModelProviders.of(this).get(MainActivityVM::class.java)
        viewModel.getServerAnswer()?.observe(this, Observer<ServerAnsver> { mainText.text = it.toString()})
        Timber.d(somethingElse)
        initList()
        mainText.onClick { onClickLog() }
    }

    private fun initList(){

        val speedyLayoutManager = SpeedyLinearLayoutManager(this)
        speedyLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        gallery.layoutManager = speedyLayoutManager

        val items : ArrayList<String> = ArrayList()
        for (index in 1..10) items += (getString(R.string.item_header)+index)
        gallery.adapter = GalleryAdapter(items) {
            Timber.d("$it was Clicked")
        }
    }

    private fun onClickLog() {
        Timber.d("On click was fired ")
    }

}
