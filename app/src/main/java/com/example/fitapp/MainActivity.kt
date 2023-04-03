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
    private lateinit var textView: TextView
    private lateinit var lineChart: LineChart

    private var queueX = GraphQueue(maxSize = 1000)
    private var queueY = GraphQueue(maxSize = 1000)
    private var queueZ = GraphQueue(maxSize = 1000)

    private var lineDataSet1: LineDataSet? = null
    private var lineDataSet2: LineDataSet? = null
    private var lineDataSet3: LineDataSet? = null

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

        lineChart = findViewById(R.id.line_chart)
        lineChart.axisLeft.isEnabled = true
        lineChart.axisRight.isEnabled = true
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = true
        lineChart.legend.textColor = Color.WHITE

        accelerometer = Accelerometer(sensorManager)
        //accelerometer.registerListener()
        sensorManager.registerListener(this, accelerometer.sensor, SensorManager.SENSOR_DELAY_NORMAL)

        stepCounter = StepCounter(sensorManager)
        stepCounter.registerListener()

        stepDetector = StepDetector(sensorManager)
        stepDetector.registerListener()
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

            queueX.add(x)
            queueY.add(y)
            queueZ.add(z)

            graphUpdate()
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

    private fun graphUpdate() {
        lineDataSet1 = LineDataSet(queueX.getQueue(), "Data Set 1")
        lineDataSet1?.color = Color.RED
        lineDataSet1?.valueTextColor = Color.WHITE
        lineDataSet1?.setDrawValues(false)
        lineDataSet1?.setDrawIcons(false)
        lineDataSet1?.setDrawCircles(false)

        lineDataSet2 = LineDataSet(queueY.getQueue(), "Data Set 2")
        lineDataSet2?.color = Color.BLUE
        lineDataSet2?.valueTextColor = Color.WHITE
        lineDataSet2?.setDrawValues(false)
        lineDataSet2?.setDrawIcons(false)
        lineDataSet2?.setDrawCircles(false)

        lineDataSet3 = LineDataSet(queueZ.getQueue(), "Data Set 3")
        lineDataSet3?.color = Color.GREEN
        lineDataSet3?.valueTextColor = Color.WHITE
        lineDataSet3?.setDrawValues(false)
        lineDataSet3?.setDrawIcons(false)
        lineDataSet3?.setDrawCircles(false)

        lineChart.data = LineData(lineDataSet1, lineDataSet2, lineDataSet3)
        lineChart.invalidate()
    }
}