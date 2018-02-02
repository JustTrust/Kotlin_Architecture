package com.boilerplate.kotlin.architecture.utils

import android.content.Context
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import com.boilerplate.kotlin.architecture.models.BlueDevice
import java.lang.IllegalStateException
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import rx.functions.Action1
import timber.log.Timber


/**
 * Created by a.belichenko@gmail.com on 30.01.2018.
 */
class BlueManager(private val context: Context, private val listener : Action1<BlueDevice>) {

    private val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Timber.d("New blue device")
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    listener.call(BlueDevice(device.name ?: "", device.address))
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                stopBlueSearch()
            }
        }
    }

    @Throws(RuntimeException::class)
    fun startBlueSearch() {
        if (bluetoothAdapter == null){
            throw IllegalStateException()
        }else if(!bluetoothAdapter.isEnabled){
            throw IllegalAccessException()
        }else{
            Timber.d("Start blue device search")
            bluetoothAdapter.startDiscovery()
            context.registerReceiver(receiver, filter)
        }
    }

    fun stopBlueSearch(){
        Timber.d("Stop blue device search")
        bluetoothAdapter.cancelDiscovery()
        context.unregisterReceiver(receiver)
    }

    fun connectToDevice(device: BlueDevice) {


    }
}