package com.gnommostudios.alertru.beacon_estimote_project.view.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.estimote.coresdk.recognition.packets.Beacon
import com.gnommostudios.alertru.beacon_estimote_project.API.MyOperationalFilm

import com.gnommostudios.alertru.beacon_estimote_project.R
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Film
import java.text.DecimalFormat

class DetailsFilmFragment : Fragment(), View.OnClickListener {

    private val decimalFormat = DecimalFormat("0.00")

    private var cover: ImageView? = null

    private var searchLayout: LinearLayout? = null

    private var detailsLayout: LinearLayout? = null
    private var titleFilm: TextView? = null
    private var macFilm: TextView? = null
    private var uuidFilm: TextView? = null
    private var majorFilm: TextView? = null
    private var minorFilm: TextView? = null
    private var rssiFilm: TextView? = null
    private var distanceFilm: TextView? = null

    private var addFav: ImageView? = null

    private var mof: MyOperationalFilm? = null

    private var beacon: Beacon? = null

    private var isFavourite: Boolean = false

    private var film: Film? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_details_film, container, false) as View

        mof = MyOperationalFilm.getInstance(context)

        cover = view.findViewById<View>(R.id.cover) as ImageView
        searchLayout = view.findViewById<View>(R.id.search_layout) as LinearLayout
        addFav = view.findViewById<View>(R.id.add_fav) as ImageView
        addFav!!.setOnClickListener(this)

        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            detailsLayout = view.findViewById<View>(R.id.details_layout) as LinearLayout
            detailsLayout!!.visibility = View.GONE

            titleFilm = view.findViewById<View>(R.id.title_film) as TextView
            macFilm = view.findViewById<View>(R.id.mac_film) as TextView
            uuidFilm = view.findViewById<View>(R.id.uuid_film) as TextView
            majorFilm = view.findViewById<View>(R.id.major_film) as TextView
            minorFilm = view.findViewById<View>(R.id.minor_film) as TextView
            rssiFilm = view.findViewById<View>(R.id.rssi_film) as TextView
            distanceFilm = view.findViewById<View>(R.id.distance_film) as TextView
        }

        searchLayout!!.setOnClickListener(this)

        searchLayout!!.visibility = View.VISIBLE
        cover!!.visibility = View.GONE
        addFav!!.visibility = View.GONE

        return view
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.search_layout -> {
                val intent = Intent("ScanFilms")
                intent.putExtra("REFRESH", true)
                //send broadcast
                context.applicationContext.sendBroadcast(intent)
            }
            R.id.add_fav -> {
                isFavourite = mof!!.isFavourite(film!!)

                if (!isFavourite) {
                    mof!!.addFav(beacon!!)
                    addFav!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_selected_24))
                } else {
                    mof!!.deleteFav(mof!!.searchFav(film!!.id)!!)
                    addFav!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_unselected_24))
                }
            }
        }
    }

    fun showFilm() {
        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            detailsLayout!!.visibility = View.GONE
        }

        searchLayout!!.visibility = View.VISIBLE
        cover!!.visibility = View.GONE
        addFav!!.visibility = View.GONE
    }

    fun showFilm(beacon: Beacon) {
        this.beacon = beacon

        film = mof!!.searchFilm(beacon.macAddress.toString())
        val imageResource = resources.getIdentifier(film!!.image, null, activity.packageName)
        Glide.with(context).load(imageResource).into(cover)

        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            titleFilm!!.text = film!!.title
            macFilm!!.text = beacon.macAddress.toString()
            uuidFilm!!.text = beacon.proximityUUID.toString()
            majorFilm!!.text = "Major: ${beacon.major}"
            minorFilm!!.text = "Minor: ${beacon.minor}"
            rssiFilm!!.text = "RSSI: ${beacon.rssi}"
            distanceFilm!!.text = "${decimalFormat.format(Math.pow(10.0, (beacon.measuredPower - beacon.rssi) / 20.0))}m"

            detailsLayout!!.visibility = View.VISIBLE
        }

        searchLayout!!.visibility = View.GONE
        cover!!.visibility = View.VISIBLE
        addFav!!.visibility = View.VISIBLE

        if (!mof!!.isFavourite(film!!))
            addFav!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_unselected_24))
        else
            addFav!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_selected_24))
    }

}
