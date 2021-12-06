package com.example.accelerationtest

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var sManager: SensorManager
    private var magnetic = FloatArray(9)
    private var gravity = FloatArray(9)

    private var accelerations = FloatArray(3)
    private var magneticFields = FloatArray(3)
    private var values = FloatArray(3)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvSensor = findViewById<TextView>(R.id.tvSensor)
        val lRotation = findViewById<LinearLayout>(R.id.lRotation)

        sManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensor2 = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val sListener = object : SensorEventListener {
            override fun onSensorChanged(sEvent: SensorEvent?) {
                // когда работает определенный сенсор (акселерометр или магнетик) заполняются соответствующие массивы
                when (sEvent?.sensor?.type) {
                    Sensor.TYPE_ACCELEROMETER -> accelerations = sEvent.values.clone()
                    Sensor.TYPE_MAGNETIC_FIELD -> magneticFields = sEvent.values.clone()
                }

                //  блок который переводит из радиан в градусы
                SensorManager.getRotationMatrix(gravity, magnetic, accelerations, magneticFields)
                val outGravity = FloatArray(9)
                SensorManager.remapCoordinateSystem(gravity,
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_MINUS_Z,
                    outGravity)
                SensorManager.getOrientation(outGravity, values)
                val degree = values[2] * 57.2958f
                val rotate = 270 + degree
                lRotation.rotation = rotate
                val rData = 90 + degree
                val color = if (rData.toInt() == 0){
                    Color.GREEN
                }else{
                    Color.RED
                }
                lRotation.setBackgroundColor(color)
                tvSensor.text = rData.toInt().toString()
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }
        }
        sManager.registerListener(sListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        sManager.registerListener(sListener, sensor2, SensorManager.SENSOR_DELAY_NORMAL)
    }

}