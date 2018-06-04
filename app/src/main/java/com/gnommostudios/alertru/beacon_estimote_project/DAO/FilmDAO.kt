package com.gnommostudios.alertru.beacon_estimote_project.DAO

import android.util.Log

import com.gnommostudios.alertru.beacon_estimote_project.bd.MiBD
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Favourite
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Film

import java.util.ArrayList

class FilmDAO : PojoDAO {

    override fun existFav(obj: Any): Boolean {
        val film = obj as Film

        val condition = "film = " + film.id

        val columns = arrayOf("id", "film", "uuid", "major", "minor", "mac")

        val cursor = MiBD.getDB().query("favourites", columns, condition, null, null, null, null)

        if (cursor.moveToFirst()) {
            return true
        }
        return false
    }

    override val allFavs: ArrayList<*>
        get() {
            val listFavs = ArrayList<Favourite>()

            val columns = arrayOf("id", "film", "uuid", "major", "minor", "mac")

            val cursor = MiBD.getDB().query("favourites", columns, null, null, null, null, null)

            if (cursor.moveToFirst()) {
                do {
                    val newFav = Favourite()

                    newFav.id = cursor.getInt(0)
                    newFav.film = cursor.getInt(1)
                    newFav.uuid = cursor.getString(2)
                    newFav.major = cursor.getInt(3)
                    newFav.minor = cursor.getInt(4)
                    newFav.mac = cursor.getString(5)

                    listFavs.add(newFav)
                } while (cursor.moveToNext())
            }

            return listFavs

        }
    override fun searchFav(obj: Any): Any {
        val fav = obj as Favourite
        val condition = "film = " + fav.film

        val columns = arrayOf("id", "film", "uuid", "major", "minor", "mac")

        val cursor = MiBD.getDB().query("favourites", columns, condition, null, null, null, null)
        var newFav: Favourite? = null
        if (cursor.moveToFirst()) {
            newFav = Favourite()

            newFav.id = cursor.getInt(0)
            newFav.film = cursor.getInt(1)
            newFav.uuid = cursor.getString(2)
            newFav.major = cursor.getInt(3)
            newFav.minor = cursor.getInt(4)
            newFav.mac = cursor.getString(5)
        }

        return newFav as Any
    }

    override val allFilms: ArrayList<*>
        get() {
            val listFilms = ArrayList<Film>()

            val columns = arrayOf("id", "title", "image", "beacon")

            val cursor = MiBD.getDB().query("films", columns, null, null, null, null, null)

            if (cursor.moveToFirst()) {
                do {
                    val newFilm = Film()

                    newFilm.id = cursor.getInt(0)
                    newFilm.title = cursor.getString(1)
                    newFilm.image = cursor.getString(2)
                    newFilm.beaconMac = cursor.getString(3)

                    listFilms.add(newFilm)
                } while (cursor.moveToNext())
            }

            return listFilms
        }

    override fun searchFilm(obj: Any): Film {
        Log.i("DAO", "Llego a searchFilm")
        val p = obj as Film
        val condition = "beacon = '" + p.beaconMac + "'"

        val columns = arrayOf("id", "title", "image", "beacon")

        val cursor = MiBD.getDB().query("films", columns, condition, null, null, null, null)
        Log.i("DAO", "Paso el cursor")
        var newFilm: Film? = null
        if (cursor.moveToFirst()) {
            Log.i("DAO", "Despues del cursor")
            newFilm = Film()

            Log.i("DAO", "Llego if")
            newFilm.id = cursor.getInt(0)
            newFilm.title = cursor.getString(1)
            newFilm.image = cursor.getString(2)
            newFilm.beaconMac = cursor.getString(3)
        }

        return newFilm!!
    }
}
