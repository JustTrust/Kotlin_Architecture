package com.boilerplate.kotlin.architecture.utils

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import timber.log.Timber

/**
 * Created by a.belichenko@gmail.com on 09.10.17.
 */
class MovementDetector constructor(context: Context, lifecycle: Lifecycle, val listener: (Int) -> Unit) : LifecycleObserver, SensorEventListener {

    private val sensorManager: SensorManager

    private val sensor: Sensor

    private var values: FloatArray = floatArrayOf(0f, 0f, 0f, 0f)

    private var moveDirection = 0

    init {
        sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Timber.d("Start listen movements")
        sensorManager.registerListener(this, sensor, 1000)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Timber.d("Stop listen movements")
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Timber.d("Accuracy was changed %d", p1)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.values != null) {
            values = event.values.clone()

            val norm = Math.sqrt((values[0] * values[0] + values[1] * values[1] + values[2] * values[2]).toDouble())

            values[0] = values[0] / norm.toFloat()
            values[1] = values[1] / norm.toFloat()
            values[2] = values[2] / norm.toFloat()

            //val inclination = Math.round(Math.toDegrees(Math.acos(values[0].toDouble()))).toInt()
            //val rotation = Math.round(Math.toDegrees(Math.atan2(values[0].toDouble(), values[2].toDouble()))).toInt()
            //Timber.d("Angle 0= %f     1= %f     2=%f", values[0], values[1], values[2])
            Timber.d("Angle = %f", values[1])
            val newDirection = Math.round(values[1] * -10)

            if (newDirection != moveDirection) {
                moveDirection = newDirection
                listener.invoke(moveDirection)
            }
        }
    }
}