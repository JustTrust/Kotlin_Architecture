package com.boilerplate.kotlin.architecture.ui

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject


class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainActivityVM
    private val list: ArrayList<BlueDevice> = ArrayList()
    private val REQUEST_ENABLE_BT: Int = 11

    @Inject
    lateinit var blueManager: BlueManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidApplication.component.inject(this)

        viewModel = ViewModelProviders.of(this).get(MainActivityVM::class.java)
        viewModel.getServerAnswer()?.observe(this, Observer<ServerAnsver> { mainText.text = it.toString() })
        initList()
        mainText.onClick { onClickLog() }
    }

    private fun initList() {

        gallery.adapter = GalleryAdapter(list, { device: BlueDevice -> showToast("Device ${device.name} was chosen") })

        if (LocationPermissionHelper.hasLocationPermission(this)) {
            getDeviceList()
        } else {
            LocationPermissionHelper.requestLocationPermission(this)
        }
    }

    private fun getDeviceList() {
        list.clear()
        gallery.adapter.notifyDataSetChanged()
        blueManager.getBlueDevices()
                .subscribe({ newDevice -> list.add(newDevice)
                    gallery.adapter.notifyItemInserted(list.size - 1)
                }, { t -> handleError(t) })
    }

    private fun handleError(t: Throwable?) {
        when (t) {
            is IllegalAccessException -> startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT)
            is IllegalStateException -> showToast("Your device does not support BT")
            else -> showToast(t?.message)
        }
    }

    private fun showToast(msg: String?) {
        if (!TextUtils.isEmpty(msg)) Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun onClickLog() {
        Timber.d("On click was fired ")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_ENABLE_BT) {
            getDeviceList()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, results: IntArray) {
        if (!LocationPermissionHelper.hasLocationPermission(this)) {
            Toast.makeText(this, "Location permission is needed to find bluetooth devices", Toast.LENGTH_LONG).show()
            if (!LocationPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                LocationPermissionHelper.launchPermissionSettings(this)
            }
        } else {
            getDeviceList()
        }
    }
}