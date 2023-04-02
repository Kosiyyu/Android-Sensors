package com.example.fitapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class StepCounter(private val sensorManager: SensorManager) : SensorEventListener, LocalSensor {

    private var steps = 0
    private var sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    override fun onSensorChanged(event: SensorEvent?) {
        event?.values.let { it ->
            Log.w("TYPE_STEP_COUNTER","${it?.get(0)} + ($steps)")
            steps++;
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