package com.gnommostudios.beacon_estimote_project.bd

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.gnommostudios.beacon_estimote_project.DAO.FilmDAO
import com.gnommostudios.beacon_estimote_project.DAO.VideoGameDAO
import com.gnommostudios.beacon_estimote_project.R
import com.gnommostudios.beacon_estimote_project.pojo.Favourite
import com.gnommostudios.beacon_estimote_project.pojo.Film
import com.gnommostudios.beacon_estimote_project.utils.MainBeacons

class MiBD private constructor(private val context: Context) : SQLiteOpenHelper(context, database, null, version) {

    private val sqlCreationGames = "CREATE TABLE videogames ( id STRING PRIMARY KEY, title STRING, players INTEGER, image STRING);"
    private val sqlCreationFilms = "CREATE TABLE films ( id STRING PRIMARY KEY, title STRING, director STRING, image STRING);"
    private val sqlCreationFavs = "CREATE TABLE favourites ( id INTEGER PRIMARY KEY AUTOINCREMENT, item STRING NOT NULL);"

    companion object {

        private var db: SQLiteDatabase? = null

        private const val database = "BeaconFilms"

        private const val version = 7

        private var instance: MiBD? = null

        private var filmDAO: FilmDAO? = null

        private var gameDAO: VideoGameDAO? = null

        fun getInstance(context: Context): MiBD {
            if (instance == null) {
                instance = MiBD(context)
                db = instance!!.writableDatabase
                filmDAO = FilmDAO()
                gameDAO = VideoGameDAO()
            }

            return instance!!
        }

        fun getDB(): SQLiteDatabase = db!!

        fun closeDB() {
            db!!.close()
        }
    }

    fun getFilmDAO(): FilmDAO? {
        return filmDAO
    }

    fun getGameDAO(): VideoGameDAO? {
        return gameDAO
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.i("SQLite", "Llego aqui")
        db.execSQL(sqlCreationGames)
        db.execSQL(sqlCreationFilms)
        db.execSQL(sqlCreationFavs)

        insertData(db)
        Log.i("SQLite", "Se crea la base de datos $database version $version")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.i("SQLite", "Control de versiones: Old Version=$oldVersion New Version= $newVersion")
        if (newVersion > oldVersion) {
            Log.i("SQLite", "Entro al if")
            db.execSQL("DROP TABLE IF EXISTS films")
            db.execSQL("DROP TABLE IF EXISTS favourites")
            db.execSQL("DROP TABLE IF EXISTS videogames")
            Log.i("SQLite", "Paso los drops")

            db.execSQL(sqlCreationGames)
            db.execSQL(sqlCreationFilms)
            db.execSQL(sqlCreationFavs)
            Log.i("SQLite", "Paso execs")

            insertData(db)
            Log.i("SQLite", "Se actualiza versión de la base de datos, New version= $newVersion")

        }
    }

    private fun insertData(db: SQLiteDatabase) {
        val gow = "@drawable/god_of_war"
        val pais = "@drawable/no_es_pais_para_viejos"
        val souls = "@drawable/dark_souls"

        val sql1 = "INSERT INTO videogames(id, title, players, image) VALUES (?, 'God of War', 1, ?);"
        val sql2 = "INSERT INTO films(id, title, director, image) VALUES (?, 'No es pais para viejos', 'Hermanos Coen', ?);"
        val sql3 = "INSERT INTO videogames(id, title, players, image) VALUES (?, 'Dark Souls', 1, ?);"

        val insert1 = db.compileStatement(sql1)
        insert1.clearBindings()
        insert1.bindString(2, gow)
        insert1.bindString(1, MainBeacons.PURPLE)
        insert1.executeInsert()

        Log.i("SQLite", "Paso el primer insert")

        val insert2 = db.compileStatement(sql2)
        insert2.clearBindings()
        insert2.bindString(2, pais)
        insert2.bindString(1, MainBeacons.BLUE)
        insert2.executeInsert()

        Log.i("SQLite", "Paso el segundo insert")

        val insert3 = db.compileStatement(sql3)
        insert3.clearBindings()
        insert3.bindString(2, souls)
        insert3.bindString(1, MainBeacons.GREEN)
        insert3.executeInsert()

        Log.i("SQLite", "Paso el tercer insert")

    }

    fun insertFav(fav: Favourite) {
        val sql = "INSERT INTO favourites (item) VALUES ('${fav.item}')"
        val insert = db!!.compileStatement(sql)

        insert.executeInsert()
    }

    fun deleteFav(fav: Favourite) {
        val delete = "DELETE FROM favourites WHERE item = '${fav.item}' AND id = ${fav.id};"
        val ps = db!!.compileStatement(delete)

        ps.executeUpdateDelete()
    }

    //Esta funcion esta mal
    fun insertFilm(film: Film) {
        val sql = "INSERT INTO films (title, image) VALUES (?, ?)"
        val insert = db!!.compileStatement(sql)
        insert.clearBindings()
        insert.bindString(1, film.title)
        //insert.bindBlob(2, film.image)

        insert.executeInsert()
    }

    fun deleteFilm(film: Film) {
        val delete = "DELETE FROM films WHERE image = ?;"
        val ps = db!!.compileStatement(delete)
        //ps.bindBlob(1, film.image)
        ps.executeUpdateDelete()
    }
}
