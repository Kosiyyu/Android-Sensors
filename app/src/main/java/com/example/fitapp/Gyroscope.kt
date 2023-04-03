package com.example.fitapp

import android.hardware.Sensor

import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class Gyroscope(private val sensorManager: SensorManager) : SensorEventListener, LocalSensor {
    private var x: Float? = null
    private var y: Float? = null
    private var z: Float? = null
    val sensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    override fun onSensorChanged(event: SensorEvent?) {
        event?.values.let{it ->
            x = it?.get(0)
            y = it?.get(1)
            z = it?.get(2)
            //Log.w("TYPE_GYROSCOPE","$x, $y, $z")

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //TODO("Not yet implemented")
    }

    override fun registerListener() {
        sensorManager.registerListener(
            this,
            sensor,
            SensorManager.SENSOR_DELAY_UI,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun unregisterListener() {
        sensorManager.unregisterListener(this)
    }

}