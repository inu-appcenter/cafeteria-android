/**
 * This file is part of INU Cafeteria.
 *
 * Copyright (C) 2020 INU Global App Center <potados99@gmail.com>
 *
 * INU Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.inu.cafeteria.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import timber.log.Timber

class ShakeListener(private val context: Context) : SensorEventListener {

    private var sensorManager: SensorManager? = null

    private var lastShakeTime: Long = 0
    private var onShake: () -> Unit = {}

    init {
        resume()
    }

    fun setOnShakeListener(listener: () -> Unit) {
        onShake = listener
    }

    fun resume() {
        (context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager)?.let {
            Timber.d("SensorManager available!")

            sensorManager = it

            val accelerometer = it.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            it.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        }

    }

    fun pause() {
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Ignore
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER){
            return
        }

        val now = System.currentTimeMillis()
        val diff = now - lastShakeTime

        if (diff < TIME_THRESHOLD) {
            return
        }

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val acceleration = Vector.sum(x, y, z) - SensorManager.GRAVITY_EARTH

        if (acceleration < SHAKE_THRESHOLD) {
            return
        }

        lastShakeTime = now

        onShake()
    }

    companion object {
        private const val SHAKE_THRESHOLD = 60.00f // m/s^2
        private const val TIME_THRESHOLD = 500
    }
}