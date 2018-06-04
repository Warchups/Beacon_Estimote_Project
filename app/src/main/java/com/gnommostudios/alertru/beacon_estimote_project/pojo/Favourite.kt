package com.gnommostudios.alertru.beacon_estimote_project.pojo

import java.io.Serializable

class Favourite : Serializable {

    var id: Int = 0
    var film: Int? = null
    var uuid: String? = null
    var major: Int? = null
    var minor: Int? = null
    var mac: String? = null

    constructor()

    constructor(film: Int?, uuid: String?, major: Int?, minor: Int?, mac: String?) {
        this.film = film
        this.uuid = uuid
        this.major = major
        this.minor = minor
        this.mac = mac
    }
}
