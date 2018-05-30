package com.gnommostudios.alertru.beacon_estimote_project.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.gnommostudios.alertru.beacon_estimote_project.DAO.PeliculaDAO;
import com.gnommostudios.alertru.beacon_estimote_project.R;
import com.gnommostudios.alertru.beacon_estimote_project.pojo.Pelicula;
import com.gnommostudios.alertru.beacon_estimote_project.utils.MainBeacons;

import java.io.ByteArrayOutputStream;

public class MiBD extends SQLiteOpenHelper{

    private static SQLiteDatabase db;

    private Context context;

    private static final String database = "BeaconPelis";

    private static final int version = 3;

    private String sqlCreacionPeliculas = "CREATE TABLE peliculas ( id INTEGER PRIMARY KEY AUTOINCREMENT, titulo STRING, imagen BLOB, beacon STRING);";

    private static MiBD instance = null;

    private static PeliculaDAO peliculaDAO;

    public PeliculaDAO getPeliculaDAO() {
        return peliculaDAO;
    }

    public static MiBD getInstance(Context context) {
        if(instance == null) {
            instance = new MiBD(context);
            db = instance.getWritableDatabase();
            peliculaDAO = new PeliculaDAO();
        }

        return instance;
    }

    public static SQLiteDatabase getDB(){
        return db;
    }

    public static void closeDB(){db.close();}

    protected MiBD(Context context) {
        super( context, database, null, version );
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreacionPeliculas);

        insercionDatos(db);
        Log.i("SQLite", "Se crea la base de datos " + database + " version " + version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("SQLite", "Control de versiones: Old Version=" + oldVersion + " New Version= " + newVersion);
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS peliculas");

            db.execSQL(sqlCreacionPeliculas);

            insercionDatos(db);
            Log.i("SQLite", "Se actualiza versión de la base de datos, New version= " + newVersion);

        }
    }

    private void insercionDatos(SQLiteDatabase db){
        //Tabla Peliculas
        Bitmap dunkerqueBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.dunkerque);
        ByteArrayOutputStream baosO = new ByteArrayOutputStream(20480);
        dunkerqueBitmap.compress(Bitmap.CompressFormat.PNG, 0 , baosO);
        byte[] dunkerque = baosO.toByteArray();

        //Log.i("Origen length MiBD" , "" + origen.length);

        Bitmap paisBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_es_pais_para_viejos);
        ByteArrayOutputStream baosB = new ByteArrayOutputStream(20480);
        paisBitmap.compress(Bitmap.CompressFormat.PNG, 0 , baosB);
        byte[] pais = baosB.toByteArray();

        //Log.i("Batman length MiBD" , "" + batman.length);

        Bitmap opBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.one_piece);
        ByteArrayOutputStream baosOp = new ByteArrayOutputStream(20480);
        opBitmap.compress(Bitmap.CompressFormat.PNG, 0 , baosOp);
        byte[] op = baosOp.toByteArray();

        String sql1 = "INSERT INTO peliculas(titulo, imagen, beacon) VALUES ('Dunkerque', ?, ?);";
        String sql2 = "INSERT INTO peliculas(titulo, imagen, beacon) VALUES ('No es pais para viejos', ?, ?);";
        String sql3 = "INSERT INTO peliculas(titulo, imagen, beacon) VALUES ('One Piece Film Gold', ?, ?);";

        SQLiteStatement insert1 = db.compileStatement(sql1);
        insert1.clearBindings();
        insert1.bindBlob(1, dunkerque);
        insert1.bindString(2, MainBeacons.PURPLE);
        insert1.executeInsert();

        SQLiteStatement insert2 = db.compileStatement(sql2);
        insert2.clearBindings();
        insert2.bindBlob(1, pais);
        insert2.bindString(2, MainBeacons.BLUE);
        insert2.executeInsert();

        SQLiteStatement insert3 = db.compileStatement(sql3);
        insert3.clearBindings();
        insert3.bindBlob(1, op);
        insert3.bindString(2, MainBeacons.GREEN);
        insert3.executeInsert();

    }

    public void insercionPelicula(Pelicula p){
        if (peliculaDAO.search(p) == null) {
            String sql = "INSERT INTO peliculas (titulo, imagen) VALUES (?, ?)";
            SQLiteStatement insert = db.compileStatement(sql);
            insert.clearBindings();
            insert.bindString(1, p.getTitulo());
            insert.bindBlob(2, p.getImagen());

            insert.executeInsert();
        }
    }

    public void deletePelicula(Pelicula p) {
        String delete = "DELETE FROM peliculas WHERE imagen = ?;";
        SQLiteStatement ps = db.compileStatement(delete);
        ps.bindBlob(1, p.getImagen());
        ps.executeUpdateDelete();
    }

    public Pelicula search(Pelicula p) {
        String select = "SELECT * FROM peliculas WHERE beacon = ?;";
        SQLiteStatement ps = db.compileStatement(select);
        ps.bindString(1, p.getBeaconMac());
        ps.execute();

        return (Pelicula) peliculaDAO.search(p);
    }
}
