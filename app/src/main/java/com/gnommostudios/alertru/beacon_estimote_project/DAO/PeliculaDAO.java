package com.gnommostudios.alertru.beacon_estimote_project.DAO;

import android.database.Cursor;
import android.util.Log;

import com.gnommostudios.alertru.beacon_estimote_project.bd.MiBD;
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Pelicula;

import java.util.ArrayList;

public class PeliculaDAO implements PojoDAO {

    @Override
    public Object search(Object obj) {
        Log.i("DAO", "Llego a search");
        Pelicula p = (Pelicula) obj;
        String condicion = "beacon = '" + p.getBeaconMac() + "'";

        String[] columnas = {
                "titulo","imagen", "beacon"
        };

        Cursor cursor = MiBD.getDB().query("peliculas", columnas, condicion, null, null, null, null);
        Log.i("DAO", "Paso el cursor");
        Pelicula nuevaPelicula = null;
        if (cursor.moveToFirst()) {
            nuevaPelicula = new Pelicula();

            Log.i("DAO", "Llego if");
            //nuevaPelicula.setId(cursor.getInt(0));
            nuevaPelicula.setTitulo(cursor.getString(0));
            nuevaPelicula.setImagen(cursor.getBlob(1));
            nuevaPelicula.setBeaconMac(cursor.getString(2));
        }

        return nuevaPelicula;
    }

    @Override
    public ArrayList getAll() {
        ArrayList<Pelicula> listaPeliculas = new ArrayList<>();

        String[] columnas = {
                "id", "titulo","imagen","beacon"
        };

        Cursor cursor = MiBD.getDB().query("peliculas", columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Pelicula nuevaPelicula = new Pelicula();

                nuevaPelicula.setId(cursor.getInt(0));
                nuevaPelicula.setTitulo(cursor.getString(1));
                nuevaPelicula.setImagen(cursor.getBlob(2));
                nuevaPelicula.setBeaconMac(cursor.getString(3));

                listaPeliculas.add(nuevaPelicula);
            }while(cursor.moveToNext());
        }

        return listaPeliculas;
    }
}
