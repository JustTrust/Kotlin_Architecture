package com.boilerplate.kotlin.architecture.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.boilerplate.kotlin.architecture.models.BlueDevice
import rx.Observable
import rx.Subscriber
import rx.subscriptions.Subscriptions
import timber.log.Timber
import java.lang.IllegalStateException


/**
 * Created by a.belichenko@gmail.com on 30.01.2018.
 */
class BlueManager(private val context: Context) {

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    fun getBlueDevices(): Observable<BlueDevice> {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

        return if (bluetoothAdapter == null) {
            Observable.error(IllegalStateException())
        } else if (!bluetoothAdapter.isEnabled) {
            Observable.error(IllegalAccessException())
        } else {
            Observable.create(object : Observable.OnSubscribe<BlueDevice> {
                override fun call(subscriber: Subscriber<in BlueDevice>) {
                    val receiver = object : BroadcastReceiver() {
                        override fun onReceive(context: Context, intent: Intent) {
                            val action = intent.action
                            if (BluetoothDevice.ACTION_FOUND == action) {
                                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                                Timber.d("New blue device")
                                subscriber.onNext(BlueDevice(device.name?: "", device.address))
                            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                                subscriber.onCompleted()
                            }
                        }
                    }
                    Timber.d("Start blue receiver")
                    bluetoothAdapter.startDiscovery()
                    context.registerReceiver(receiver, filter)

                    subscriber.add(Subscriptions.create {
                        Timber.d("Stop blue receiver")
                        bluetoothAdapter.cancelDiscovery()
                        context.unregisterReceiver(receiver)
                    })
                }
            })
        }
    }
}