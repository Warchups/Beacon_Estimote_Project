package com.gnommostudios.beacon_estimote_project.view.fragments

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
import com.gnommostudios.beacon_estimote_project.API.MyOperationalFilm

import com.gnommostudios.beacon_estimote_project.R
import com.gnommostudios.beacon_estimote_project.pojo.Favourite
import com.gnommostudios.beacon_estimote_project.pojo.MyBeacon
import java.text.DecimalFormat

class DetailsFilmFragment : Fragment(), View.OnClickListener {

    private val decimalFormat = DecimalFormat("0.00")

    private var cover: ImageView? = null

    private var searchLayout: LinearLayout? = null

    private var detailsLayout: LinearLayout? = null
    private var titleSearch: TextView? = null
    private var macSearch: TextView? = null
    private var uuidSearch: TextView? = null
    private var majorSearch: TextView? = null
    private var minorSearch: TextView? = null
    private var rssiSearch: TextView? = null
    private var distanceSearch: TextView? = null
    private var directorSearch: TextView? = null
    private var playersSearch: TextView? = null

    private var addFav: ImageView? = null

    private var mof: MyOperationalFilm? = null

    private var beacon: Beacon? = null

    private var isFavourite: Boolean = false

    private var isFilm: Boolean = false

    private var item: MyBeacon? = null

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

            titleSearch = view.findViewById<View>(R.id.title_search) as TextView
            macSearch = view.findViewById<View>(R.id.mac_search) as TextView
            uuidSearch = view.findViewById<View>(R.id.uuid_search) as TextView
            majorSearch = view.findViewById<View>(R.id.major_search) as TextView
            minorSearch = view.findViewById<View>(R.id.minor_search) as TextView
            rssiSearch = view.findViewById<View>(R.id.rssi_search) as TextView
            distanceSearch = view.findViewById<View>(R.id.distance_search) as TextView
            directorSearch = view.findViewById<View>(R.id.director_search) as TextView
            playersSearch = view.findViewById<View>(R.id.players_search) as TextView
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
                isFavourite = if (isFilm)
                    mof!!.isFilmFavourite(mof!!.searchFilm(item!!.idItem!!)!!)
                else
                    mof!!.isGameFavourite(mof!!.searchGame(item!!.idItem!!)!!)

                if (isFilm) {
                    if (!isFavourite) {
                        mof!!.addFilmFav(Favourite(item!!.idItem!!))
                        addFav!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_selected_24))
                    } else {
                        mof!!.deleteFav(mof!!.searchFilmFav(item!!.idItem!!)!!)
                        addFav!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_unselected_24))
                    }
                } else {
                    if (!isFavourite) {
                        mof!!.addGameFav(Favourite(item!!.idItem!!))
                        addFav!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_selected_24))
                    } else {
                        mof!!.deleteFav(mof!!.searchGameFav(item!!.idItem!!)!!)
                        addFav!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_unselected_24))
                    }
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

        isFilm = mof!!.isFilm(beacon.macAddress.toString())
        item = if (isFilm)
            MyBeacon(mof!!.searchFilm(beacon.macAddress.toString()), beacon)
        else
            MyBeacon(mof!!.searchGame(beacon.macAddress.toString()), beacon)

        val imageResource = resources.getIdentifier(item!!.image, null, activity.packageName)
        Glide.with(context).load(imageResource).into(cover)

        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            titleSearch!!.text = item!!.title
            macSearch!!.text = item!!.mac
            uuidSearch!!.text = item!!.uuid
            majorSearch!!.text = "Major: ${item!!.major}"
            minorSearch!!.text = "Minor: ${item!!.minor}"
            rssiSearch!!.text = "RSSI: ${item!!.rssi}"
            distanceSearch!!.text = "${decimalFormat.format(Math.pow(10.0, (beacon.measuredPower - beacon.rssi) / 20.0))}m"

            if (isFilm) {
                directorSearch!!.text = "Director: ${mof!!.searchFilm(item!!.idItem!!)!!.director!!}"
                directorSearch!!.visibility = View.VISIBLE
                playersSearch!!.visibility = View.GONE
            } else {
                playersSearch!!.text = "Players: ${mof!!.searchGame(item!!.idItem!!)!!.players!!}"
                directorSearch!!.visibility = View.GONE
                playersSearch!!.visibility = View.VISIBLE
            }

            detailsLayout!!.visibility = View.VISIBLE
        }

        searchLayout!!.visibility = View.GONE
        cover!!.visibility = View.VISIBLE
        addFav!!.visibility = View.VISIBLE

        if (isFilm) {
            if (!mof!!.isFilmFavourite(mof!!.searchFilm(item!!.idItem!!)!!))
                addFav!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_unselected_24))
            else
                addFav!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_selected_24))
        } else {
            if (!mof!!.isGameFavourite(mof!!.searchGame(item!!.idItem!!)!!))
                addFav!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_unselected_24))
            else
                addFav!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_selected_24))
        }
    }

}
