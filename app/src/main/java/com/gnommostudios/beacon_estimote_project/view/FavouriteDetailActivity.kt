package com.gnommostudios.beacon_estimote_project.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gnommostudios.beacon_estimote_project.API.MyOperationalFilm
import com.gnommostudios.beacon_estimote_project.R
import com.gnommostudios.beacon_estimote_project.pojo.Favourite
import com.gnommostudios.beacon_estimote_project.pojo.Film
import com.gnommostudios.beacon_estimote_project.pojo.MyBeacon
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class FavouriteDetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    private var favourite: Favourite? = null
    private var film: Film? = null
    private var myBeacon: MyBeacon? = null

    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var coordinator: CoordinatorLayout? = null
    private var appBar: AppBarLayout? = null
    private var toolbar: Toolbar? = null
    private var actionBar: ActionBar? = null

    private var backButton: ImageView? = null
    private var favButton: ImageView? = null

    private var imageDetail: ImageView? = null
    private var coverDetailFixed: ImageView? = null
    private var coverDetailFloat: ImageView? = null

    private var containerCover: CardView? = null
    private var finalCover: CardView? = null

    private var titleDetail: TextView? = null
    private var uuidDetail: TextView? = null
    private var majorDetail: TextView? = null
    private var minorDetail: TextView? = null
    private var macDetail: TextView? = null
    private var directorDetail: TextView? = null
    private var playersDetail: TextView? = null

    private var previousPosition = 0

    private var width: Int = 0
    private var height: Int = 0

    private var mof: MyOperationalFilm? = null

    private var isFavourite: Boolean = true
    private var isFilm: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_detail)

        mof = MyOperationalFilm.getInstance(this)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        width = metrics.widthPixels // ancho absoluto en pixels
        height = metrics.heightPixels // alto absoluto en pixels

        isFavourite = intent.extras.getBoolean("IS_FAV")
        myBeacon = intent.extras.getSerializable("MY_BEACON") as MyBeacon

        isFilm = mof!!.isFilm(myBeacon!!.idItem!!)

        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar)
        collapsingToolbarLayout!!.isTitleEnabled = false

        coordinator = findViewById(R.id.coordinator)
        appBar = findViewById(R.id.appbar)
        appBar!!.addOnOffsetChangedListener(this)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar

        backButton = findViewById(R.id.back_button)
        backButton!!.setOnClickListener(this)

        favButton = findViewById(R.id.fav_button)
        favButton!!.setOnClickListener(this)

        imageDetail = findViewById(R.id.image_detail)
        coverDetailFixed = findViewById(R.id.cover_detail_2)
        coverDetailFloat = findViewById(R.id.cover_detail_1)

        containerCover = findViewById(R.id.container_cover)
        finalCover = findViewById(R.id.final_cover)
        finalCover!!.visibility = View.INVISIBLE

        titleDetail = findViewById(R.id.title_detail)
        uuidDetail = findViewById(R.id.uuid_detail)
        majorDetail = findViewById(R.id.major_detail)
        minorDetail = findViewById(R.id.minor_detail)
        macDetail = findViewById(R.id.mac_detail)
        directorDetail = findViewById(R.id.director_detail)
        playersDetail = findViewById(R.id.players_detail)

        if (isFavourite) {
            favButton!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_selected_24))
        } else {
            favButton!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_unselected_24))
        }

        val imageResource = resources.getIdentifier(myBeacon!!.image, null, packageName)

        Glide.with(this).load(imageResource).into(imageDetail)
        Glide.with(this).load(imageResource).into(coverDetailFloat)
        Glide.with(this).load(imageResource).into(coverDetailFixed)

        titleDetail!!.text = myBeacon!!.title
        uuidDetail!!.text = myBeacon!!.uuid
        majorDetail!!.text = "Major: ${myBeacon!!.major}"
        minorDetail!!.text = "Minor: ${myBeacon!!.minor}"
        macDetail!!.text = myBeacon!!.mac

        if (isFilm) {
            writeSpannable("Director: ", mof!!.searchFilm(myBeacon!!.idItem!!)!!.director!!, directorDetail!!)
            playersDetail!!.visibility = View.GONE
            directorDetail!!.visibility = View.VISIBLE
        } else {
            writeSpannable("Players: ", mof!!.searchGame(myBeacon!!.idItem!!)!!.players!!.toString(), playersDetail!!)
            playersDetail!!.visibility = View.VISIBLE
            directorDetail!!.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fav_button -> {
                isFavourite = if (isFilm)
                    mof!!.isFilmFavourite(mof!!.searchFilm(myBeacon!!.idItem!!)!!)
                else
                    mof!!.isGameFavourite(mof!!.searchGame(myBeacon!!.idItem!!)!!)

                if (isFilm) {
                    if (!isFavourite) {
                        mof!!.addFilmFav(Favourite(myBeacon!!.idItem!!))
                        favButton!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_selected_24))
                    } else {
                        mof!!.deleteFav(mof!!.searchFilmFav(myBeacon!!.idItem!!)!!)
                        favButton!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_unselected_24))
                    }
                } else {
                    if (!isFavourite) {
                        mof!!.addGameFav(Favourite(myBeacon!!.idItem!!))
                        favButton!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_selected_24))
                    } else {
                        mof!!.deleteFav(mof!!.searchGameFav(myBeacon!!.idItem!!)!!)
                        favButton!!.setImageDrawable(resources.getDrawable(R.drawable.baseline_star_unselected_24))
                    }
                }
            }
            R.id.back_button -> onBackPressed()
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (verticalOffset == 0) {
            //La imagen que esta dentro del NestedScrollView la pongo invisible
            finalCover!!.visibility = View.INVISIBLE
            //Y la que esta en el CoordinatorLayout en visible
            containerCover!!.visibility = View.VISIBLE

        } else {
            if (verticalOffset == -810) {
                //La imagen que esta dentro del NestedScrollView la pongo en visible
                finalCover!!.visibility = View.VISIBLE
                //Y la que esta en el CoordinatorLayout en invisible
                containerCover!!.visibility = View.INVISIBLE
            } else {

                //Si la pantalla es mas grande de 800x480**
                if (width > 480) {
                    if (verticalOffset < -130 && verticalOffset > -135 || verticalOffset < -135) {
                        //Si el desplazamiento esta entre -130 y -135 o se pasa de -135 por si vamos muy deprisa pongo:
                        //La imagen del NestedScrollView en visible
                        finalCover!!.visibility = View.VISIBLE
                        //Y la que esta en el CoordinatorLayout en invisible
                        containerCover!!.visibility = View.INVISIBLE
                    }
                } else {
                    if (verticalOffset < -115 && verticalOffset > -120 || verticalOffset < -125) {
                        //Si el desplazamiento esta entre -115 y -120 o se pasa de -125 por si vamos muy deprisa pongo:
                        //La imagen del NestedScrollView en visible
                        finalCover!!.visibility = View.VISIBLE
                        //Y la que esta en el CoordinatorLayout en invisible
                        containerCover!!.visibility = View.INVISIBLE
                    }
                }

                if (previousPosition < verticalOffset) {
                    //Si estoy bajando
                    //Si la pantalla es mas grande de 800x480**
                    if (width > 480) {
                        if (verticalOffset < -125 && verticalOffset > -130 || verticalOffset > -125) {
                            //Si el desplazamiento esta entre -125 y -130 o se pasa de -125 por si vamos muy deprisa pongo:
                            //La imagen del NestedScrollView en invisible
                            finalCover!!.visibility = View.INVISIBLE
                            //Y la que esta en el CoordinatorLayout en invisible
                            containerCover!!.visibility = View.VISIBLE
                        }
                    } else {
                        if (verticalOffset < -110 && verticalOffset > -115 || verticalOffset > -110) {
                            //Si el desplazamiento esta entre -110 y -115 o se pasa de -110 por si vamos muy deprisa pongo:
                            //La imagen del NestedScrollView en invisible
                            finalCover!!.visibility = View.INVISIBLE
                            //Y la que esta en el CoordinatorLayout en visible
                            containerCover!!.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        //Me guardo la posicion en la que estoy ahora para despues mirar si estoy subiendo o bajando
        previousPosition = verticalOffset
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.share_detail)
            share()

        return super.onOptionsItemSelected(item)
    }

    private fun writeSpannable(headBoard: String, content: String, textView: TextView) {
        val builder = SpannableStringBuilder()

        val headBoardSpannable = SpannableString(headBoard)
        headBoardSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, headBoard.length, 0)
        builder.append(headBoardSpannable)

        val contentSpannable = SpannableString(content)
        contentSpannable.setSpan(null, 0, content!!.length, 0)
        builder.append(contentSpannable)

        textView!!.setText(builder, TextView.BufferType.SPANNABLE)
    }

    private fun share() {
        val sat = ShareAsyncTask()
        sat.execute()
    }

    internal inner class ShareAsyncTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            if (isExternalStorageWritable()) {
                Log.i("Share", "El almacenamiento externo esta disponible :)")
                val directory = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + File.separator + "BeaconScan")
                if (!directory.exists()) {
                    if (!directory.mkdirs())
                        Log.i("logcat", "Error: No se creo el directorio privado")
                }

                val imageResource = resources.getIdentifier(myBeacon!!.image, null, packageName)
                val drawable = resources.getDrawable(imageResource)
                val bitmapDrawable = drawable as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap
                val bytes = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)

                val f = File(directory.absolutePath + File.separator + myBeacon!!.title!!.replace(" ", "_") + ".png")

                try {
                    f.createNewFile()
                    val fo = FileOutputStream(f)
                    fo.write(bytes.toByteArray())
                    val file = File(f.absolutePath)

                    file.setReadable(true, false)

                    val intent = Intent(android.content.Intent.ACTION_SEND)

                    intent.type = "image/png"
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                    intent.putExtra(Intent.EXTRA_TEXT, myBeacon!!.title + "\nHe encontrado este beacon!")
                    startActivity(Intent.createChooser(intent, "Share with"))

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                Log.i("Share", "El almacenamiento externo no esta disponible :(")
            }
            return null
        }
    }

    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

}
