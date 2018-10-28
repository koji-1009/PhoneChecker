package com.app.dr1009.phonechecker.main

import android.content.Intent
import android.view.View
import com.app.dr1009.phonechecker.detail.DetailActivity

object CardPresenter {

    const val INTENT_SENSOR_NAME = "IntentSensorName"

    @JvmStatic
    fun onClickDetail(view: View, sensorName: String) {
        val context = view.context

        val intent = Intent(context, DetailActivity::class.java).apply {
            putExtra(INTENT_SENSOR_NAME, sensorName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
