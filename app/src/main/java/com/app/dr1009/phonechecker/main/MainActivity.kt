package com.app.dr1009.phonechecker.main

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.app.dr1009.phonechecker.R
import com.app.dr1009.phonechecker.legal.LegalActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {
    companion object {
        private const val SAMPLING_PERIOD_US = 5e5.toInt()
        private const val MAX_REPORT_LATENCY_US = 5e6.toInt()
    }

    private val mAdapter: CardRecyclerAdapter by lazy { CardRecyclerAdapter(applicationContext) }
    private val mSensorManager: SensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recycler.adapter = mAdapter
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_legal -> {
            startActivity(Intent(applicationContext, LegalActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // nop
    }

    override fun onSensorChanged(event: SensorEvent) {
        mAdapter.updateSensor(event.sensor, event.values)
    }
}
