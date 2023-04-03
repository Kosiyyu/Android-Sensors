package com.example.fitapp

import com.github.mikephil.charting.data.Entry


class GraphQueue(private var maxSize: Int = 30, private var counter: Int = 0) {
    private var queue = mutableListOf<Entry>()

    init {
        for (i in 0 until maxSize) {
            queue.add(Entry(i.toFloat(), 0f))
        }
    }

    fun add(value: Float) {
        queue.add(Entry(queue.size.toFloat() + counter, value))
        counter++
        if (queue.size > maxSize) {
            queue.removeAt(0)
        }
    }

    fun getQueue(): MutableList<Entry> {
        return queue
    }
}