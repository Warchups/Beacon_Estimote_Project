package com.gnommostudios.beacon_estimote_project.pojo

import com.estimote.coresdk.recognition.packets.Beacon
import java.io.Serializable

class MyBeacon: Serializable {

    var idItem: String? = null
    var uuid: String? = null
    var major: Int? = null
    var minor: Int? = null
    var mac: String? = null
    var title: String? = null
    var image: String? = null
    var rssi: Int? = null
    var distance: Double? = null

    constructor(film: Film?, beacon: Beacon?) {
        idItem = film!!.id
        uuid = beacon!!.proximityUUID.toString()
        major = beacon!!.major
        minor = beacon!!.minor
        mac = beacon!!.macAddress.toString()
        title = film!!.title
        image = film!!.image
        rssi = beacon!!.rssi
        distance = Math.pow(10.0, (beacon.measuredPower - beacon.rssi) / 20.0)
    }

    constructor(game: VideoGame?, beacon: Beacon?) {
        idItem = game!!.id
        uuid = beacon!!.proximityUUID.toString()
        major = beacon!!.major
        minor = beacon!!.minor
        mac = beacon!!.macAddress.toString()
        title = game!!.title
        image = game!!.image
        rssi = beacon!!.rssi
        distance = Math.pow(10.0, (beacon.measuredPower - beacon.rssi) / 20.0)
    }

}