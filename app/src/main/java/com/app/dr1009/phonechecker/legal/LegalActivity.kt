package com.app.dr1009.phonechecker.legal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.app.dr1009.phonechecker.R
import kotlinx.android.synthetic.main.activity_legal.*

class LegalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legal)

        resources.openRawResource(R.raw.legal).use {
            val byteArray = ByteArray(it.available())
            it.read(byteArray)
            textView.text = String(byteArray)
        }
    }
}
