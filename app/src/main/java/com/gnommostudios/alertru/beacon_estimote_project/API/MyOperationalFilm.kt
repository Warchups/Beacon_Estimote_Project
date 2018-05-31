package com.gnommostudios.alertru.beacon_estimote_project.API

import android.content.Context

import com.gnommostudios.alertru.beacon_estimote_project.bd.MiBD
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Film

import java.util.ArrayList

class MyOperationalFilm private constructor(context: Context) {

    private val miBD: MiBD = MiBD.getInstance(context)

    val all: ArrayList<Film>
        get() = miBD.getFilmDAO()!!.all as ArrayList<Film>

    fun insertFilm(film: Film) {
        miBD.insertFilm(film)
    }

    fun delete(film: Film) {
        miBD.deleteFilm(film)
    }

    fun search(mac: String): Film {
        val film = Film()
        film.beaconMac = mac
        return miBD.getFilmDAO()!!.search(film) as Film
    }

    companion object {

        private var instance: MyOperationalFilm? = null

        fun getInstance(context: Context): MyOperationalFilm {
            if (instance == null)
                instance = MyOperationalFilm(context)

            return instance!!
        }
    }
}
