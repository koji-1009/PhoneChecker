package com.app.dr1009.phonechecker

import android.databinding.BaseObservable
import android.databinding.Bindable

class SensorValues : BaseObservable() {

    @get:Bindable
    var x = Float.MIN_VALUE
        set(value) {
            field = value
            isShowX = true
            notifyPropertyChanged(BR.x)
            notifyPropertyChanged(BR.showX)
        }

    @get:Bindable
    var isShowX = false

    @get:Bindable
    var y = Float.MIN_VALUE
        set(value) {
            field = value
            isShowY = true
            notifyPropertyChanged(BR.y)
            notifyPropertyChanged(BR.showY)
        }

    @get:Bindable
    var isShowY = false

    @get:Bindable
    var z = Float.MIN_VALUE
        set(value) {
            field = value
            isShowZ = true
            notifyPropertyChanged(BR.z)
            notifyPropertyChanged(BR.showZ)
        }

    @get:Bindable
    var isShowZ = false

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
