package com.gnommostudios.alertru.beacon_estimote_project.DAO;

import android.database.Cursor;

import com.gnommostudios.alertru.beacon_estimote_project.bd.MiBD;
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Pelicula;

import java.util.ArrayList;

public class PeliculaDAO implements PojoDAO {

    @Override
    public Object search(Object obj) {
        Pelicula p = (Pelicula) obj;
        String condicion = "id = " + p.getId();

        String[] columnas = {
                "id", "titulo","imagen"
        };

        Cursor cursor = MiBD.getDB().query("peliculas", columnas, condicion, null, null, null, null);
        Pelicula nuevaPelicula = null;
        if (cursor.moveToFirst()) {
            nuevaPelicula = new Pelicula();

            nuevaPelicula.setTitulo(cursor.getString(0));
            nuevaPelicula.setImagen(cursor.getBlob(1));
        }

        return nuevaPelicula;
    }

    @Override
    public ArrayList getAll() {
        ArrayList<Pelicula> listaPeliculas = new ArrayList<>();

        String[] columnas = {
                "id", "titulo","imagen"
        };

        Cursor cursor = MiBD.getDB().query("peliculas", columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Pelicula nuevaPelicula = new Pelicula();

                nuevaPelicula.setId(cursor.getInt(0));
                nuevaPelicula.setTitulo(cursor.getString(1));
                nuevaPelicula.setImagen(cursor.getBlob(2));

                listaPeliculas.add(nuevaPelicula);
            }while(cursor.moveToNext());
        }

        return listaPeliculas;
    }
}
