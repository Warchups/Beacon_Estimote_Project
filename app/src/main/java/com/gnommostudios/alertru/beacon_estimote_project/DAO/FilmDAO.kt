package com.gnommostudios.alertru.beacon_estimote_project.DAO

import android.util.Log

import com.gnommostudios.alertru.beacon_estimote_project.bd.MiBD
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Film

import java.util.ArrayList

class FilmDAO : PojoDAO {

    override val all: ArrayList<*>
        get() {
            val listFilms = ArrayList<Film>()

            val columns = arrayOf("id", "title", "image", "beacon")

            val cursor = MiBD.getDB().query("films", columns, null, null, null, null, null)

            if (cursor.moveToFirst()) {
                do {
                    val newFilm = Film()

                    newFilm.id = cursor.getInt(0)
                    newFilm.title = cursor.getString(1)
                    newFilm.image = cursor.getBlob(2)
                    newFilm.beaconMac = cursor.getString(3)

                    listFilms.add(newFilm)
                } while (cursor.moveToNext())
            }

            return listFilms
        }

    override fun search(obj: Any): Any {
        Log.i("DAO", "Llego a search")
        val p = obj as Film
        val condition = "beacon = '" + p.beaconMac + "'"

        val columns = arrayOf("title", "image", "beacon")

        val cursor = MiBD.getDB().query("films", columns, condition, null, null, null, null)
        Log.i("DAO", "Paso el cursor")
        var newFilm: Film? = null
        if (cursor.moveToFirst()) {
            newFilm = Film()

            Log.i("DAO", "Llego if")
            //newFilm.setId(cursor.getInt(0));
            newFilm.title = cursor.getString(0)
            newFilm.image = cursor.getBlob(1)
            newFilm.beaconMac = cursor.getString(2)
        }

        return newFilm as Any
    }
}
