package com.jetruby.motionsensors.example

import android.content.Context
import android.databinding.DataBindingUtil
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jetruby.motionsensors.example.databinding.ActivityMainBinding
import java.text.DecimalFormat

private const val T: Int = 2

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var sensorManager: SensorManager

    private var accelerometer: Sensor? = null
    private var gravity: Sensor? = null
    private var gyroscope: Sensor? = null
    private var linearAcceleration: Sensor? = null
    private var rotationVector: Sensor? = null

    private val accelGravity: FloatArray = FloatArray(3)
    private val accelLin: FloatArray = FloatArray(3)

    private val valuesGravity: FloatArray = FloatArray(3)
    private val valuesRotation: FloatArray = FloatArray(3)
    private val valuesGyroscope: FloatArray = FloatArray(3)
    private val valuesLinAcceleration: FloatArray = FloatArray(3)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            this.accelerometer = it
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)?.let {
            this.gravity = it
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let {
            this.gyroscope = it
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)?.let {
            this.linearAcceleration = it
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.let {
            this.rotationVector = it
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {

        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val alpha = 0.8f

                accelGravity[0] = alpha * accelGravity[0] + (1 - alpha) * event.values[0]
                accelGravity[1] = alpha * accelGravity[1] + (1 - alpha) * event.values[1]
                accelGravity[2] = alpha * accelGravity[2] + (1 - alpha) * event.values[2]

                accelLin[0] = event.values[0] - accelGravity[0]
                accelLin[1] = event.values[1] - accelGravity[1]
                accelLin[2] = event.values[2] - accelGravity[2]

                this.binding.tvAccelerometerX.text = accelLin[0].formatValue()
                this.binding.tvAccelerometerY.text = accelLin[1].formatValue()
                this.binding.tvAccelerometerZ.text = accelLin[2].formatValue()
            }

            Sensor.TYPE_GRAVITY -> {
                valuesGravity[0] = event.values[0]
                valuesGravity[1] = event.values[1]
                valuesGravity[2] = event.values[2]

                this.binding.tvGravity.text = event.values[0].formatValue()
                this.binding.tvGravityY.text = event.values[1].formatValue()
                this.binding.tvGravityZ.text = event.values[2].formatValue()
            }

            Sensor.TYPE_GYROSCOPE -> {
                valuesGyroscope[0] = event.values[0]
                valuesGyroscope[1] = event.values[1]
                valuesGyroscope[2] = event.values[2]

                this.binding.tvGyroscope.text = event.values[0].formatValue()
                this.binding.tvGyroscopeY.text = event.values[1].formatValue()
                this.binding.tvGyroscopeZ.text = event.values[2].formatValue()
            }

            Sensor.TYPE_LINEAR_ACCELERATION -> {
                valuesLinAcceleration[0] = event.values[0]
                valuesLinAcceleration[1] = event.values[1]
                valuesLinAcceleration[2] = event.values[2]

                this.binding.tvLinearAccelX.text = event.values[0].formatValue()
                this.binding.tvLinearAccelY.text = event.values[1].formatValue()
                this.binding.tvLinearAccelZ.text = event.values[2].formatValue()
            }

            Sensor.TYPE_ROTATION_VECTOR -> {
                valuesRotation[0] = event.values[0]
                valuesRotation[1] = event.values[1]
                valuesRotation[2] = event.values[2]

                this.binding.tvRotationVector.text = event.values[0].formatValue()
                this.binding.tvRotationVectorY.text = event.values[1].formatValue()
                this.binding.tvRotationVectorZ.text = event.values[2].formatValue()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, linearAcceleration, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, rotationVector, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this, accelerometer)
        sensorManager.unregisterListener(this, gravity)
        sensorManager.unregisterListener(this, gyroscope)
        sensorManager.unregisterListener(this, linearAcceleration)
        sensorManager.unregisterListener(this, rotationVector)
    }

    private fun Float.formatValue(): String {
        val precision = DecimalFormat("0.00")
        return precision.format(this)
    }
}
