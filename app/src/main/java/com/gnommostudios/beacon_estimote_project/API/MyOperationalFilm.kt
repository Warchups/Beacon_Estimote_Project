package com.gnommostudios.beacon_estimote_project.API

import android.content.Context
import com.estimote.coresdk.recognition.packets.Beacon

import com.gnommostudios.beacon_estimote_project.bd.MiBD
import com.gnommostudios.beacon_estimote_project.pojo.Favourite
import com.gnommostudios.beacon_estimote_project.pojo.Film
import com.gnommostudios.beacon_estimote_project.pojo.VideoGame

import java.util.ArrayList

class MyOperationalFilm private constructor(context: Context) {

    private val miBD: MiBD = MiBD.getInstance(context)

    val allFilms: ArrayList<Film>
        get() = miBD.getFilmDAO()!!.all as ArrayList<Film>

    fun insertFilm(film: Film) {
        miBD.insertFilm(film)
    }

    fun deleteFilm(film: Film) {
        miBD.deleteFilm(film)
    }

    fun searchFilm(mac: String): Film? {
        val film = Film()
        film.id = mac
        return miBD.getFilmDAO()!!.search(film)
    }

    fun searchGame(mac: String): VideoGame? {
        val game = VideoGame()
        game.id = mac
        return miBD.getGameDAO()!!.search(game)
    }

    fun searchGameFav(game: String): Favourite? {
        var fav = Favourite()
        fav.item = game
        return miBD.getGameDAO()!!.searchFav(fav)
    }

    fun addGameFav(beacon: Beacon) {
        var game = searchGame(beacon.macAddress.toString())

        if (game != null) {
            var favourite = Favourite(game.id)

            if (!miBD.getGameDAO()!!.existFav(game)) {
                miBD.insertFav(favourite)
            }
        }
    }

    fun addGameFav(fav: Favourite) {
        var game = searchGame(fav.item!!)

        if (game != null) {
            var favourite = Favourite(game.id)

            if (!miBD.getGameDAO()!!.existFav(game)) {
                miBD.insertFav(favourite)
            }
        }
    }

    val allFilmsFavs: ArrayList<Favourite>
        get() = miBD.getFilmDAO()!!.allFavs as ArrayList<Favourite>

    fun addFilmFav(beacon: Beacon) {
        var film = searchFilm(beacon.macAddress.toString())

        if (film != null) {
            var favourite = Favourite(film.id)

            if (!miBD.getFilmDAO()!!.existFav(film)) {
                miBD.insertFav(favourite)
            }
        }
    }

    fun addFilmFav(fav: Favourite) {
        var film = searchFilm(fav.item!!)

        if (film != null) {
            var favourite = Favourite(film.id)

            if (!miBD.getFilmDAO()!!.existFav(film)) {
                miBD.insertFav(favourite)
            }
        }
    }

    fun searchFilmFav(film: String): Favourite? {
        var fav = Favourite()
        fav.item = film
        return miBD.getFilmDAO()!!.searchFav(fav)
    }

    fun isFilm(mac: String): Boolean {
        val film = Film()
        film.id = mac
        if (miBD.getFilmDAO()!!.search(film) != null)
            return true
        return false
    }

    fun isFilmFavourite(film: Film): Boolean = miBD.getFilmDAO()!!.existFav(film)

    fun isGameFavourite(game: VideoGame): Boolean = miBD.getGameDAO()!!.existFav(game)

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
