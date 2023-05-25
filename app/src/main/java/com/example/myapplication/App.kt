package com.example.myapplication

import android.app.Application
import com.microblink.blinkid.MicroblinkSDK
import com.microblink.blinkid.intent.IntentDataTransferMode

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MicroblinkSDK.setIntentDataTransferMode(IntentDataTransferMode.PERSISTED_OPTIMISED)
        MicroblinkSDK.setLicenseKey(getString(R.string.blink_key), applicationContext)
    }

}
