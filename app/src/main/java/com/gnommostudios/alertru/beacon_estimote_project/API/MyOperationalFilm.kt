package com.gnommostudios.alertru.beacon_estimote_project.API

import android.content.Context
import android.util.Log
import com.estimote.coresdk.recognition.packets.Beacon

import com.gnommostudios.alertru.beacon_estimote_project.bd.MiBD
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Favourite
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Film

import java.util.ArrayList

class MyOperationalFilm private constructor(context: Context) {

    private val miBD: MiBD = MiBD.getInstance(context)

    val allFilms: ArrayList<Film>
        get() = miBD.getFilmDAO()!!.allFilms as ArrayList<Film>

    fun insertFilm(film: Film) {
        miBD.insertFilm(film)
    }

    fun deleteFilm(film: Film) {
        miBD.deleteFilm(film)
    }

    fun searchFilm(mac: String): Film {
        val film = Film()
        film.beaconMac = mac
        return miBD.getFilmDAO()!!.searchFilm(film)
    }

    val allFavs: ArrayList<Favourite>
        get() = miBD.getFilmDAO()!!.allFavs as ArrayList<Favourite>

    fun addFav(beacon: Beacon) {
        var film = searchFilm(beacon.macAddress.toString())

        if (film != null) {
            var favourite = Favourite(film.id, beacon.proximityUUID.toString(), beacon.major, beacon.minor, beacon.macAddress.toString())

            if (!miBD.getFilmDAO()!!.existFav(film)) {
                miBD.insertFav(favourite)
            }
        }
    }

    fun addFav(fav: Favourite) {
        var film = searchFilm(fav.mac!!)

        if (film != null) {
            var favourite = Favourite(film.id, fav.uuid, fav.major, fav.minor, fav.mac)

            if (!miBD.getFilmDAO()!!.existFav(film)) {
                miBD.insertFav(favourite)
            }
        }
    }

    fun searchFav(film: Int): Favourite? {
        var fav = Favourite()
        fav.film = film
        return miBD.getFilmDAO()!!.searchFav(fav)
    }

    fun isFavourite(film: Film): Boolean = miBD.getFilmDAO()!!.existFav(film)

    fun deleteFav(fav: Favourite) {
        miBD.deleteFav(fav)
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
