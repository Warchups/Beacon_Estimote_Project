package com.gnommostudios.alertru.beacon_estimote_project.bd

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log

import com.gnommostudios.alertru.beacon_estimote_project.DAO.FilmDAO
import com.gnommostudios.alertru.beacon_estimote_project.R
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Favourite
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Film
import com.gnommostudios.alertru.beacon_estimote_project.utils.MainBeacons

import java.io.ByteArrayOutputStream

class MiBD private constructor(private val context: Context) : SQLiteOpenHelper(context, database, null, version) {

    private val sqlCreationFilms = "CREATE TABLE films ( id INTEGER PRIMARY KEY AUTOINCREMENT, title STRING, image STRING, beacon STRING);"
    private val sqlCreationFavs = "CREATE TABLE favourites ( id INTEGER PRIMARY KEY AUTOINCREMENT, film INTEGER NOT NULL, uuid STRING, major INTEGER, minor INTEGER, mac STRING, FOREIGN KEY (film) REFERENCES films(id));"

    companion object {

        private var db: SQLiteDatabase? = null

        private const val database = "BeaconFilms"

        private const val version = 6

        private var instance: MiBD? = null

        private var filmDAO: FilmDAO? = null

        fun getInstance(context: Context): MiBD {
            if (instance == null) {
                instance = MiBD(context)
                db = instance!!.writableDatabase
                filmDAO = FilmDAO()
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

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(sqlCreationFilms)
        db.execSQL(sqlCreationFavs)

        insertData(db)
        Log.i("SQLite", "Se crea la base de datos $database version $version")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.i("SQLite", "Control de versiones: Old Version=$oldVersion New Version= $newVersion")
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS films")
            db.execSQL("DROP TABLE IF EXISTS favourites")

            db.execSQL(sqlCreationFilms)
            db.execSQL(sqlCreationFavs)

            insertData(db)
            Log.i("SQLite", "Se actualiza versión de la base de datos, New version= $newVersion")

        }
    }

    private fun insertData(db: SQLiteDatabase) {
        val dunkerque = "@drawable/dunkerque"
        val pais = "@drawable/no_es_pais_para_viejos"
        val op = "@drawable/one_piece"

        val sql1 = "INSERT INTO films(title, image, beacon) VALUES ('Dunkerque', ?, ?);"
        val sql2 = "INSERT INTO films(title, image, beacon) VALUES ('No es pais para viejos', ?, ?);"
        val sql3 = "INSERT INTO films(title, image, beacon) VALUES ('One Piece Film Gold', ?, ?);"

        val insert1 = db.compileStatement(sql1)
        insert1.clearBindings()
        insert1.bindString(1, dunkerque)
        insert1.bindString(2, MainBeacons.PURPLE)
        insert1.executeInsert()

        val insert2 = db.compileStatement(sql2)
        insert2.clearBindings()
        insert2.bindString(1, pais)
        insert2.bindString(2, MainBeacons.BLUE)
        insert2.executeInsert()

        val insert3 = db.compileStatement(sql3)
        insert3.clearBindings()
        insert3.bindString(1, op)
        insert3.bindString(2, MainBeacons.GREEN)
        insert3.executeInsert()

    }

    fun insertFav(fav: Favourite) {
        val sql = "INSERT INTO favourites (film, uuid, major, minor, mac) VALUES (${fav.film}, ?, ${fav.major}, ${fav.minor}, ?)"
        val insert = db!!.compileStatement(sql)
        insert.clearBindings()
        insert.bindString(1, fav.uuid)
        insert.bindString(2, fav.mac)

        insert.executeInsert()
    }

    fun deleteFav(fav: Favourite) {
        val delete = "DELETE FROM favourites WHERE film = ${fav.film} AND id = ${fav.id};"
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
