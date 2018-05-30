package com.gnommostudios.alertru.beacon_estimote_project

import android.app.Application
import android.content.Context

import com.estimote.coresdk.common.config.EstimoteSDK

import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.powersave.BackgroundPowerSaver

class MyApplication : Application() {

    var beaconManager: BeaconManager? = null
    var backgroundPowerSaver: BackgroundPowerSaver? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        EstimoteSDK.initialize(this, "christian-sellent-hotmail--hcu", "6492859b557684e4ec0cb3a212a55c42")

        beaconManager = BeaconManager.getInstanceForApplication(this)
        backgroundPowerSaver = BackgroundPowerSaver(this)
    }

    companion object {
        var context: Context? = null
        fun getAppContext(): Context = context!!
    }
}
