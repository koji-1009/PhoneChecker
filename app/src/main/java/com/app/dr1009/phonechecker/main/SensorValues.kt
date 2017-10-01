package com.app.dr1009.phonechecker.main

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import com.app.dr1009.phonechecker.BR

class SensorValues : BaseObservable() {

    private var x = Float.MIN_VALUE
        set(value) {
            field = value
            notifyPropertyChanged(BR.visibilityX)
            notifyPropertyChanged(BR.formatX)
        }

    @Bindable
    fun getVisibilityX() = if (x == Float.MIN_VALUE) View.GONE else View.VISIBLE

    @Bindable
    fun getFormatX() = String.format("%+.3f", x)

    private var y = Float.MIN_VALUE
        set(value) {
            field = value
            notifyPropertyChanged(BR.visibilityY)
            notifyPropertyChanged(BR.formatY)
        }

    @Bindable
    fun getVisibilityY() = if (y == Float.MIN_VALUE) View.GONE else View.VISIBLE

    @Bindable
    fun getFormatY() = String.format("%+.3f", y)

    private var z = Float.MIN_VALUE
        set(value) {
            field = value
            notifyPropertyChanged(BR.visibilityZ)
            notifyPropertyChanged(BR.formatZ)
        }

    @Bindable
    fun getVisibilityZ() = if (z == Float.MIN_VALUE) View.GONE else View.VISIBLE

    @Bindable
    fun getFormatZ() = String.format("%+.3f", z)

    fun setValues(array: FloatArray) {
        array.forEachIndexed { index, value ->
            when (index) {
                0 -> x = value
                1 -> y = value
                2 -> z = value
            }
        }
    }

}
