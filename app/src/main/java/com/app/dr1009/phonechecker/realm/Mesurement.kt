package com.app.dr1009.phonechecker.realm

import io.realm.RealmList
import io.realm.RealmObject

open class Mesurement(
        open var sensorName: String = "",
        open var recordDate: String = "",
        open var values: RealmList<SensorValues> = RealmList()
) : RealmObject()