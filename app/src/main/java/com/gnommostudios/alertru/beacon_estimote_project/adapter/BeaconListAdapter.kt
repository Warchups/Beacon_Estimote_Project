package com.gnommostudios.alertru.beacon_estimote_project.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.estimote.coresdk.recognition.packets.Beacon
import com.gnommostudios.alertru.beacon_estimote_project.API.MyOperationalFilm
import com.gnommostudios.alertru.beacon_estimote_project.R
import com.gnommostudios.alertru.beacon_estimote_project.pojo.MyBeacon

import java.text.DecimalFormat

class BeaconListAdapter(private val context: Activity, private val elements: ArrayList<MyBeacon>, private val mof: MyOperationalFilm) : ArrayAdapter<MyBeacon>(context, R.layout.element_list_beacon, elements) {

    private val decimalFormat = DecimalFormat("0.00")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView: View?
        val inflater = context.layoutInflater
        convertView = inflater.inflate(R.layout.element_list_beacon, null)
        val element = elements[position]

        val uuid = convertView!!.findViewById<TextView>(R.id.uuid_main)
        val major = convertView.findViewById<TextView>(R.id.major_main)
        val minor = convertView.findViewById<TextView>(R.id.minor_main)
        val mac = convertView.findViewById<TextView>(R.id.mac_main)
        val rssi = convertView.findViewById<TextView>(R.id.rssi_main)
        val distanceTxt = convertView.findViewById<TextView>(R.id.distance_main)
        val cover = convertView.findViewById<ImageView>(R.id.image_main)
        val title = convertView.findViewById<TextView>(R.id.title_main)
        val favourite = convertView.findViewById<ImageView>(R.id.fav_main)

        if (mof.isFavourite(mof.searchFilm(element.mac!!)))
            favourite.setImageDrawable(context.resources.getDrawable(R.drawable.baseline_star_selected_24))
        else
            favourite.setImageDrawable(context.resources.getDrawable(R.drawable.baseline_star_unselected_24))

        val imageResource = context.resources.getIdentifier(element!!.image, null, context.packageName)
        Glide.with(context).load(imageResource).into(cover)
        title.text = element.title
        uuid.text = element.uuid
        major.text = "Major: ${element.major}"
        minor.text = "Minor: ${element.minor}"
        mac.text = "M.A.C.: ${element.mac}"
        rssi.text = "Rssi: ${element.rssi}"
        distanceTxt.text = "Distance: ${decimalFormat.format(element.distance)}m"

        return convertView
    }

    fun updateBeaconList(beacons: List<MyBeacon>) {
        elements.clear()
        elements.addAll(beacons)
        notifyDataSetChanged()
    }
}
