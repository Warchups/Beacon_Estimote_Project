package com.gnommostudios.beacon_estimote_project.DAO

import java.util.ArrayList

interface PojoDAO {

    val all: ArrayList<*>

    fun search(obj: Any): Any?

    val allFavs: ArrayList<*>

    fun searchFav(obj: Any): Any?

    fun existFav(obj: Any): Boolean

}
