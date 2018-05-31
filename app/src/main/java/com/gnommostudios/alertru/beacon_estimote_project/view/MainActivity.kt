package com.gnommostudios.alertru.beacon_estimote_project.view

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
import com.gnommostudios.alertru.beacon_estimote_project.API.MyOperationalFilm
import com.gnommostudios.alertru.beacon_estimote_project.adapter.BeaconListAdapter
import com.gnommostudios.alertru.beacon_estimote_project.utils.MainBeacons
import android.content.pm.PackageManager
import android.os.Build
import android.annotation.TargetApi
import android.content.DialogInterface
import android.Manifest.permission.ACCESS_FINE_LOCATION
import com.gnommostudios.alertru.beacon_estimote_project.R
import java.util.*

class MainActivity : AppCompatActivity(), BeaconManager.BeaconRangingListener, BeaconManager.ServiceReadyCallback, View.OnClickListener {

    companion object {
        private val TAG = "RangingMainActivity"
        private const val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123
    }

    private var beaconManager: BeaconManager? = null
    private var beaconRegion: BeaconRegion? = null
    private var relativeLayout: RelativeLayout? = null
    private var listBeacons: ListView? = null

    private var floatingActionMenu: FloatingActionMenu? = null
    private var fabRefresh: FloatingActionButton? = null
    private var fabEmit: FloatingActionButton? = null
    private var fabScan: FloatingActionButton? = null

    private var beaconsList: List<Beacon>? = null

    private var mof: MyOperationalFilm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+ Permission APIs
            fuckMarshMallow()
        }

        mof = MyOperationalFilm.getInstance(this)

        relativeLayout = findViewById<View>(R.id.relative_fondo) as RelativeLayout
        listBeacons = findViewById<View>(R.id.lista_beacons) as ListView

        floatingActionMenu = findViewById<View>(R.id.floating_menu_main) as FloatingActionMenu
        fabRefresh = findViewById<View>(R.id.fab_refresh) as FloatingActionButton
        fabEmit = findViewById<View>(R.id.fab_emit) as FloatingActionButton
        fabScan = findViewById<View>(R.id.fab_scan) as FloatingActionButton

        fabRefresh!!.setOnClickListener(this)
        fabEmit!!.setOnClickListener(this)
        fabScan!!.setOnClickListener(this)

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
        /*here@for (i in beacons.indices) {
            if (Math.pow(10.0, (beacons[i].measuredPower - beacons[i].rssi) / 20.0) <= 1) {
                when (beacons[i].macAddress.toString()) {
                    MainBeacons.BLUE -> {
                        Toast.makeText(this, "Azul cerca", Toast.LENGTH_SHORT).show()
                        beaconManager!!.disconnect()
                        break@here
                    }
                    MainBeacons.PURPLE -> {
                        Toast.makeText(this, "Morado cerca", Toast.LENGTH_SHORT).show()
                        beaconManager!!.disconnect()
                        break@here
                    }
                    MainBeacons.GREEN -> {
                        Toast.makeText(this, "Verde cerca", Toast.LENGTH_SHORT).show()
                        beaconManager!!.disconnect()
                        break@here
                    }
                }
            }
        }*/

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
            R.id.fab_scan -> {
                beaconManager!!.disconnect()
                val intent = Intent(this@MainActivity, ScanFilms::class.java)
                this@MainActivity.startActivity(intent)
            }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                val perms = HashMap<String, Int>()
                // Initial
                perms[ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED


                // Fill with results
                for (i in permissions.indices)
                    perms[permissions[i]] = grantResults[i]

                // Check for ACCESS_FINE_LOCATION
                if (perms[ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    // Permission Denied
                    Toast.makeText(this@MainActivity, "All Permission GRANTED !! Thank You :)", Toast.LENGTH_SHORT)
                            .show()


                } else {
                    // Permission Denied
                    Toast.makeText(this@MainActivity, "One or More Permissions are DENIED Exiting App :(", Toast.LENGTH_SHORT)
                            .show()

                    finish()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private fun fuckMarshMallow() {
        val permissionsNeeded = ArrayList<String>()

        val permissionsList = ArrayList<String>()
        if (!addPermission(permissionsList, ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Show Location")

        if (permissionsList.size > 0) {
            if (permissionsNeeded.size > 0) {

                // Need Rationale
                var message = "App need access to " + permissionsNeeded[0]

                for (i in 1 until permissionsNeeded.size)
                    message = message + ", " + permissionsNeeded[i]

                showMessageOKCancel(message,
                        DialogInterface.OnClickListener { dialog, which ->
                            requestPermissions(permissionsList.toTypedArray(),
                                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
                        })
                return
            }
            requestPermissions(permissionsList.toTypedArray(),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
            return
        }

        Toast.makeText(this@MainActivity, "No new Permission Required- Launching App .You are Awesome!!", Toast.LENGTH_SHORT)
                .show()
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@MainActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun addPermission(permissionsList: MutableList<String>, permission: String): Boolean {

        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission)
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false
        }
        return true
    }
}