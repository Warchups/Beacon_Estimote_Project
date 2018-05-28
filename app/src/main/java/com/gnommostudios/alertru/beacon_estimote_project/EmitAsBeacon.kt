package com.gnommostudios.alertru.beacon_estimote_project

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import com.sevenheaven.iosswitch.ShSwitchView
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter
import java.util.*
import java.util.Arrays.asList
import java.util.Arrays.asList
import android.bluetooth.BluetoothAdapter








class EmitAsBeacon : AppCompatActivity(), View.OnClickListener {

    private var emitPrefs: SharedPreferences? = null

    var switchOn: Switch? = null

    var txtId1: EditText? = null
    var txtId2: EditText? = null
    var txtId3: EditText? = null
    var txtPower: EditText? = null

    var spinnerAdvertisement: Spinner? = null
    var spinnerPower: Spinner? = null
    var spinnerFormat: Spinner? = null

    private val advertiseCallback = object : AdvertiseCallback() {

        fun onAdvertiseStart(result: Int) {
            if (result == BluetoothAdapter.ADVERTISE_CALLBACK_SUCCESS) {
                Log.d(FragmentActivity.TAG, "started advertising successfully.")
            } else {
                Log.d(FragmentActivity.TAG, "did not start advertising successfully")
            }

        }

        fun onAdvertiseStop(result: Int) {
            if (result == BluetoothAdapter.ADVERTISE_CALLBACK_SUCCESS) {
                Log.d(FragmentActivity.TAG, "stopped advertising successfully")
            } else {
                Log.d(FragmentActivity.TAG, "did not stop advertising successfully")
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emit_as_beacon)

        emitPrefs = getSharedPreferences("emit_as_beacon", Context.MODE_PRIVATE)

        switchOn = findViewById<View>(R.id.switch_on) as Switch
        switchOn!!.setOnClickListener(this)

        txtId1 = findViewById<View>(R.id.txt_id_1) as EditText
        txtId2 = findViewById<View>(R.id.txt_id_2) as EditText
        txtId3 = findViewById<View>(R.id.txt_id_3) as EditText
        txtPower = findViewById<View>(R.id.txt_power) as EditText

        spinnerAdvertisement = findViewById<View>(R.id.spinner_advertisements) as Spinner
        spinnerPower = findViewById<View>(R.id.spinner_power) as Spinner
        spinnerFormat = findViewById<View>(R.id.spinner_format) as Spinner
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.switch_on -> {
                if (txtId1!!.text.isEmpty() || txtId2!!.text.isEmpty() || txtId3!!.text.isEmpty() || txtPower!!.text.isEmpty()) {
                    Toast.makeText(this, "Falta algun campo por rellenar", Toast.LENGTH_SHORT).show()
                } else {
                    var id1 = txtId1!!.text.toString()
                    var id2 = txtId2!!.text.toString()
                    var id3 = txtId3!!.text.toString()
                    var power = Integer.parseInt(txtPower!!.text.toString())

                    var advertisementsPerSecond = spinnerAdvertisement!!.selectedItem.toString().split(" ")[0]
                    var powerTransmition = spinnerPower!!.selectedItem.toString()
                    var format = spinnerFormat!!.selectedItem.toString()

                    if (id1.split("-").size == 5) {
                        val beacon = Beacon.Builder()
                                .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                                .setId2("1")
                                .setId3("2")
                                .setManufacturer(0x027D) // Radius Networks.  Change this for other beacon layouts
                                .setTxPower(-59)
                                //.setDataFields(Arrays.asList(arrayOf(0L))) // Remove this for beacon layouts without d: fields
                                .build()
                        /*var beacon = Beacon.Builder()
                                .setId1(id1)
                                .setId2(id2)
                                .setId3(id3)
                                .setTxPower(power)
                                .setManufacturer(0x0118)
                                .build()*/
                        val beaconParser = BeaconParser()
                                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")
                        val beaconTransmitter = BeaconTransmitter(applicationContext, beaconParser)

                        beaconTransmitter.startAdvertising(beacon, object : AdvertiseCallback() {

                            override fun onStartFailure(errorCode: Int) {
                                Log.e("Hola", "Advertisement start failed with code: $errorCode")
                            }

                            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                                Log.i("Hola", "Advertisement start succeeded.")
                            }
                        })
                    } else {
                        Toast.makeText(this, "UUID no valida", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
}

