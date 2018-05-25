package com.gnommostudios.alertru.beacon_estimote_project

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.RelativeLayout

import com.estimote.coresdk.common.config.EstimoteSDK
import com.estimote.coresdk.observation.region.beacon.BeaconRegion
import com.estimote.coresdk.recognition.packets.Beacon
import com.estimote.coresdk.service.BeaconManager
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.gnommostudios.alertru.beacon_estimote_project.adapter.BeaconListAdapter
import java.util.UUID

class MainActivity : AppCompatActivity(), BeaconManager.BeaconRangingListener, BeaconManager.ServiceReadyCallback, View.OnClickListener {

    companion object {
        protected val TAG = "MonitoringActivity"
    }

    private var beaconManager: BeaconManager? = null
    private var beaconRegion: BeaconRegion? = null
    private var relativeLayout: RelativeLayout? = null
    private var listaBeacons: ListView? = null
    private var floatingActionMenu: FloatingActionMenu? = null
    private var fabRefresh: FloatingActionButton? = null
    private var fabEmit: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        relativeLayout = findViewById<View>(R.id.relative_fondo) as RelativeLayout
        listaBeacons = findViewById<View>(R.id.lista_beacons) as ListView
        floatingActionMenu = findViewById<View>(R.id.floating_menu_main) as FloatingActionMenu
        fabRefresh = findViewById<View>(R.id.fab_refresh) as FloatingActionButton
        fabEmit = findViewById<View>(R.id.fab_emit) as FloatingActionButton

        fabRefresh!!.setOnClickListener(this)
        fabEmit!!.setOnClickListener(this)

        EstimoteSDK.initialize(this, "christian-sellent-hotmail--hcu", "6492859b557684e4ec0cb3a212a55c42")

        beaconRegion = BeaconRegion("Beacons with default Estimote UUID",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null)

        beaconManager = BeaconManager(applicationContext)

        beaconManager!!.connect(this)
    }

    override fun onBeaconsDiscovered(beaconRegion: BeaconRegion, beacons: List<Beacon>) {
        this@MainActivity.runOnUiThread { listaBeacons!!.adapter = BeaconListAdapter(this@MainActivity, beacons) }

        if (beacons.isNotEmpty()) {
            var nearlyBeacon = beacons[0]

            for (i in beacons.indices) {
                val beacon = beacons[i]
                if (Math.pow(10.0, (beacon.measuredPower - beacon.rssi) / 20.0) < Math.pow(10.0, (nearlyBeacon.measuredPower - nearlyBeacon.rssi) / 20.0)) {
                    nearlyBeacon = beacon
                }
                Log.i("Estimote", beacons[i].macAddress.toString() + "")
            }

            when (nearlyBeacon.macAddress.toString()) {
                "[E6:99:6A:7A:39:CA]" -> //Azul
                    relativeLayout!!.setBackgroundColor(Color.parseColor("#1ccef2"))
                "[F4:50:04:B3:FA:B4]" -> //Morao
                    relativeLayout!!.setBackgroundColor(Color.parseColor("#82039a"))
                "[D3:94:A3:0B:22:C3]" -> //Verde
                    relativeLayout!!.setBackgroundColor(Color.parseColor("#d0e0b8"))
                else -> relativeLayout!!.setBackgroundColor(Color.BLACK)
            }

            this@MainActivity.runOnUiThread {
                var adapter = listaBeacons!!.adapter as BeaconListAdapter
                adapter.notifyDataSetChanged()
            }

        } else {
            this@MainActivity.runOnUiThread {
                var adapter = listaBeacons!!.adapter as BeaconListAdapter
                adapter.notifyDataSetChanged()
            }

            relativeLayout!!.setBackgroundColor(Color.WHITE)
            Log.i(TAG, "No hay estimotes cerca.")
        }
    }

    override fun onServiceReady() {
        beaconManager!!.setRangingListener(this)

        //beaconManager!!.setBackgroundScanPeriod(3000, 3000)

        beaconManager!!.startRanging(beaconRegion)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fab_emit -> {
                val intent = Intent(this@MainActivity, EmitAsBeacon::class.java)
                this@MainActivity.startActivity(intent)
            }
            R.id.fab_refresh -> {
                refresh()
            }
        }
    }

    private fun refresh() {
        beaconManager!!.disconnect()
        beaconManager!!.connect(this)
    }
}