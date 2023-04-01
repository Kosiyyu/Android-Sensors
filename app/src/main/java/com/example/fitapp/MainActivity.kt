package com.example.fitapp

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var stepCounter: Sensor
    private lateinit var stepDetector: Sensor
    private lateinit var accelerometer: Sensor
    private lateinit var textView: TextView
    private var steps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 1)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BODY_SENSORS), 2)
        }


        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        textView = findViewById(R.id.textView)

//        if (stepCounter != null && stepCounter.isWakeUpSensor) {
//            val stepCounterTrigger = object : TriggerEventListener() {
//                override fun onTrigger(event: TriggerEvent?) {
//                    Log.w("TYPE_STEP_COUNTER", "Triggered")
//                }
//            }
//            val status = sensorManager.requestTriggerSensor(stepCounterTrigger, stepCounter)
//            if (!status) {
//                // Handle error
//            }
//        }

        setUpSensors()

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                // Handle sensor reading
                val steps = event.values[0]
                Log.d("TEEEEEEEEEEEEEEEEEEEEEEEST", "Step count: $steps")
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Handle accuracy change
            }
        }
        sensorManager.registerListener(sensorEventListener, stepCounter, SensorManager.SENSOR_DELAY_NORMAL)



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                } else {
                    //permission denied
                }
            }
            2 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                } else {
                    //permission denied
                }
            }
        }
    }


    private fun setUpSensors() {
        // Accelerometer
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometer?.let {
            Log.w("TYPE_ACCELEROMETER", "Accelerometer is available")
            Log.w("TYPE_ACCELEROMETER", "Accelerometer: ${it.name} ${it.vendor} ${it.version} ${it.power} ${it.resolution} ${it.maximumRange}")
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_UI, //int samplingPeriodUs !!!!with SENSOR_DELAY_FASTEST crashes ;cc
                SensorManager.SENSOR_DELAY_UI //int maxReportLatencyUs
            )
        } ?: run {
            Log.w("TYPE_ACCELEROMETER", "Accelerometer is not available")
        }

        // Step Counter
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepCounter?.let {
            Log.w("TYPE_STEP_COUNTER", "StepCounter is available")
            Log.w("TYPE_STEP_COUNTER", "StepCounter: ${it.name} ${it.vendor} ${it.version} ${it.power} ${it.resolution} ${it.maximumRange}")
            sensorManager.registerListener(
                this,
                stepCounter,
                SensorManager.SENSOR_DELAY_UI, //int samplingPeriodUs !!!!with SENSOR_DELAY_FASTEST crashes ;cc
                SensorManager.SENSOR_DELAY_UI //int maxReportLatencyUs
            )
        } ?: run {
            Log.w("TYPE_STEP_COUNTER", "StepCounter is not available")
        }

        // Step Detector
        stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        stepDetector?.let {
            Log.w("TYPE_STEP_DETECTOR", "Step detector is available")
            Log.w("TYPE_STEP_DETECTOR", "Step detector: ${it.name} ${it.vendor} ${it.version} ${it.power} ${it.resolution} ${it.maximumRange}")
            sensorManager.registerListener(
                this,
                stepDetector,
                SensorManager.SENSOR_DELAY_UI, //int samplingPeriodUs !!!!with SENSOR_DELAY_FASTEST crashes ;cc
                SensorManager.SENSOR_DELAY_UI //int maxReportLatencyUs
            )
        } ?: run {
            Log.w("TYPE_STEP_DETECTOR", "Step detector is not available")
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            event.values.let { it ->
                val x = it[0]
                val y = it[1]
                val z = it[2]
                Log.w("TYPE_ACCELEROMETER","$x, $y, $z")

                textView.text = "$x, $y, $z"
            }
        }

        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            event.values.let { it ->
                Log.w("TYPE_STEP_COUNTER","${it[0]} + $steps")
                steps++;
                textView.text = "$steps"
            }
        }


        if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
            event.values.let { it ->
                Log.w("TYPE_STEP_DETECTOR","${it[0]} + $steps")
                steps++;
                textView.text = "$steps"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return Unit
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}