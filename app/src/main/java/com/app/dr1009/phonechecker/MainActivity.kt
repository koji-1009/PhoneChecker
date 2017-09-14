package com.app.dr1009.phonechecker

import android.databinding.DataBindingUtil
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.app.dr1009.phonechecker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    private val SAMPLING_PERIOD_US = 5e5.toInt()
    private val MAX_REPORT_LATENCY_US = 5e6.toInt()

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: CardRecyclerAdapter
    private lateinit var mSensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        mAdapter = CardRecyclerAdapter(applicationContext)
        mBinding.recycler.adapter = mAdapter

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.getSensorList(Sensor.TYPE_ALL).forEach { sensor ->
            mAdapter.insertSensor(sensor)
            mSensorManager.registerListener(this@MainActivity, sensor, SAMPLING_PERIOD_US, MAX_REPORT_LATENCY_US)
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        mAdapter.clearSensors()
        mAdapter.notifyDataSetChanged()
        mSensorManager.getSensorList(Sensor.TYPE_ALL).forEach { sensor ->
            mSensorManager.unregisterListener(this@MainActivity, sensor)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        mAdapter.updateSensor(event.sensor, event.values)
    }
}
