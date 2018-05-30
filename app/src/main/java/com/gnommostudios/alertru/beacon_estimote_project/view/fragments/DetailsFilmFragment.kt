package com.gnommostudios.alertru.beacon_estimote_project.view.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.gnommostudios.alertru.beacon_estimote_project.API.MiPeliculaOperacional

import com.gnommostudios.alertru.beacon_estimote_project.R
import com.gnommostudios.alertru.beacon_estimote_project.utils.MainBeacons

class DetailsFilmFragment : Fragment(), View.OnClickListener {

    var cover: ImageView? = null
    var searchLayout: LinearLayout? = null

    private var mpo: MiPeliculaOperacional? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_details_film, container, false) as View

        mpo = MiPeliculaOperacional.getInstance(context)

        cover = view.findViewById<View>(R.id.cover) as ImageView
        searchLayout = view.findViewById<View>(R.id.search_layout) as LinearLayout

        searchLayout!!.setOnClickListener(this)

        searchLayout!!.visibility = View.VISIBLE
        cover!!.visibility = View.GONE

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
        }
    }

    fun showFilm(beaconMAC: String) {
        if (beaconMAC != MainBeacons.EMPTY) {
            var film = mpo!!.search(beaconMAC)
            Glide.with(context).load(film.imagen).asBitmap().into(cover)

            searchLayout!!.visibility = View.GONE
            cover!!.visibility = View.VISIBLE
        } else {
            searchLayout!!.visibility = View.VISIBLE
            cover!!.visibility = View.GONE
        }
    }


}
