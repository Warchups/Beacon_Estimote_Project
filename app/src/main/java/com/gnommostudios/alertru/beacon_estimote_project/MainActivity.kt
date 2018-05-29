package com.gnommostudios.alertru.beacon_estimote_project

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.Toast

import com.estimote.coresdk.observation.region.beacon.BeaconRegion
import com.estimote.coresdk.recognition.packets.Beacon
import com.estimote.coresdk.service.BeaconManager
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.gnommostudios.alertru.beacon_estimote_project.API.MiPeliculaOperacional
import com.gnommostudios.alertru.beacon_estimote_project.adapter.BeaconListAdapter
import com.gnommostudios.alertru.beacon_estimote_project.utils.MainBeacons

class MainActivity : AppCompatActivity(), BeaconManager.BeaconRangingListener, BeaconManager.ServiceReadyCallback, View.OnClickListener {

    companion object {
        protected val TAG = "RangingMainActivity"
    }

    private var beaconManager: BeaconManager? = null
    private var beaconRegion: BeaconRegion? = null
    private var relativeLayout: RelativeLayout? = null
    private var listBeacons: ListView? = null
    private var floatingActionMenu: FloatingActionMenu? = null
    private var fabRefresh: FloatingActionButton? = null
    private var fabEmit: FloatingActionButton? = null

    private var beaconsList: List<Beacon>? = null

    private var miPeliculaOperacional: MiPeliculaOperacional? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        miPeliculaOperacional = MiPeliculaOperacional.getInstance(this)

        relativeLayout = findViewById<View>(R.id.relative_fondo) as RelativeLayout
        listBeacons = findViewById<View>(R.id.lista_beacons) as ListView
        floatingActionMenu = findViewById<View>(R.id.floating_menu_main) as FloatingActionMenu
        fabRefresh = findViewById<View>(R.id.fab_refresh) as FloatingActionButton
        fabEmit = findViewById<View>(R.id.fab_emit) as FloatingActionButton

        fabRefresh!!.setOnClickListener(this)
        fabEmit!!.setOnClickListener(this)

        //verifyBluetooth()

        beaconRegion = BeaconRegion("Beacons with default Estimote UUID",
                null, null, null)
        //UUID.fromString(getString(R.string.beacon_uuid))
        beaconManager = BeaconManager(applicationContext)
    }

    override fun onBeaconsDiscovered(beaconRegion: BeaconRegion, beacons: List<Beacon>) {
        if (beaconsList == null) {
            Log.i("Hola", "Es la primera")
            beaconsList = beacons
            listBeacons!!.adapter = BeaconListAdapter(this@MainActivity, beaconsList!!)
        } else {
            this@MainActivity.runOnUiThread {
                var adapter = listBeacons!!.adapter as BeaconListAdapter
                //adapter!!.setBeacons(beaconsList!!)
                adapter!!.updateBeaconList(beaconsList!!)
                Log.i("Hola", "No es la primera")
            }
        }

        if (beacons.isNotEmpty()) {
            var nearlyBeacon = beacons[0]

            for (i in beacons.indices) {
                val beacon = beacons[i]
                if (Math.pow(10.0, (beacon.measuredPower - beacon.rssi) / 20.0) < Math.pow(10.0, (nearlyBeacon.measuredPower - nearlyBeacon.rssi) / 20.0)) {
                    nearlyBeacon = beacon
                }
                Log.i("Estimote", beacons[i].macAddress.toString() + " - " + beacons[i].rssi)
            }

            when (nearlyBeacon.macAddress.toString()) {
                MainBeacons.BLUE ->
                    relativeLayout!!.setBackgroundColor(Color.parseColor("#1ccef2"))
                MainBeacons.PURPLE ->
                    relativeLayout!!.setBackgroundColor(Color.parseColor("#82039a"))
                MainBeacons.GREEN ->
                    relativeLayout!!.setBackgroundColor(Color.parseColor("#d0e0b8"))
                else -> relativeLayout!!.setBackgroundColor(Color.BLACK)
            }

            showBeaconFilms(beacons)
        } else {
            relativeLayout!!.setBackgroundColor(Color.WHITE)
            Log.i(TAG, "No hay beacons cerca.")
        }

        beaconsList = beacons
    }

    private fun showBeaconFilms(beacons: List<Beacon>) {
        here@for (i in beacons.indices) {
            if (Math.pow(10.0, (beacons[i].measuredPower - beacons[i].rssi) / 20.0) <= 1) {
                when (beacons[i].macAddress.toString()) {
                    MainBeacons.BLUE -> {
                        Toast.makeText(this, "Azul cerca", Toast.LENGTH_SHORT).show()
                        break@here
                    }
                    MainBeacons.PURPLE -> {
                        Toast.makeText(this, "Morado cerca", Toast.LENGTH_SHORT).show()
                        break@here
                    }
                    MainBeacons.GREEN -> {
                        Toast.makeText(this, "Verde cerca", Toast.LENGTH_SHORT).show()
                        break@here
                    }
                }
            }
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
                beaconManager!!.disconnect()
                val intent = Intent(this@MainActivity, EmitAsBeacon::class.java)
                this@MainActivity.startActivity(intent)
            }
            R.id.fab_refresh -> refresh()
        }
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

    private fun verifyBluetooth() {

        try {
            if (!org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Bluetooth not enabled")
                builder.setMessage("Please enable bluetooth in settings and restart this application.")
                builder.setPositiveButton(android.R.string.ok, null)
                builder.setOnDismissListener {
                    finish()
                    System.exit(0)
                }
                builder.show()
            }
        } catch (e: RuntimeException) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Bluetooth LE not available")
            builder.setMessage("Sorry, this device does not support Bluetooth LE.")
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener {
                finish()
                System.exit(0)
            }
            builder.show()

        }

    }
}