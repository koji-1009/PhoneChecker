package com.app.dr1009.phonechecker

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private val SAMPLING_PERIOD_US = 5e5.toInt()
    private val MAX_REPORT_LATENCY_US = 5e6.toInt()

    private lateinit var mAdapter: CardRecyclerAdapter
    private lateinit var mSensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mAdapter = CardRecyclerAdapter(applicationContext)
        recycler.adapter = mAdapter

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_legal -> {
                startActivity(Intent(applicationContext, LegalActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // nor
    }

    override fun onSensorChanged(event: SensorEvent) {
        mAdapter.updateSensor(event.sensor, event.values)
    }
}
