package com.gnommostudios.beacon_estimote_project.DAO

import android.util.Log
import com.gnommostudios.beacon_estimote_project.bd.MiBD
import com.gnommostudios.beacon_estimote_project.pojo.Favourite
import com.gnommostudios.beacon_estimote_project.pojo.VideoGame
import java.util.ArrayList

class VideoGameDAO : PojoDAO {
    override fun existFav(obj: Any): Boolean {
        val game = obj as VideoGame

        val condition = "item = '" + game.id + "'"

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
            val listGames = ArrayList<VideoGame>()

            val columns = arrayOf("id", "title", "players", "image")

            val cursor = MiBD.getDB().query("videogames", columns, null, null, null, null, null)

            if (cursor.moveToFirst()) {
                do {
                    val newGame = VideoGame()

                    newGame.id = cursor.getString(0)
                    newGame.title = cursor.getString(1)
                    newGame.players = cursor.getInt(2)
                    newGame.image = cursor.getString(3)

                    listGames.add(newGame)
                } while (cursor.moveToNext())
            }

            return listGames
        }

    override fun search(obj: Any): VideoGame? {
        Log.i("DAO", "Llego a search")
        val p = obj as VideoGame
        val condition = "id = '" + p.id + "'"

        val columns = arrayOf("id", "title", "players", "image")

        val cursor = MiBD.getDB().query("videogames", columns, condition, null, null, null, null)
        Log.i("DAO", "Paso el cursor")
        var newGame: VideoGame? = null
        if (cursor.moveToFirst()) {
            Log.i("DAO", "Despues del cursor")
            newGame = VideoGame()

            Log.i("DAO", "Llego if")
            newGame.id = cursor.getString(0)
            newGame.title = cursor.getString(1)
            newGame.players = cursor.getInt(2)
            newGame.image = cursor.getString(3)
        }

        return newGame
    }

}