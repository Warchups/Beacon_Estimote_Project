package com.gnommostudios.alertru.beacon_estimote_project.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.estimote.coresdk.recognition.packets.Beacon
import com.gnommostudios.alertru.beacon_estimote_project.R
import java.text.DecimalFormat

class BeaconListAdapter(context: Activity, private var elements: List<Beacon>) : ArrayAdapter<Beacon>(context , R.layout.element_list, elements) {
    internal var context: Activity = context
    var decimalFormat = DecimalFormat("0.00")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        var convertView = inflater.inflate(R.layout.element_list, null)
        val element = elements[position]

        val uuid = convertView.findViewById<View>(R.id.txt_uuid) as TextView
        val major = convertView.findViewById<View>(R.id.txt_maj) as TextView
        val minor = convertView.findViewById<View>(R.id.txt_min) as TextView
        val mac = convertView.findViewById<View>(R.id.txt_mac) as TextView
        val rssi = convertView.findViewById<View>(R.id.txt_rssi) as TextView
        val distanceTxt = convertView.findViewById<View>(R.id.txt_distance) as TextView



        uuid.text = "UUID: ${element.proximityUUID}"
        major.text = "Major: ${element.major}"
        minor.text = "Minor: ${element.minor}"
        mac.text = "M.A.C: ${element.macAddress}"
        rssi.text = "Rssi: ${element.rssi}"
        distanceTxt.text = "Distance: ${decimalFormat.format(Math.pow(10.0, (element.measuredPower - element.rssi) / 20.0))} m"

        return convertView
    }
}
