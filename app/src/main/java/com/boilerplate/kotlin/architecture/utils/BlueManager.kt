package com.boilerplate.kotlin.architecture.utils

import android.content.Context
import rx.Observable
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import com.boilerplate.kotlin.architecture.models.BlueDevice
import java.lang.IllegalStateException
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 * Created by a.belichenko@gmail.com on 30.01.2018.
 */
class BlueManager(private val context: Context) {

    private val deviceList : ArrayList<BlueDevice> = ArrayList()
    private val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                Timber.d("New blue device")
                deviceList.add(BlueDevice(device.name, device.address))
            }
        }
    }

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    fun getDeviceList() : Observable<List<BlueDevice>>{
        return if (bluetoothAdapter == null){
            Observable.error(IllegalStateException())
        }else if(!bluetoothAdapter.isEnabled){
            Observable.error(IllegalAccessException())
        }else{
            Observable.just(deviceList.toList())
                    .doOnSubscribe({
                        Timber.d("Start blue receiver")
                        bluetoothAdapter.startDiscovery()
                        context.registerReceiver(receiver, filter) })
                    .zipWith(Observable.interval(19, TimeUnit.SECONDS), { t1, _ -> t1 })
                    .doOnNext({
                        Timber.d("Stop blue receiver")
                        bluetoothAdapter.cancelDiscovery()
                        context.unregisterReceiver(receiver) })
        }
    }

    private fun checkBlueState() : Observable<String>{
        return Observable.error(IllegalStateException())
    }
}