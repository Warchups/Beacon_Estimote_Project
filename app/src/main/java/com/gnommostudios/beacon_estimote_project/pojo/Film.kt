package com.gnommostudios.beacon_estimote_project.pojo

import java.io.Serializable

class Film : Serializable {

    var id: String? = null
    var title: String? = null
    var image: String? = null
    var director: String? = null
    var favourite: Boolean = false

    constructor()
}
