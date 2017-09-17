package com.app.dr1009.phonechecker.realm

import io.realm.RealmObject

open class SensorValues(
        open var elapsedTime: Float = Float.MIN_VALUE,
        open var valueX: Float = Float.MIN_VALUE,
        open var valueY: Float = Float.MIN_VALUE,
        open var valueZ: Float = Float.MIN_VALUE
) : RealmObject()