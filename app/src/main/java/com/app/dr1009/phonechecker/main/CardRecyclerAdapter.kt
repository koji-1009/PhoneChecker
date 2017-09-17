package com.app.dr1009.phonechecker.main

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.hardware.Sensor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.dr1009.phonechecker.BR
import com.app.dr1009.phonechecker.R

class CardRecyclerAdapter(private val mContext: Context) : RecyclerView.Adapter<CardRecyclerAdapter.BindingHolder>() {

    private val mSensorList = mutableListOf<Sensor>()
    private val mSensorData = mutableMapOf<String, SensorValues>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.layout_card, parent, false)
        return BindingHolder(view)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        if (itemCount == 0) {
            return
        }

        val sensor = mSensorList[position]
        holder.binding.setVariable(BR.sensor, sensor)
        holder.binding.setVariable(BR.values, mSensorData[sensor.name])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mSensorList.size
    }

    fun updateSensor(sensor: Sensor, values: FloatArray) {
        mSensorData[sensor.name]?.setValues(values)
    }

    fun insertSensor(sensor: Sensor) {
        mSensorList.add(sensor)
        mSensorData.put(sensor.name, SensorValues())
    }

    fun clearSensors() {
        mSensorList.clear()
        mSensorData.clear()
    }

    inner class BindingHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewDataBinding = DataBindingUtil.bind(view)
    }
}

