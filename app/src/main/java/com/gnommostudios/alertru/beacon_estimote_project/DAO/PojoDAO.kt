package com.gnommostudios.alertru.beacon_estimote_project.DAO

import java.util.ArrayList

interface PojoDAO {

    val allFilms: ArrayList<*>

    fun searchFilm(obj: Any): Any

    val allFavs: ArrayList<*>

    fun searchFav(obj: Any): Any?

    fun existFav(obj: Any): Boolean

}
