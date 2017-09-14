package com.app.dr1009.phonechecker

import android.databinding.BaseObservable
import android.databinding.Bindable

class SensorValues : BaseObservable() {

    private var x = Float.MIN_VALUE
        set(value) {
            field = value
            isShowX = true
            notifyPropertyChanged(BR.showX)
            notifyPropertyChanged(BR.formatX)
        }

    @get:Bindable
    var isShowX = false

    @Bindable
    fun getFormatX() = String.format("%+.3f", x)

    private var y = Float.MIN_VALUE
        set(value) {
            field = value
            isShowY = true
            notifyPropertyChanged(BR.showY)
            notifyPropertyChanged(BR.formatY)
        }

    @get:Bindable
    var isShowY = false

    @Bindable
    fun getFormatY() = String.format("%+.3f", y)

    private var z = Float.MIN_VALUE
        set(value) {
            field = value
            isShowZ = true
            notifyPropertyChanged(BR.showZ)
            notifyPropertyChanged(BR.formatZ)
        }

    @get:Bindable
    var isShowZ = false

    @Bindable
    fun getFormatZ() = String.format("%+.3f", z)

    fun setValues(array: FloatArray) {
        when (array.size) {
            1 -> {
                x = array[0]
            }
            2 -> {
                x = array[0]
                y = array[1]
            }
            3 -> {
                x = array[0]
                y = array[1]
                z = array[2]
            }
        }
    }

}
