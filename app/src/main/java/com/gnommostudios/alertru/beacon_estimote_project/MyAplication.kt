package com.gnommostudios.alertru.beacon_estimote_project

import android.app.Application
import com.estimote.coresdk.common.config.EstimoteSDK

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //  To get your AppId and AppToken you need to create new application in Estimote Cloud.
        EstimoteSDK.initialize(applicationContext, "christian-sellent-hotmail--hcu", "6492859b557684e4ec0cb3a212a55c42")
        // Optional, debug logging.
        //EstimoteSDK.enableDebugLogging(true)
    }
}