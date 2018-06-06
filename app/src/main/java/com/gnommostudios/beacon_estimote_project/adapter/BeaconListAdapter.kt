package com.gnommostudios.beacon_estimote_project.adapter

import android.app.Activity
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.gnommostudios.beacon_estimote_project.API.MyOperationalFilm
import com.gnommostudios.beacon_estimote_project.R
import com.gnommostudios.beacon_estimote_project.pojo.MyBeacon

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
        val director = convertView!!.findViewById<TextView>(R.id.director_main)
        val players = convertView.findViewById<TextView>(R.id.players_main)

        if (mof.isFilm(element.mac!!)) {
            writeSpannable("Director: ", mof!!.searchFilm(element.idItem!!)!!.director!!, director)
            director.visibility = View.VISIBLE
            players.visibility = View.GONE
            if (mof.isFilmFavourite(mof.searchFilm(element.mac!!)!!))
                favourite.setImageDrawable(context.resources.getDrawable(R.drawable.baseline_star_selected_24))
            else
                favourite.setImageDrawable(context.resources.getDrawable(R.drawable.baseline_star_unselected_24))
        } else {
            writeSpannable("Players: ", mof!!.searchGame(element.idItem!!)!!.players!!.toString(), players)
            director.visibility = View.GONE
            players.visibility = View.VISIBLE
            if (mof.isGameFavourite(mof.searchGame(element.mac!!)!!))
                favourite.setImageDrawable(context.resources.getDrawable(R.drawable.baseline_star_selected_24))
            else
                favourite.setImageDrawable(context.resources.getDrawable(R.drawable.baseline_star_unselected_24))
        }

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

    private fun writeSpannable(headBoard: String, content: String, textView: TextView) {
        val builder = SpannableStringBuilder()

        val headBoardSpannable = SpannableString(headBoard)
        headBoardSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, headBoard.length, 0)
        builder.append(headBoardSpannable)

        val contentSpannable = SpannableString(content)
        contentSpannable.setSpan(null, 0, content!!.length, 0)
        builder.append(contentSpannable)

        textView!!.setText(builder, TextView.BufferType.SPANNABLE)
    }

    fun updateBeaconList(beacons: List<MyBeacon>) {
        elements.clear()
        elements.addAll(beacons)
        notifyDataSetChanged()
    }
}
