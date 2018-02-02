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
import com.boilerplate.kotlin.architecture.utils.LocationPermissionHelper
import com.boilerplate.kotlin.architecture.utils.onClick
import kotlinx.android.synthetic.main.activity_main.*
import rx.functions.Action1
import timber.log.Timber


class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainActivityVM
    private val list: ArrayList<BlueDevice> = ArrayList()
    private lateinit var blueManager: BlueManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidApplication.component.inject(this)

        viewModel = ViewModelProviders.of(this).get(MainActivityVM::class.java)
        viewModel.getServerAnswer()?.observe(this, Observer<ServerAnsver> { mainText.text = it.toString() })
        blueManager = BlueManager(this, Action1 { blueDevice ->
            list.add(blueDevice)
            gallery.adapter.notifyItemInserted(list.size - 1)
        })

        initList()
        mainText.onClick { onClickLog() }
    }

    private fun initList() {

        gallery.layoutManager = LinearLayoutManager(this)
        gallery.adapter = GalleryAdapter(list, { device -> connectToDevice(device) })
        if (LocationPermissionHelper.hasLocationPermission(this)) {
            blueManager.startBlueSearch()
        } else {
            LocationPermissionHelper.requestLocationPermission(this)
        }
    }

    private fun connectToDevice(device: BlueDevice) {
        showToast(device)
        blueManager.stopBlueSearch()
        blueManager.connectToDevice(device)
    }

    override fun onPause() {
        super.onPause()
        blueManager.stopBlueSearch()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, results: IntArray) {
        if (!LocationPermissionHelper.hasLocationPermission(this)) {
            Toast.makeText(this, "Location permission is needed to find bluetooth devices", Toast.LENGTH_LONG).show()
            if (!LocationPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                LocationPermissionHelper.launchPermissionSettings(this)
            }
        }
    }

    private fun showToast(device: BlueDevice) {
        Toast.makeText(this, device.name + " was chosen", Toast.LENGTH_LONG).show()
    }

    private fun onClickLog() {
        Timber.d("On click was fired ")
    }

}
