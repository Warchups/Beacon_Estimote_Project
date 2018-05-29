package com.gnommostudios.alertru.beacon_estimote_project

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.jaredrummler.materialspinner.MaterialSpinner
import org.altbeacon.beacon.*
import java.util.*
import android.content.DialogInterface
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.support.v7.app.AlertDialog


class EmitAsBeacon : AppCompatActivity(), View.OnClickListener {

    var switchOn: Switch? = null

    var txtId1: EditText? = null
    var txtId2: EditText? = null
    var txtId3: EditText? = null
    var txtPower: EditText? = null

    var spinnerAdvertisement: MaterialSpinner? = null
    var spinnerPower: MaterialSpinner? = null
    var spinnerFormat: MaterialSpinner? = null

    var beacon: Beacon? = null
    var beaconParser: BeaconParser? = null
    var beaconTransmitter: BeaconTransmitter? = null


    private val advertiseCallback = object : AdvertiseCallback() {

        override fun onStartFailure(errorCode: Int) {
            Log.i("Hola", "Advertisement start failed with code: $errorCode")
        }

        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            Log.i("Hola", "Advertisement start succeeded.")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emit_as_beacon)

        if (checkPrerequisites())
            Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()

        switchOn = findViewById<View>(R.id.switch_on) as Switch
        switchOn!!.setOnClickListener(this)

        txtId1 = findViewById<View>(R.id.txt_id_1) as EditText
        txtId2 = findViewById<View>(R.id.txt_id_2) as EditText
        txtId3 = findViewById<View>(R.id.txt_id_3) as EditText
        txtPower = findViewById<View>(R.id.txt_power) as EditText

        spinnerAdvertisement = findViewById<View>(R.id.spinner_advertisements) as MaterialSpinner
        spinnerPower = findViewById<View>(R.id.spinner_power) as MaterialSpinner
        spinnerFormat = findViewById<View>(R.id.spinner_format) as MaterialSpinner

        spinnerAdvertisement!!.setItems("10 (Low Latency)", "3 (Balanced)", "1 (Low Power)")
        spinnerPower!!.setItems("High", "Medium", "Low", "Ultra Low")
        spinnerFormat!!.setItems("Estimote", "AltBeacon")
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.switch_on -> {
                if (switchOn!!.isChecked) {
                    if (txtId1!!.text.isEmpty() || txtId2!!.text.isEmpty() || txtId3!!.text.isEmpty() || txtPower!!.text.isEmpty()) {
                        Toast.makeText(this, "Falta algun campo por rellenar", Toast.LENGTH_SHORT).show()
                    } else {
                        var id1 = txtId1!!.text.toString()
                        var id2 = txtId2!!.text.toString()
                        var id3 = txtId3!!.text.toString()
                        var power = Integer.parseInt(txtPower!!.text.toString())

                        var advertisementsPerSecond = spinnerAdvertisement!!.getItems<String>()[spinnerAdvertisement!!.selectedIndex].toString().split(" ")[0]
                        var powerTransmition = spinnerPower!!.getItems<String>()[spinnerAdvertisement!!.selectedIndex].toString()
                        var format = spinnerFormat!!.getItems<String>()[spinnerAdvertisement!!.selectedIndex].toString()

                        if (id1.split("-").size == 5) {
                            beacon = AltBeacon.Builder()
                                    .setId1(id1)
                                    .setId2(id2)
                                    .setId3(id3)
                                    .setManufacturer(0x004c) // Radius Networks.  Change this for other beacon layouts
                                    .setTxPower(power)
                                    .setDataFields(Arrays.asList(6L, 7L)) // Remove this for beacon layouts without d: fields
                                    .setRssi(power)
                                    .build()

                            beaconParser = BeaconParser()
                                    .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")
                            beaconTransmitter = BeaconTransmitter(applicationContext, beaconParser)

                            beaconTransmitter!!.startAdvertising(beacon, advertiseCallback)

                            val result = BeaconTransmitter.checkTransmissionSupported(this)
                            Log.i("SUPPORTED", BeaconTransmitter.SUPPORTED.toString())
                            Log.i("NOT_SUPPORTED_BLE", BeaconTransmitter.NOT_SUPPORTED_BLE.toString())
                            Log.i("NOT_SUPPORTED", BeaconTransmitter.NOT_SUPPORTED_CANNOT_GET_ADVERTISER.toString())
                            Log.i("NOT_SUPPORTED_CANNOT", BeaconTransmitter.NOT_SUPPORTED_CANNOT_GET_ADVERTISER_MULTIPLE_ADVERTISEMENTS.toString())
                            Log.i("NOT_SUPPORTED_MIN_SDK", BeaconTransmitter.NOT_SUPPORTED_MIN_SDK.toString())
                            Log.i("OOO", result.toString())
                        } else {
                            Toast.makeText(this, "UUID no valida", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    beaconTransmitter!!.stopAdvertising()
                }
            }
        }
    }

    private fun checkPrerequisites(): Boolean {

        if (android.os.Build.VERSION.SDK_INT < 18) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Bluetooth LE not supported by this device's operating system")
            builder.setMessage("You will not be able to transmit as a Beacon")
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener(DialogInterface.OnDismissListener { finish() })
            builder.show()
            return false
        }
        if (!applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Bluetooth LE not supported by this device")
            builder.setMessage("You will not be able to transmit as a Beacon")
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener(DialogInterface.OnDismissListener { finish() })
            builder.show()
            return false
        }
        if (!(applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter.isEnabled) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Bluetooth not enabled")
            builder.setMessage("Please enable Bluetooth and restart this app.")
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener(DialogInterface.OnDismissListener { finish() })
            builder.show()
            return false

        }

        try {
            // Check to see if the getBluetoothLeAdvertiser is available.  If not, this will throw an exception indicating we are not running Android L
            (this.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter.bluetoothLeAdvertiser
        } catch (e: Exception) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Bluetooth LE advertising unavailable")
            builder.setMessage("Sorry, the operating system on this device does not support Bluetooth LE advertising.  As of July 2014, only the Android L preview OS supports this feature in user-installed apps.")
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener(DialogInterface.OnDismissListener { finish() })
            builder.show()
            return false

        }

        return true
    }

}
 /*, object : AdvertiseCallback() {

                                override fun onStartFailure(errorCode: Int) {
                                    Log.i("Hola", "Advertisement start failed with code: $errorCode")
                                }

                                override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                                    Log.i("Hola", "Advertisement start succeeded.")
                                }
                            }*/
