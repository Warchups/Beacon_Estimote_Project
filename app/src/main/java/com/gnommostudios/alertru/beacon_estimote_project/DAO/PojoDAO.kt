package com.gnommostudios.alertru.beacon_estimote_project.DAO

import java.util.ArrayList

interface PojoDAO {

    val all: ArrayList<*>

    fun search(obj: Any): Any

}
