package com.gnommostudios.alertru.beacon_estimote_project.pojo

import java.io.Serializable

class Pelicula : Serializable {

    var id: Int = 0
    var titulo: String? = null
    var imagen: ByteArray? = null

    constructor(id: Int, titulo: String?, imagen: ByteArray?) {
        this.id = id
        this.titulo = titulo
        this.imagen = imagen
    }

    constructor()
}
