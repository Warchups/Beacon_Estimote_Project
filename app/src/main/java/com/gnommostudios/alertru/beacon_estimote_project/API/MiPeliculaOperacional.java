package com.gnommostudios.alertru.beacon_estimote_project.API;

import android.content.Context;

import com.gnommostudios.alertru.beacon_estimote_project.bd.MiBD;
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Pelicula;

import java.util.ArrayList;

public class MiPeliculaOperacional {

    private MiBD miBD;

    protected MiPeliculaOperacional(Context context) {
        miBD = MiBD.getInstance(context);
    }

    private static MiPeliculaOperacional instance = null;

    public static MiPeliculaOperacional getInstance(Context context) {
        if (instance == null)
            instance = new MiPeliculaOperacional(context);

        return instance;
    }

    public void insertarPelicula(Pelicula p) {
        miBD.insercionPelicula(p);
    }

    public ArrayList<Pelicula> getAll(){
        return miBD.getPeliculaDAO().getAll();
    }

    public void delete(Pelicula p) {
        miBD.deletePelicula(p);
    }
}
