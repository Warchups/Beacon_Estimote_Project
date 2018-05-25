package com.gnommostudios.alertru.beacon_estimote_project

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.sevenheaven.iosswitch.ShSwitchView
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser

class EmitAsBeacon : AppCompatActivity(), View.OnClickListener {

    private var emitPrefs: SharedPreferences? = null

    var switchOn: ShSwitchView? = null

    var txtId1: EditText? = null
    var txtId2: EditText? = null
    var txtId3: EditText? = null
    var txtPower: EditText? = null

    var spinnerAdvertisement: Spinner? = null
    var spinnerPower: Spinner? = null
    var spinnerFormat: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emit_as_beacon)

        emitPrefs = getSharedPreferences("emit_as_beacon", Context.MODE_PRIVATE)

        switchOn = findViewById<View>(R.id.switch_on) as ShSwitchView
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
                        var beacon = Beacon.Builder()
                                .setId1(id1)
                                .setId2(id2)
                                .setId3(id3)
                                .setTxPower(power)
                                .build()
                        var beaconParser = BeaconParser.BeaconLa
                    } else {
                        Toast.makeText(this, "UUID no valida", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
}
