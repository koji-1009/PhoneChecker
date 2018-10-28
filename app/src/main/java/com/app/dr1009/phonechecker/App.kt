package com.app.dr1009.phonechecker

import android.app.Application

import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {
    companion object {
        private const val REALM_FILE_NAME = "com.dr1009.app.phone_checker.realm"
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder().name(REALM_FILE_NAME).build()
        Realm.setDefaultConfiguration(config)
    }
}

