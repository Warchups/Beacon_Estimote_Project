package com.gnommostudios.beacon_estimote_project.pojo

import java.io.Serializable

class Favourite : Serializable {

    var id: Int? = null
    var item: String? = null

    constructor()

    constructor(item: String?) {
        this.item = item
    }

}
