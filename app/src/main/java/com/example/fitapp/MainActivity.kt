package com.example.fitapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    private lateinit var stepCounter: StepCounter
    private lateinit var stepDetector: StepDetector
    private lateinit var accelerometer: Accelerometer
    private lateinit var gyroscope: Gyroscope

    private lateinit var textView: TextView
    private lateinit var accelerometerChart: LineChart
    private lateinit var gyroscopeChart: LineChart

    private val accelerometerQueue = GraphQueue(maxSize = 100)
    private val gyroscopeQueue = GraphQueue(maxSize = 100)

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
        accelerometerChart = findViewById(R.id.accelerometer_chart)
        gyroscopeChart = findViewById(R.id.gyroscope_chart)

        accelerometer = Accelerometer(sensorManager)
        //accelerometer.registerListener()
        sensorManager.registerListener(this, accelerometer.sensor, SensorManager.SENSOR_DELAY_NORMAL)

        gyroscope = Gyroscope(sensorManager)
        //gyroscope.registerListener()
        sensorManager.registerListener(this, gyroscope.sensor, SensorManager.SENSOR_DELAY_NORMAL)

        stepCounter = StepCounter(sensorManager)
        stepCounter.registerListener()

        stepDetector = StepDetector(sensorManager)
        stepDetector.registerListener()

        accelerometerChart.axisLeft.isEnabled = true
        accelerometerChart.axisRight.isEnabled = true
        accelerometerChart.description.isEnabled = false
        accelerometerChart.legend.isEnabled = true
        accelerometerChart.legend.textColor = Color.WHITE
        accelerometerChart.xAxis.textColor = Color.WHITE
        accelerometerChart.axisLeft.textColor = Color.WHITE
        accelerometerChart.axisRight.textColor = Color.WHITE

        gyroscopeChart.axisLeft.isEnabled = true
        gyroscopeChart.axisRight.isEnabled = true
        gyroscopeChart.description.isEnabled = false
        gyroscopeChart.legend.isEnabled = true
        gyroscopeChart.legend.textColor = Color.WHITE
        gyroscopeChart.xAxis.textColor = Color.WHITE
        gyroscopeChart.axisLeft.textColor = Color.WHITE
        gyroscopeChart.axisRight.textColor = Color.WHITE
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

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            accelerometerQueue.add(x, y, z)

            accelerometerGraphUpdate()
        }

        if(event?.sensor?.type == Sensor.TYPE_GYROSCOPE){
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            gyroscopeQueue.add(x, y, z)

            gyroscopeGraphUpdate()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return Unit
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        accelerometer.unregisterListener()
        stepCounter.unregisterListener()
        stepDetector.unregisterListener()
        super.onDestroy()
    }

    private fun accelerometerGraphUpdate() {
        val queues = accelerometerQueue.getQueue()
        val lineDataSetX = LineDataSet(queues.first, "x")
        lineDataSetX.color = Color.RED
        lineDataSetX.valueTextColor = Color.WHITE
        lineDataSetX.setDrawValues(false)
        lineDataSetX.setDrawIcons(false)
        lineDataSetX.setDrawCircles(false)

        val lineDataSetY = LineDataSet(queues.second, "y")
        lineDataSetY.color = Color.BLUE
        lineDataSetY.valueTextColor = Color.WHITE
        lineDataSetY.setDrawValues(false)
        lineDataSetY.setDrawIcons(false)
        lineDataSetY.setDrawCircles(false)

        val lineDataSetZ = LineDataSet(queues.third, "z")
        lineDataSetZ.color = Color.GREEN
        lineDataSetZ.valueTextColor = Color.WHITE
        lineDataSetZ.setDrawValues(false)
        lineDataSetZ.setDrawIcons(false)
        lineDataSetZ.setDrawCircles(false)

        accelerometerChart.data = LineData(lineDataSetX, lineDataSetY, lineDataSetZ)
        accelerometerChart.invalidate()
    }

    private fun gyroscopeGraphUpdate() {
        val queues = gyroscopeQueue.getQueue()
        val lineDataSetX = LineDataSet(queues.first, "x")
        lineDataSetX.color = Color.RED
        lineDataSetX.valueTextColor = Color.WHITE
        lineDataSetX.setDrawValues(false)
        lineDataSetX.setDrawIcons(false)
        lineDataSetX.setDrawCircles(false)

        val lineDataSetY = LineDataSet(queues.second, "y")
        lineDataSetY.color = Color.BLUE
        lineDataSetY.valueTextColor = Color.WHITE
        lineDataSetY.setDrawValues(false)
        lineDataSetY.setDrawIcons(false)
        lineDataSetY.setDrawCircles(false)

        val lineDataSetZ = LineDataSet(queues.third, "z")
        lineDataSetZ.color = Color.GREEN
        lineDataSetZ.valueTextColor = Color.WHITE
        lineDataSetZ.setDrawValues(false)
        lineDataSetZ.setDrawIcons(false)
        lineDataSetZ.setDrawCircles(false)

        gyroscopeChart.data = LineData(lineDataSetX, lineDataSetY, lineDataSetZ)
        gyroscopeChart.invalidate()
    }
}