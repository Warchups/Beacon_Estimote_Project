package com.gnommostudios.alertru.beacon_estimote_project.pojo

import java.io.Serializable

class Film : Serializable {

    var id: Int = 0
    var title: String? = null
    var image: ByteArray? = null
    var beaconMac: String? = null

    constructor()
}
