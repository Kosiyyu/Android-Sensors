package com.example.fitapp

import com.github.mikephil.charting.data.Entry


class GraphQueue(private var maxSize: Int = 30, private var counter: Int = 0) {
    private var queueX = mutableListOf<Entry>()
    private var queueY = mutableListOf<Entry>()
    private var queueZ = mutableListOf<Entry>()

    init {
        for (i in 0 until maxSize) {
            queueX.add(Entry(i.toFloat(), 0f))
            queueY.add(Entry(i.toFloat(), 0f))
            queueZ.add(Entry(i.toFloat(), 0f))
        }
    }

    fun add(valueX: Float, valueY: Float, valueZ: Float) {
        queueX.add(Entry(queueX.size.toFloat() + counter, valueX))
        queueY.add(Entry(queueY.size.toFloat() + counter, valueY))
        queueZ.add(Entry(queueZ.size.toFloat() + counter, valueZ))
        counter++
        if (queueX.size > maxSize) {
            queueX.removeAt(0)
            queueY.removeAt(0)
            queueZ.removeAt(0)
        }
    }

    fun getQueue(): Triple<List<Entry>, List<Entry>, List<Entry>> {
        return Triple(queueX, queueY, queueZ)
    }
}