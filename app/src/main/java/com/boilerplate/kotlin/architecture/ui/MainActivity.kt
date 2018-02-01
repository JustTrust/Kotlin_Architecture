package com.boilerplate.kotlin.architecture.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.boilerplate.kotlin.architecture.AndroidApplication
import com.boilerplate.kotlin.architecture.R
import com.boilerplate.kotlin.architecture.models.BlueDevice
import com.boilerplate.kotlin.architecture.models.ServerAnsver
import com.boilerplate.kotlin.architecture.ui.adapters.GalleryAdapter
import com.boilerplate.kotlin.architecture.ui.base.BaseActivity
import com.boilerplate.kotlin.architecture.ui.viewModels.MainActivityVM
import com.boilerplate.kotlin.architecture.utils.BlueManager
import com.boilerplate.kotlin.architecture.utils.onClick
import kotlinx.android.synthetic.main.activity_main.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named


class MainActivity : BaseActivity() {

    @field:[Inject Named("something")]
    lateinit var somethingElse: String
    private lateinit var viewModel: MainActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidApplication.component.inject(this)

        viewModel = ViewModelProviders.of(this).get(MainActivityVM::class.java)
        viewModel.getServerAnswer()?.observe(this, Observer<ServerAnsver> { mainText.text = it.toString() })
        Timber.d(somethingElse)
        initList()
        mainText.onClick { onClickLog() }
    }

    private fun initList() {

        gallery.layoutManager = LinearLayoutManager(this)

        BlueManager(this).getDeviceList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list -> gallery.adapter = GalleryAdapter(list, { device -> showToast(device) })
                gallery.adapter.notifyDataSetChanged()},
                { t -> Timber.e(t) })
    }

    private fun showToast(device: BlueDevice) {
        Toast.makeText(this, device.name + " was chosen", Toast.LENGTH_LONG).show()
    }

    private fun onClickLog() {
        Timber.d("On click was fired ")
    }

}
