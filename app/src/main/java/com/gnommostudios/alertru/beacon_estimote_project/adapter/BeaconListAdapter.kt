package com.gnommostudios.alertru.beacon_estimote_project.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.estimote.coresdk.recognition.packets.Beacon
import com.gnommostudios.alertru.beacon_estimote_project.R

import java.text.DecimalFormat

class BeaconListAdapter(private val context: Activity, private val elements: MutableList<Beacon>) : ArrayAdapter<Beacon>(context, R.layout.element_list, elements) {

    private val decimalFormat = DecimalFormat("0.00")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView: View?
        val inflater = context.layoutInflater
        convertView = inflater.inflate(R.layout.element_list, null)
        val element = elements[position]

        val uuid = convertView!!.findViewById<TextView>(R.id.txt_uuid)
        val major = convertView.findViewById<TextView>(R.id.txt_maj)
        val minor = convertView.findViewById<TextView>(R.id.txt_min)
        val mac = convertView.findViewById<TextView>(R.id.txt_mac)
        val rssi = convertView.findViewById<TextView>(R.id.txt_rssi)
        val distanceTxt = convertView.findViewById<TextView>(R.id.txt_distance)

        uuid.text = "UUID: " + element.proximityUUID
        major.text = "Major: " + element.major
        minor.text = "Minor: " + element.minor
        mac.text = "M.A.C.: " + element.macAddress
        rssi.text = "Rssi: " + element.rssi
        distanceTxt.text = "Distance: " + decimalFormat.format(Math.pow(10.0, (element.measuredPower - element.rssi) / 20.0)) + "m"

        return convertView
    }

    fun updateBeaconList(beacons: List<Beacon>) {
        elements.clear()
        elements.addAll(beacons)
        notifyDataSetChanged()
    }
}
