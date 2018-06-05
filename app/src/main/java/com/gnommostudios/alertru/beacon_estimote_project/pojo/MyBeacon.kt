package com.gnommostudios.alertru.beacon_estimote_project.pojo

import com.estimote.coresdk.recognition.packets.Beacon
import java.io.Serializable

class MyBeacon: Serializable {

    var idFilm: Int? = null
    var uuid: String? = null
    var major: Int? = null
    var minor: Int? = null
    var mac: String? = null
    var title: String? = null
    var image: String? = null
    var rssi: Int? = null
    var distance: Double? = null

    constructor(film: Film?, beacon: Beacon?) {
        idFilm = film!!.id
        uuid = beacon!!.proximityUUID.toString()
        major = beacon!!.major
        minor = beacon!!.minor
        mac = beacon!!.macAddress.toString()
        title = film!!.title
        image = film!!.image
        rssi = beacon!!.rssi
        distance = Math.pow(10.0, (beacon.measuredPower - beacon.rssi) / 20.0)
    }


}
