package com.app.dr1009.phonechecker

import android.content.Intent
import android.view.View

object CardPresenter {

    val INTENT_SENSOR_NAME = "IntentSensorName"

    @JvmStatic
    fun onClickDetail(view: View, sensorName: String) {
        val context = view.context

        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(INTENT_SENSOR_NAME, sensorName)
        context.startActivity(intent)
    }
}
