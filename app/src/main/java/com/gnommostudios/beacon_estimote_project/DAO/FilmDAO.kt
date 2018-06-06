package com.gnommostudios.beacon_estimote_project.DAO

import android.util.Log

import com.gnommostudios.beacon_estimote_project.bd.MiBD
import com.gnommostudios.beacon_estimote_project.pojo.Favourite
import com.gnommostudios.beacon_estimote_project.pojo.Film

import java.util.ArrayList

class FilmDAO : PojoDAO {

    override fun existFav(obj: Any): Boolean {
        val film = obj as Film

        val condition = "item = '" + film.id + "'"

        val columns = arrayOf("id", "item")

        val cursor = MiBD.getDB().query("favourites", columns, condition, null, null, null, null)

        if (cursor.moveToFirst()) {
            return true
        }
        return false
    }

    override val allFavs: ArrayList<*>
        get() {
            val listFavs = ArrayList<Favourite>()

            val columns = arrayOf("id", "item")

            val cursor = MiBD.getDB().query("favourites", columns, null, null, null, null, null)

            if (cursor.moveToFirst()) {
                do {
                    val newFav = Favourite()

                    newFav.id = cursor.getInt(0)
                    newFav.item = cursor.getString(1)

                    listFavs.add(newFav)
                } while (cursor.moveToNext())
            }

            return listFavs

        }
    override fun searchFav(obj: Any): Favourite? {
        val fav = obj as Favourite
        val condition = "item = '" + fav.item + "'"

        val columns = arrayOf("id", "item")

        val cursor = MiBD.getDB().query("favourites", columns, condition, null, null, null, null)
        var newFav: Favourite? = null
        if (cursor.moveToFirst()) {
            newFav = Favourite()

            newFav.id = cursor.getInt(0)
            newFav.item = cursor.getString(1)

        }

        return newFav
    }

    override val all: ArrayList<*>
        get() {
            val listFilms = ArrayList<Film>()

            val columns = arrayOf("id", "title", "director", "image")

            val cursor = MiBD.getDB().query("films", columns, null, null, null, null, null)

            if (cursor.moveToFirst()) {
                do {
                    val newFilm = Film()

                    newFilm.id = cursor.getString(0)
                    newFilm.title = cursor.getString(1)
                    newFilm.director = cursor.getString(2)
                    newFilm.image = cursor.getString(3)

                    listFilms.add(newFilm)
                } while (cursor.moveToNext())
            }

            return listFilms
        }

    override fun search(obj: Any): Film? {
        Log.i("DAO", "Llego a search")
        val p = obj as Film
        val condition = "id = '" + p.id + "'"

        val columns = arrayOf("id", "title", "director", "image")

        val cursor = MiBD.getDB().query("films", columns, condition, null, null, null, null)
        Log.i("DAO", "Paso el cursor")
        var newFilm: Film? = null
        if (cursor.moveToFirst()) {
            Log.i("DAO", "Despues del cursor")
            newFilm = Film()

            Log.i("DAO", "Llego if")
            newFilm.id = cursor.getString(0)
            newFilm.title = cursor.getString(1)
            newFilm.director = cursor.getString(2)
            newFilm.image = cursor.getString(3)
        }

        return newFilm
    }
}
