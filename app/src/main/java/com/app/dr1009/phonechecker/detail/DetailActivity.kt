package com.app.dr1009.phonechecker.detail

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.app.dr1009.phonechecker.R
import com.app.dr1009.phonechecker.main.CardPresenter
import com.app.dr1009.phonechecker.realm.Mesurement
import com.app.dr1009.phonechecker.realm.SensorValues
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DetailActivity : AppCompatActivity() {

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy/MM/dd, E, HH:mm:ss", Locale.ENGLISH)
    }

    private val mSensorManager: SensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }
    private val mSensor: Sensor by lazy { mSensorManager.getSensorList(Sensor.TYPE_ALL).first { sensor -> sensor.name == mSensorName } }
    private val mSensorName: String by lazy { intent.extras[CardPresenter.INTENT_SENSOR_NAME] as String }
    private val mAdapter: ArrayAdapter<String> by lazy { ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, mItems) }
    private val mItems = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        toolbar.title = mSensorName
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        button.setOnClickListener { onClickStartButton() }

        mItems.add(getString(R.string.item_new))
        Realm.getDefaultInstance().use { realm ->
            realm.where(Mesurement::class.java).equalTo("sensorName", mSensorName).findAll().forEach { mItems.add(it.recordDate) }
        }

        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = mAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    button.visibility = View.VISIBLE
                    chart.clear()
                    chart.invalidate()
                } else {
                    button.visibility = View.INVISIBLE
                    showChart(parent!!.selectedItem.toString())
                }
            }
        }
    }

    private fun onClickStartButton() {
        button.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        Completable.timer(1500, TimeUnit.MILLISECONDS).subscribe { measure() }
    }

    private fun measure() {
        // Get nano time for check elapsed time
        val startTime = System.nanoTime()
        // Get recordDate for record object
        val now = DATE_FORMAT.format(Calendar.getInstance().time)

        Realm.getDefaultInstance().use { realm ->
            realm.beginTransaction()
            realm.createObject(Mesurement::class.java).apply {
                sensorName = mSensorName
                recordDate = now
            }
            realm.commitTransaction()
        }

        val emitter = Observable.create<SensorEvent> {
            val sensorListener = object : SensorEventListener {
                override fun onSensorChanged(sensorEvent: SensorEvent) {
                    it.onNext(sensorEvent)
                }

                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                    // nop
                }
            }
            it.setCancellable { mSensorManager.unregisterListener(sensorListener, mSensor) }
            mSensorManager.registerListener(sensorListener, mSensor, SensorManager.SENSOR_DELAY_UI)
        }.subscribe { sensorEvent ->
            val elapsedTime = (System.nanoTime() - startTime).div(1e9.toFloat())
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()
                val sensorObject = realm.createObject(SensorValues::class.java)
                sensorObject.elapsedTime = elapsedTime
                if (sensorEvent.values.isNotEmpty()) sensorObject.valueX = sensorEvent.values[0]
                if (sensorEvent.values.size > 1) sensorObject.valueY = sensorEvent.values[1]
                if (sensorEvent.values.size > 2) sensorObject.valueZ = sensorEvent.values[2]
                realm.where(Mesurement::class.java).equalTo("recordDate", now).findFirst()!!.values.add(sensorObject)
                realm.commitTransaction()
            }
        }

        Completable.timer(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    emitter.dispose()
                    showChart(now)
                    mAdapter.add(now)
                    mAdapter.notifyDataSetChanged()
                }
    }

    private fun showChart(date: String) {
        val mesurement = Realm.getDefaultInstance().where(Mesurement::class.java).equalTo("recordDate", date).findFirst()
        if (mesurement != null) {
            val listX = mutableListOf<Entry>()
            val listY = mutableListOf<Entry>()
            val listZ = mutableListOf<Entry>()
            mesurement.values.forEach {
                if (it.valueX != Float.MIN_VALUE) listX.add(Entry(it.elapsedTime, it.valueX))
                if (it.valueY != Float.MIN_VALUE) listY.add(Entry(it.elapsedTime, it.valueY))
                if (it.valueZ != Float.MIN_VALUE) listZ.add(Entry(it.elapsedTime, it.valueZ))
            }

            val setX = LineDataSet(listX, "X").apply {
                color = Color.GREEN
                lineWidth = 2f
            }

            val setY = LineDataSet(listY, "Y").apply {
                color = Color.YELLOW
                lineWidth = 2f
            }

            val setZ = LineDataSet(listZ, "Z").apply {
                color = Color.BLUE
                lineWidth = 2f
            }

            chart.xAxis.apply {
                granularity = 1f
                valueFormatter = IAxisValueFormatter { value, _ -> "$value s" }
            }

            val lineData = LineData().apply {
                if (listX.isNotEmpty()) {
                    addDataSet(setX)
                }
                if (listY.isNotEmpty()) {
                    addDataSet(setY)
                }
                if (listZ.isNotEmpty()) {
                    addDataSet(setZ)
                }
            }

            chart.data = lineData
            chart.invalidate()
        } else {
            chart.clear()
            chart.invalidate()
        }

        progressBar.visibility = View.INVISIBLE
    }
}