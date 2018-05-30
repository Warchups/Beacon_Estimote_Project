package com.gnommostudios.alertru.beacon_estimote_project.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import com.estimote.coresdk.observation.region.beacon.BeaconRegion
import com.estimote.coresdk.recognition.packets.Beacon
import com.estimote.coresdk.service.BeaconManager
import com.gnommostudios.alertru.beacon_estimote_project.R
import com.gnommostudios.alertru.beacon_estimote_project.utils.MainBeacons
import com.gnommostudios.alertru.beacon_estimote_project.view.fragments.DetailsFilmFragment
import java.util.*

class ScanFilms : AppCompatActivity(), BeaconManager.BeaconRangingListener, BeaconManager.ServiceReadyCallback {

    companion object {
        private val TAG = "RangingScanFilms"
    }

    private var backgroundScan: ConstraintLayout? = null

    private var beaconManager: BeaconManager? = null
    private var beaconRegion: BeaconRegion? = null

    private var beaconsList: List<Beacon>? = null

    private var frgDetail: DetailsFilmFragment? = null

    private var lastMAC: String? = null

    //BroadcastReveiver
    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.extras!!.getBoolean("REFRESH")) {
                refresh()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_films)

        backgroundScan = findViewById<View>(R.id.background_scan) as ConstraintLayout
        //fragmentDetailsFilm = findViewById<View>(R.id.fragment_details_film) as Fragment

        beaconRegion = BeaconRegion("Beacons with default Estimote UUID",
                UUID.fromString(getString(R.string.beacon_uuid)), null, null)

        beaconManager = BeaconManager(applicationContext)

        registerReceiver(mMessageReceiver, IntentFilter("ScanFilms"))
    }

    override fun onBeaconsDiscovered(beaconRegion: BeaconRegion, beacons: List<Beacon>) {

        if (beacons.isNotEmpty()) {
            var nearlyBeacon = beacons[0]

            for (i in beacons.indices) {
                val beacon = beacons[i]
                if (Math.pow(10.0, (beacon.measuredPower - beacon.rssi) / 20.0) < Math.pow(10.0, (nearlyBeacon.measuredPower - nearlyBeacon.rssi) / 20.0)) {
                    nearlyBeacon = beacon
                }
                Log.i("Estimote", beacons[i].macAddress.toString() + " - " + beacons[i].rssi)
            }

            showBeaconFilm(nearlyBeacon)
        } else {
            backgroundScan!!.setBackgroundColor(Color.WHITE)
            Log.i(TAG, "No hay beacons cerca.")
        }

        beaconsList = beacons
    }

    private fun showBeaconFilm(nearlyBeacon: Beacon) {
        if (frgDetail == null)
            frgDetail = supportFragmentManager.findFragmentById(R.id.fragment_details_film) as DetailsFilmFragment

        if (Math.pow(10.0, (nearlyBeacon.measuredPower - nearlyBeacon.rssi) / 20.0) <= 1) {
            if (lastMAC == null) {
                lastMAC = nearlyBeacon.macAddress.toString()
                frgDetail!!.showFilm(nearlyBeacon.macAddress.toString())
            }

            if (!lastMAC.equals(nearlyBeacon.macAddress.toString()))
                frgDetail!!.showFilm(nearlyBeacon.macAddress.toString())

            lastMAC = nearlyBeacon.macAddress.toString()
        } else {
            if (lastMAC == null) {
                lastMAC = MainBeacons.EMPTY
                frgDetail!!.showFilm(MainBeacons.EMPTY)
            }

            if (!lastMAC.equals(MainBeacons.EMPTY))
                frgDetail!!.showFilm(MainBeacons.EMPTY)

            lastMAC = MainBeacons.EMPTY
        }

    }

    override fun onServiceReady() {
        beaconManager!!.setRangingListener(this)

        //beaconManager!!.setBackgroundScanPeriod(3000, 3000)

        beaconManager!!.startRanging(beaconRegion)
    }

    override fun onResume() {
        beaconManager!!.connect(this)
        super.onResume()
    }

    override fun onDestroy() {
        beaconManager!!.disconnect()
        super.onDestroy()
    }

    private fun refresh() {
        beaconManager!!.disconnect()
        beaconsList = null
        beaconManager!!.connect(this)
    }
}
